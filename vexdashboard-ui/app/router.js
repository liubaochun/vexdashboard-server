import Ember from 'ember';
import config from './config/environment';

var Router = Ember.Router.extend({
  location: config.locationType
});

export default Router.map(function() {
  this.route('index', {path:'/'});
  this.resource('vexboxs', {path: '/vexboxs'}, function() {
        this.route('query');
    });
  this.route('Pagination');
  this.route('VexdashboardPagination');
  this.route('PaginationBase');
});
