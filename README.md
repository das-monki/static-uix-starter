# UIx Static Site Starter

A Clojure template for building static sites with shadow-cljs and UIx, featuring server-side rendering with client-side hydration.

## Features

- **Server-Side Rendering (SSR)**: Pre-render pages for better SEO and faster initial loads
- **Client-Side Hydration**: Interactive components come alive after page load
- **Code Splitting**: Separate bundles for each page to minimize JavaScript payload
- **Hot Reloading**: Fast development with shadow-cljs
- **Static Site Generation**: Build fully static HTML files for deployment
- **Modern Stack**: Clojure, ClojureScript, UIx (React wrapper), shadow-cljs

## Prerequisites

- Java 11+
- Node.js 16+
- Clojure CLI tools
- Babashka (optional, for task automation)

## Quick Start

1. **Install dependencies:**
   ```bash
   npm install
   # or with Babashka
   bb install
   ```

2. **Development mode:**
   ```bash
   # Terminal 1: Start shadow-cljs
   npm run dev
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
   npm run build
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
│   │   ├── render.clj       # SSR rendering logic
│   │   └── static.clj       # Static site generator
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
├── deps.edn                # Clojure dependencies
├── shadow-cljs.edn         # Shadow-cljs configuration
├── package.json            # NPM dependencies
└── bb.edn                  # Babashka tasks
```

## How It Works

1. **Universal Components**: Pages are written in `.cljc` files, allowing them to be rendered on both server and client
2. **Server Rendering**: The server uses `uix.dom.server/render-to-string` to generate HTML
3. **Client Hydration**: JavaScript bundles use `uix.dom.client/hydrate-root` to make the static HTML interactive
4. **Code Splitting**: Each page gets its own JS bundle (home.js, about.js) plus a shared bundle

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

## Available Tasks (Babashka)

- `bb clean` - Remove build artifacts
- `bb install` - Install npm dependencies
- `bb dev` - Start development server
- `bb server` - Start Ring server
- `bb build-js` - Build JavaScript bundles
- `bb build-static` - Generate static HTML
- `bb build` - Full production build
- `bb serve-static` - Serve static site locally

## Deployment

The generated static files in `resources/public/` can be deployed to any static hosting service:

- Netlify
- Vercel
- GitHub Pages
- AWS S3 + CloudFront
- Cloudflare Pages

Simply upload the contents of `resources/public/` to your hosting provider.

## Tips

- Write components in `.cljc` files for universal rendering
- Use `#?(:cljs ...)` reader conditionals for client-only code
- Keep pages lightweight - heavy interactivity can be loaded after hydration
- Test both SSR and hydrated versions during development

## License

This is free and unencumbered software released into the public domain.