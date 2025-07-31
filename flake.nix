{
  inputs = {
    # Package sets
    nixpkgs.url = "github:nixos/nixpkgs/nixos-25.05";

    clj-nix = {
      url = "github:jlesquembre/clj-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };

    flake-parts.url = "github:hercules-ci/flake-parts";

    devshell = {
      url = "github:numtide/devshell";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      clj-nix,
      flake-parts,
      devshell,
      ...
    }@inputs:
    let
      inherit (nixpkgs) lib;
      mkStaticSite =
        name: system: pkgs:
        let
          projectSrc = ./.;
          deps-cache = clj-nix.packages.${system}.mk-deps-cache {
            lockfile = (projectSrc + "/deps-lock.json");
          };

          src = projectSrc;

          node-modules = pkgs.importNpmLock.buildNodeModules {
            npmRoot = src;
            nodejs = pkgs.nodejs;
          };
        in
        pkgs.stdenv.mkDerivation {
          inherit src;
          name = name;

          # Build time deps
          nativeBuildInputs = [
            pkgs.jdk23
            pkgs.clojure
            (pkgs.clojure.override { jdk = pkgs.jdk23; })
            clj-nix.packages.${system}.clj-builder
          ];

          buildInputs = [ pkgs.nodejs ];

          outputs = [ "out" ];

          buildPhase = ''
            runHook preBuild

            # Set up npm dependencies.
            ln -s ${node-modules}/node_modules node_modules

            # Set up clojure dependencies.
            export HOME="${deps-cache}"
            export JAVA_TOOL_OPTIONS="-Duser.home=${deps-cache}"

            export CLJ_CONFIG="$HOME/.clojure"
            export CLJ_CACHE="$TMP/cp_cache"
            export GITLIBS="$HOME/.gitlibs"

            clj-builder --patch-git-sha $(pwd)

            # Build JavaScript bundles
            clojure -M -m shadow.cljs.devtools.cli release app

            # Generate static HTML files
            clojure -M:static

            runHook postBuild
          '';

          installPhase = ''
            runHook preInstall

            # Copy the generated static site to output
            ${lib.getBin pkgs.rsync}/bin/rsync \
              -av \
              --exclude='node_modules' \
              --exclude='cljs-runtime' \
              --recursive \
              resources/public/ \
              $out/

            runHook postInstall
          '';
        };
    in
    inputs.flake-parts.lib.mkFlake { inherit inputs; } rec {

      systems = [
        "aarch64-darwin"
        "x86_64-linux"
        "x86_64-darwin"
        "aarch64-linux"
      ];

      imports = [
        inputs.flake-parts.flakeModules.easyOverlay
        inputs.devshell.flakeModule
      ];

      perSystem =
        {
          pkgs,
          system,
          config,
          ...
        }:
        {
          _module.args.pkgs = import self.inputs.nixpkgs {
            inherit system;
          };

          packages = {
            default = config.packages.static-site;
            static-site = mkStaticSite "static-uix-site" system pkgs;
          };

          devshells =
            let
              buildNodeModules = pkgs.importNpmLock.buildNodeModules {
                npmRoot = ./.;
                inherit (pkgs) nodejs;
              };
            in
              {
            default = {
              packages = [
                pkgs.jdk23
                (pkgs.clojure.override { jdk = pkgs.jdk23; })
                pkgs.babashka
                pkgs.nodePackages.npm
                pkgs.nodejs
                pkgs.tree
                pkgs.git
                pkgs.cljfmt
                pkgs.clj-kondo
                pkgs.nixfmt-rfc-style
                pkgs.python3  # For serving static files
              ];

              commands = [
                {
                  name = "update-deps";
                  help = "Update deps-lock.json for Nix builds";
                  command = ''
                    nix run github:jlesquembre/clj-nix#deps-lock
                  '';
                }
                {
                  name = "update-flake";
                  help = "Update flake lock file";
                  command = ''
                    nix flake update
                  '';
                }
                {
                  name = "dev";
                  help = "Start development server with hot reload";
                  command = ''
                    if [[ "$(readlink -f ./node_modules)" == ${builtins.storeDir}* ]]; then
                      rm -f ./node_modules
                    fi
                    ln -sf ${buildNodeModules}/node_modules ./node_modules

                    clojure -M -m shadow.cljs.devtools.cli watch app
                  '';
                }
                {
                  name = "server";
                  help = "Start Ring server for SSR development";
                  command = ''
                    clojure -M:server
                  '';
                }
                {
                  name = "build";
                  help = "Build complete static site (JS + HTML)";
                  command = ''
                    echo "Building JavaScript bundles..."
                    clojure -M -m shadow.cljs.devtools.cli release app
                    echo "Generating static HTML files..."
                    clojure -M:static
                    echo "Build complete! Static site ready in resources/public/"
                  '';
                }
                {
                  name = "serve";
                  help = "Serve the static site locally";
                  command = ''
                    cd resources/public && python3 -m http.server 8000
                  '';
                }
                {
                  name = "clean";
                  help = "Clean generated files";
                  command = ''
                    rm -rf resources/public/assets/js .shadow-cljs resources/public/*.html
                  '';
                }
                {
                  name = "format";
                  help = "Format Clojure code";
                  command = ''
                    cljfmt fix src dev
                  '';
                }
                {
                  name = "check";
                  help = "Check if code is formatted";
                  command = ''
                    cljfmt check src dev
                  '';
                }
                {
                  name = "lint";
                  help = "Static code analysis with clj-kondo";
                  command = ''
                    clj-kondo --lint src dev
                  '';
                }
                {
                  name = "nix-fmt";
                  help = "Format Nix code";
                  command = ''
                    find . -name "*.nix" -type f -print0 | xargs -0 nixfmt;
                  '';
                }
                {
                  name = "repl";
                  help = "Start Clojure REPL with dev tools";
                  command = ''
                    clojure -M:dev
                  '';
                }
              ];

              env = [
                {
                  name = "NODE_PATH";
                  value = "${buildNodeModules}/node_modules";
                }
              ];
            };
          };
        };
    };
}
