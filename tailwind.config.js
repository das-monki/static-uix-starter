/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{clj,cljs,cljc}",
    "./resources/**/*.html"
  ],
  safelist: [
    // Button variant classes that are dynamically generated via (str "btn btn-" (name variant))
    // Tailwind's purging can't detect these since they're not literal strings in the source
    'btn-primary',
    'btn-secondary', 
    'btn-danger'
  ],
  theme: {
    extend: {
      fontFamily: {
        'sans': ['-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'sans-serif'],
      }
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
}