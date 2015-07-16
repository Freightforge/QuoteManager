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
            exports: '$.fn.popover'
        }
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
        underscore: 'bower_components/underscore/underscore',
        'require-handlebars-plugin': 'bower_components/require-handlebars-plugin/hbs',
        handlebars: 'bower_components/require-handlebars-plugin/hbs',
        i18nprecompile: 'bower_components/require-handlebars-plugin/hbs/i18nprecompile',
        json2: 'bower_components/require-handlebars-plugin/hbs/json2',
        text: 'bower_components/requirejs-text/text',
        i18n: 'bower_components/requirejs-i18n/i18n',
        tmpl: 'template',
        hbs: 'bower_components/require-handlebars-plugin/hbs'
    },
    locale: 'en_us',
    hbs: {
        disableI18n: true
    },
    enforceDefine: true,
    waitSeconds: 7
});
