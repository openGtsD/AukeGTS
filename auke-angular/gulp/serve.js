'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var browserSync = require('browser-sync');
var httpProxy = require('http-proxy');
var proxy = httpProxy.createProxyServer();

proxy.on('error', function (err, req, res) {
  var msg = 'Cannot proxy to ' + req.url + '. Please make sure that the target endpoint is valid and available.';
  $.util.log(msg);
  res.writeHead(500, { 'Content-Type': 'text/plain' });
  res.end(msg);
});

var proxies = [
  {
    regexp  : /^\/service\/auke/i,
    target  : 'http://localhost:8080',
    rewrite : [/^\/service\/auke/i, ''],

  }
];

function proxyMiddleware(req, res, next) {
  for (var i = 0; proxies[i]; i++) {
    if (!proxies[i].regexp.test(req.url)) continue;

    var cfg = proxies[i]
      , rewrite = cfg.rewrite
      , headers = cfg.headers;

    if (rewrite) {
      req.url = req.url.replace(rewrite[0], rewrite[1]);
    }

    if (headers) {
      for (var key in headers) {
        req.headers[key] = headers[key];
      }
    }

    return proxy.web(req, res, { target: cfg.target });
  }
  next();
}

function initBrowserSync(baseDir, files, browser) {
  browser = browser === undefined ? 'default' : browser;

  browserSync.instance = browserSync.init(files, {
    startPath: '/',
    server: {
      baseDir: baseDir,
      middleware: proxyMiddleware
    },
    logConnections: true,
    browser: browser,
    port: 3000
  });
}

gulp.task('serve', ['scripts', 'watch'], function () {
  initBrowserSync([
    'app',
    '.tmp'
  ], [
    'app/*.html',
    '.tmp/styles/**/*.css',
    'app/scripts/**/*.js',
    'app/scripts/**/*.html',
    'app/images/**/*'
  ]);
});

gulp.task('serve:dist', ['build'], function () {
  initBrowserSync('dist');
});

gulp.task('serve:e2e', function () {
  initBrowserSync(['app', '.tmp'], null, []);
});

gulp.task('serve:e2e:dist', ['watch'], function () {
  initBrowserSync('dist', null, []);
});
