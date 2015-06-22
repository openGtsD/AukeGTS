'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var browserSync = require('browser-sync');

gulp.task('webdriver:update', $.protractor.webdriver_update);

gulp.task('webdriver:standalone', $.protractor.webdriver_standalone);

gulp.task('protractor', ['webdriver:update', 'wiredep'], function (done) {
  var testFiles = [
    'test/e2e/**/*.js'
  ];

  gulp.src(testFiles)
    .pipe($.protractor.protractor({
      configFile: 'test/protractor.conf.js',
    }))
    .on('error', function (err) {
      throw err;
    })
    .on('end', function () {
      done();
      browserSync.instance.cleanup();
      process.exit();
    });
});

gulp.task('e2e', ['serve:e2e', 'protractor']);
gulp.task('e2e:dist', ['serve:e2e:dist', 'protractor']);
