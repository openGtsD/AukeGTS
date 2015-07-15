'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();

gulp.task('wiredep', function () {
  var wiredep = require('wiredep').stream;

  gulp.src('app/styles/*.scss')
    .pipe(wiredep({
      directory: 'app/bower_components',
      exclude: [
        '/bootstrap/dist' // already use Bootstrap-Sass
      ]
    }))
    .pipe(gulp.dest('app/styles'));

  gulp.src('app/*.html')
    .pipe(wiredep({
      directory: 'app/bower_components',
      exclude: [
        '/bootstrap-sass-official/assets/javascripts/' // already use angular-bootstrap
      ]
    }))
    .pipe($.inject(gulp.src('app/scripts/**/*.js'), {
      read: false,
      starttag: '<!-- inject:scripts -->',
      relative: true
    }))
    .pipe(gulp.dest('app'));
});
