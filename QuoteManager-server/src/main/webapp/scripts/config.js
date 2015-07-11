require.config({
    baseUrl: '/scripts',
    deps: [
        'backbone.marionette',
        'lib/bootstrap',
        'main'
    ],
    shim: {
        backbone: {
            deps: [
                'underscore',
                'jquery'
            ],
            exports: 'Backbone'
        },
        'lib/bootstrap': {
            deps: [
                'jquery'
            ],
            exports: 'jquery'
        },
        'jquery.atmosphere': [
            'jquery'
        ],
        'jquery.breakpoints': [
            'jquery'
        ],
        'jquery.circliful': [
            'jquery'
        ]
    },
    packages: [

    ],
    paths: {
        backbone: 'bower_components/backbone/backbone',
        'backbone.babysitter': 'bower_components/backbone.babysitter/lib/backbone.babysitter',
        'backbone.marionette': 'bower_components/backbone.marionette/lib/core/backbone.marionette',
        'backbone.wreqr': 'bower_components/backbone.wreqr/lib/backbone.wreqr',
        jquery: 'bower_components/jquery/dist/jquery',
        json3: 'bower_components/json3/lib/json3',
        modernizr: 'bower_components/modernizr/modernizr',
        moment: 'bower_components/moment/moment',
        requirejs: 'bower_components/requirejs/require',
        'requirejs-i18n': 'bower_components/requirejs-i18n/i18n',
        'requirejs-text': 'bower_components/requirejs-text/text',
        underscore: 'bower_components/underscore/underscore'
    },
    locale: 'en_us',
    hbs: {
        disableI18n: true
    }
});
