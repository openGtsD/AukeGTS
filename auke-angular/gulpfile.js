var gulp = require('gulp');
var rimraf = require('gulp-rimraf');
var browserSync = require('browser-sync');
var httpProxy = require('http-proxy');
var minifycss = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var usemin = require('gulp-usemin');
var jshint = require('gulp-jshint');
var babel = require('gulp-babel');

var baseDir = 'app';
var srcDir = baseDir + '/**/*.js';
var cssDir = baseDir + '/styles/*.scss';
var tmpDir = '.tmp';
var distDir = 'dist';

gulp.task('clean', function () {
  return gulp.src([tmpDir, distDir], {read: false}).pipe(rimraf());
});

gulp.task('build', ['clean'], function() {
  return gulp .src([srcDir])
      .pipe(babel())      .pipe(gulp.dest(tmpDir));
});

gulp.task('build-watch', ['build'], browserSync.reload);

// Start browsersync task and then watch files for changes
gulp.task('default', ['build'], function() {
  browserSync({
    startPath: '/',
    server: {
      baseDir: './'
    },
    logConnections: true,
    browser: 'default',
    port: 8888
  });
  gulp.watch(['*.html', 'assets/**/*.css', 'app/**/*.js', 'app/**/*.css', 'app/**/*.html'], ['build-watch']);
});