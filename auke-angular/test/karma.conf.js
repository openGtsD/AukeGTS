module.exports = function(config){
  config.set({

    basePath : '../',

    files : [
      '../app/bower_components/angular/angular.scripts',
      'app/bower_components/angular-route/angular-route.scripts',
      'app/bower_components/angular-mocks/angular-mocks.scripts',
      '../app/scripts/components/**/*.scripts',
      'app/view*/**/*.scripts'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
