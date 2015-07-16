define([
        'jquery',
        'backbone',
        'lib/didrok',

        'view/navbarView',

        'lib/bootstrap'
    ],
    function($, Backbone, Didrok, NavBarView) {
        var App = window.App = new Backbone.Marionette.Application();

        /* Add application regions here */
        App.addRegions({
            navigation: '#navbar',
            mainContent: '#main-content'
        });

        App.Routing = {
            showRoute: function() {
                var route = getRoutePath(arguments);
                Backbone.history.navigate(route, true);
            }
        };
        var getRoutePath = function(routeParts) {
            var base = routeParts[0];
            var length = routeParts.length;
            var route = base;

            if (length > 1){
                for(var i = 1; i < length; i++){
                    var arg = routeParts[i];
                    if (arg){
                        route = route + '/' + arg;
                    }
                }
            }
            return route;
        };

        App.showWelcome = function() {
            console.info('Show welcome');
        };

        App.Routing.CommonRouting = {};
        App.Routing.CommonRouting.Router = Didrok.Router.extend({
            appRoutes: {
                '': 'showWelcome',
            },
            controller: App
        });

        /* Add initializers here */
        App.addInitializer(function() {
            this.Routing.CommonRouting.router = new this.Routing.CommonRouting.Router({
                controller: this
            });
        });
        App.addInitializer(function() {
            window.setInterval(function() {App.vent.trigger('uiWantsToRefreshTimestamps');}, 1000);
        });

        App.vent.on('uiWantsToRefreshTimestamps', function() {
            $('.js-relative-timestamp').each(function() {
                var el = $(this);
                var date = el.data('time');
                if (!_.isEmpty(date)) {
                    el.text(moment(date).fromNow());
                }
            });
        });

        App.addInitializer(function() {
            App.navigation.show(new NavBarView({title:''}));
            //App.mainMenu.show(new MainMenuView());
            App.vent.trigger('App:startup');
        });

        App.on('start', function(options) {
            if (Backbone.history){
                Backbone.history.start();
            }
        });

        $(document).ready(function() {
            console.log('  ______        _       _     _   ______\n |  ____|      (_)     | |   | | |  ____|\n | |__ _ __ ___ _  __ _| |__ | |_| |__ ___  _ __ __ _  ___\n |  __| \'__/ _ \\ |/ _` | \'_ \\| __|  __/ _ \\| \'__/ _` |/ _ \\\n | |  | | |  __/ | (_| | | | | |_| | | (_) | | | (_| |  __/\n |_|  |_|  \\___|_|\\__, |_| |_|\\__|_|  \\___/|_|  \\__, |\\___| By ****\n                   __/ |                         __/ |\n                  |___/                         |___/\n\nHi there, nice to meet you!\n\nFind us on http://www.freightforge.com');
            App.start();
        });

        return App;
    });
