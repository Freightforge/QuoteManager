define([
        'backbone',
        'hbs!tmpl/navbar'
    ],
    function(Backbone, navBarTpl) {
        'use strict';

        var Region = Backbone.Marionette.Region.extend({
            attachHtml: function(view) {
                this.$el.replaceWith(view.el);
            }
        });

        var NavBarView = Backbone.Marionette.LayoutView.extend({
            regionClass: Region,
            template: navBarTpl,
            className: 'container',
            events :{
                'click .toggle_fullscreen': function() { App.commands.execute("App:fullScreen"); },
                'click #menu-medium': function() { }
            },
            regions: {
            },
            serializeData: function() {
                return {title: this.options.title};
            }
        });

        return NavBarView;
    });
