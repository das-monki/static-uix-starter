# UIx Static Site Starter

A Clojure template for building static sites with shadow-cljs and UIx, featuring server-side rendering with client-side hydration.

## Features

- **Server-Side Rendering (SSR)**: Pre-render pages for better SEO and faster initial loads
- **Client-Side Hydration**: Interactive components come alive after page load
- **Code Splitting**: Separate bundles for each page to minimize JavaScript payload
- **Hot Reloading**: Fast development with shadow-cljs
- **Static Site Generation**: Build fully static HTML files for deployment
- **Modern Stack**: Clojure, ClojureScript, UIx (React wrapper), shadow-cljs (via deps.edn)

## Prerequisites

### Option 1: With Nix (Recommended)
- [Nix package manager](https://nixos.org/download.html)
- (Optional) [direnv](https://direnv.net/) for automatic environment loading

### Option 2: Manual Installation
- Java 11+
- Node.js 16+
- Clojure CLI tools
- Babashka (optional, for task automation)

## Quick Start

### Using Nix Development Shell

1. **Enter the development shell:**
   ```bash
   nix develop
   # or with direnv installed
   direnv allow
   ```

2. **Run development commands:**
   ```bash
   # Start shadow-cljs development server
   dev

   # Start Ring server in another terminal
   server

   # Build the static site
   build-all

   # Serve static files
   serve
   ```

### Using Traditional Setup

1. **Install npm dependencies (React only):**
   ```bash
   npm install  # Installs React and React-DOM
   # or with Babashka
   bb install
   ```

   Note: shadow-cljs is managed through deps.edn, not npm.

2. **Development mode:**
   ```bash
   # Terminal 1: Start shadow-cljs
   clojure -M -m shadow.cljs.devtools.cli watch app
   # or
   bb dev

   # Terminal 2: Start Ring server (for SSR)
   clojure -M:server
   # or
   bb server
   ```

   - Shadow-cljs UI: http://localhost:8080
   - Development server: http://localhost:3000

3. **Build for production:**
   ```bash
   bb build
   # or manually:
   clojure -M -m shadow.cljs.devtools.cli release app
   clojure -M:static
   ```

   This generates static HTML files in `resources/public/`.

4. **Serve static site:**
   ```bash
   bb serve-static
   ```

   Visit http://localhost:8000

## Project Structure

```
├── src/
│   ├── main/                 # Server-side Clojure code
│   │   ├── core.clj         # HTTP server and routing
│   │   └── render.clj       # SSR rendering logic
│   └── app/
│       ├── common/          # Shared CLJC code
│       │   └── components.cljc
│       ├── pages/           # Page components (CLJC)
│       │   ├── home.cljc
│       │   └── about.cljc
│       └── client/          # Client-side ClojureScript
│           ├── home.cljs    # Home page hydration
│           └── about.cljs   # About page hydration
├── resources/public/        # Static assets and generated HTML
├── dev/                     # Development and build tools
│   ├── build.clj           # Static site generator
│   └── user.clj            # REPL utilities and helpers
├── deps.edn                # Clojure dependencies
├── shadow-cljs.edn         # Shadow-cljs configuration
├── package.json            # NPM dependencies
├── bb.edn                  # Babashka tasks
├── flake.nix               # Nix flake configuration
└── .envrc                  # direnv configuration
```

## How It Works

1. **Universal Components**: Pages are written in `.cljc` files, allowing them to be rendered on both server and client
2. **Server Rendering**: The server uses `uix.dom.server/render-to-string` to generate HTML
3. **Client Hydration**: JavaScript bundles use `uix.dom/hydrate-root` to make the static HTML interactive
4. **Code Splitting**: Each page gets its own JS bundle (home.js, about.js) plus a shared bundle
5. **Dev Tooling**: Build scripts and development utilities are kept in the `dev/` directory following Clojure conventions

## Adding New Pages

1. Create a new page component in `src/app/pages/mypage.cljc`
2. Create a hydration file in `src/app/client/mypage.cljs`
3. Add the module to `shadow-cljs.edn`:
   ```clojure
   :mypage {:init-fn app.client.mypage/init
            :depends-on #{:shared}}
   ```
4. Add rendering function in `src/main/render.clj`
5. Add route in `src/main/core.clj`
6. Update `build/static.clj` to generate the static HTML

## Available Commands

### Nix Development Shell Commands

When using `nix develop`:

- `dev` - Start shadow-cljs development server with hot reload
- `server` - Start Ring server for SSR development
- `static` - Generate static HTML files
- `build-all` - Build JS bundles and generate static files
- `serve` - Serve the static site locally on port 8000
- `clean` - Clean generated files
- `repl` - Start Clojure REPL with dev tools
- `format` - Format Clojure code with cljfmt
- `lint` - Run clj-kondo static analysis
- `update-deps` - Update deps-lock.json for Nix builds
- `build` - Build complete static site with Nix

### Babashka Tasks

If using Babashka directly:

- `bb clean` - Remove build artifacts
- `bb install` - Install npm dependencies
- `bb dev` - Start development server
- `bb server` - Start Ring server
- `bb build-js` - Build JavaScript bundles
- `bb build-static` - Generate static HTML
- `bb build` - Full production build
- `bb serve-static` - Serve static site locally

## Building with Nix

For reproducible builds using Nix:

```bash
# Build the static site
nix build

# The output will be in ./result/
ls -la ./result/

# Or build and copy to a specific location
nix build && cp -r result/* /path/to/deployment/
```

## Deployment

The generated static files in `resources/public/` can be deployed to any static hosting service:

- Netlify
- Vercel
- GitHub Pages
- AWS S3 + CloudFront
- Cloudflare Pages

Simply upload the contents of `resources/public/` to your hosting provider.

## Development

The `dev/` directory contains development and build utilities following Clojure conventions:

- **dev/build.clj**: Static site generator script
- **dev/user.clj**: REPL utilities and development helpers
- **:dev alias**: Includes dev tools on classpath for REPL development
- **:static alias**: Runs the static site generation

You can start a REPL with dev tools available using:
```bash
clojure -M:dev
```

Example REPL workflow:
```clojure
;; Start development server
(start-server)

;; Generate static files
(generate-static)

;; Test individual page rendering
(render-home)

;; Clean generated files
(clean)

;; Stop server when done
(stop-server)
```

## Tips

- Write components in `.cljc` files for universal rendering
- Use `#?(:cljs ...)` reader conditionals for client-only code
- Keep pages lightweight - heavy interactivity can be loaded after hydration
- Test both SSR and hydrated versions during development
- Build scripts and dev utilities belong in the `dev/` directory

## License

This is free and unencumbered software released into the public domain.
