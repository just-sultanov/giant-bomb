module.exports = {
  content:
    process.env.NODE_ENV === "production"
      ? ["./public/**/*.html", "./public/js/app.js"]
      : ["./public/**/*.html", "./public/js/app.js", "./public/js/cljs-runtime/*.js"],
  theme: {
    extend: {
      gridTemplateRows: {
        '[auto,auto,1fr]': 'auto auto 1fr',
      },
    },
  },
  darkMode: "class",
  variants: {},
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/aspect-ratio'),
  ],
};
