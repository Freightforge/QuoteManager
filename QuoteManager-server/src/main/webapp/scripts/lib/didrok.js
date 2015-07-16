define([
        'jquery',
        'underscore',
        'backbone'
    ],
    function($, _, Backbone) {
        'use strict';

        var Didrok = {};

        Didrok.Router = Backbone.Marionette.AppRouter.extend({
            before: function() {},
            after: function() {},
            route: function(route, name, callback) {
                if (!_.isRegExp(route)) route = this._routeToRegExp(route);
                if (_.isFunction(name)) {
                    callback = name;
                    name = '';
                }
                if (!callback) callback = this[name];
                var router = this;

                Backbone.history.route(route, function(fragment) {
                    var args = router._extractParameters(route, fragment);
                    var next = function() {
                        router.execute.apply(router, [callback, args]);
                        router.trigger.apply(router, ['route:' + name].concat(args));
                        router.trigger('route', name, args);
                        Backbone.history.trigger('route', router, name, args);
                        router.after.apply(router, args);
                    };
                    router.before.apply(router, [args, next]);
                });
                return this;
            },
            execute: function(callback, args) {
                if (callback) callback.apply(this, args);
            }
        });

        return Didrok;
    });
