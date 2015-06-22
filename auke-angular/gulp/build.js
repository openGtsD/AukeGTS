'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var saveLicense = require('uglify-save-license');
var mainBowerFiles = require('main-bower-files');
var eventStream = require('event-stream');
var os = require('os');

gulp.task('styles', function () {
  return gulp.src('app/styles/*.scss')
    .pipe($.plumber())
    .pipe($.rubySass({style: 'expanded'}))
    .pipe($.autoprefixer('last 1 version'))
    .pipe(gulp.dest('.tmp/styles'))
    .pipe($.size());
});

gulp.task('scripts', function () {
  return gulp.src(['app/scripts/**/*.js', 'test/**/*.js'])
    .pipe($.jshint())
    .pipe($.jshint.reporter('jshint-stylish'))
    .pipe($.size());
});

gulp.task('views', function () {
  return gulp.src('app/scripts/**/*.html')
    .pipe($.minifyHtml({
      empty : true,
      spare : true,
      quotes: true
    }))
    .pipe($.ngHtml2js({
      moduleName: "aukeGTS",
      prefix    : "scripts/"
    }))
    .pipe(gulp.dest(".tmp/views"))
    .pipe($.size());
});

gulp.task('html', ['styles', 'scripts', 'views'], function () {
  var assets = $.useref.assets();

  return gulp.src('app/*.html')
    .pipe($.inject(gulp.src('.tmp/views/**/*.js'), {
      read        : false,
      starttag    : '<!-- inject:views -->',
      addRootSlash: false,
      addPrefix   : '../'
    }))
    .pipe(assets)
    .pipe($.rev())
    .pipe($.if('**/*.js', $.ngAnnotate()))
    .pipe($.if('**/*.js', $.uglify({preserveComments: saveLicense})))
    .pipe($.if('**/*.css', $.replace('bower_components/bootstrap-sass-official/assets/fonts/bootstrap', 'fonts')))
    .pipe($.if('**/*.css', $.csso()))
    .pipe(assets.restore())
    .pipe($.useref())
    .pipe($.revReplace())
    .pipe(gulp.dest('dist'))
    .pipe($.size());
});

gulp.task('images', function () {
  return eventStream.merge(
    gulp.src('app/images/**/*'),
    gulp.src(mainBowerFiles())
      .pipe($.filter('**/*.{png,jpg,jpeg,gif}'))
      .pipe($.flatten())
  )
    .pipe($.plumber())
    .pipe($.cache($.imagemin({
      optimizationLevel: 3,
      progressive      : true,
      interlaced       : true
    }), {
      fileCache: new $.cache.Cache({tmpDir: os.tmpdir()}) // reset tmpDir
    }))
    .pipe(gulp.dest('dist/images'))
    .pipe($.size());
});

gulp.task('fonts', function () {
  return eventStream.merge(
    gulp.src('app/fonts/**/*'),
    gulp.src(mainBowerFiles())
      .pipe($.filter('**/*.{eot,svg,ttf,woff}'))
      .pipe($.flatten())
  )
    .pipe(gulp.dest('dist/fonts'))
    .pipe($.size());
});

gulp.task('resources', function () {
  return eventStream.merge(
    gulp.src('app/resources/**/*')
      .pipe(gulp.dest('dist/resources'))
      .pipe($.size()),
    gulp.src('app/i18n/**/*')
      .pipe(gulp.dest('dist/i18n'))
      .pipe($.size()));
});

gulp.task('clean', function () {
  return gulp.src(['.tmp', 'dist'], {read: false}).pipe($.rimraf());
});

gulp.task('build', ['html', 'images', 'fonts', 'resources']);
