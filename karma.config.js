process.env.CHROME_BIN = require("puppeteer").executablePath();

module.exports = function (config) {
  config.set({
    singleRun: true,
    browsers: ["ChromeHeadlessNoSandbox"],
    customLaunchers: {
      ChromeHeadlessNoSandbox: {
        base: "ChromeHeadless",
        flags: ["--no-sandbox"],
      },
    },
    basePath: "public",
    files: ["test/assets/ci.js"],
    frameworks: ["cljs-test"],
    plugins: ["karma-cljs-test", "karma-chrome-launcher", "karma-junit-reporter", "karma-coverage"],
    colors: true,
    logLevel: config.LOG_INFO,
    client: {
      args: ["shadow.test.karma.init"],
      singleRun: true,
    },

    reporters: ["junit", "coverage", "progress"],
    preprocessors: {
      'test/assets/*.js': ['coverage']
    },
    junitReporter: {
      outputDir: "test/reports/junit",
      outputFile: undefined,
      suite: "",
    },
    coverageReporter: {
      type: "lcov",
      dir: "test/reports/coverage",
      subdir: ".",
    },
  });
};
