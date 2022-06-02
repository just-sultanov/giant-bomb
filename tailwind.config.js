module.exports = {
  content:
    process.env.NODE_ENV === "production"
      ? ["./public/**/*.html", "./public/js/app.js"]
      : ["./public/**/*.html", "./public/js/app.js", "./public/js/cljs-runtime/*.js"],
  theme: {
    extend: {
      backgroundImage: {},
    },
  },
  darkMode: "class",
  variants: {},
  plugins: [
    require('@tailwindcss/forms'),
  ],
};
