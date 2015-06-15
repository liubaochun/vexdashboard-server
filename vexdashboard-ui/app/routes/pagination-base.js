import Ember from 'ember';
import RouteHistoryMixin from '../mixins/route-history';
import PaginationShuttle from '../models/pagination-shuttle';

/**
  * RouteHistory Mixin will be do.
  */

export default Ember.Route.extend({
	    /**
	     * sub-classes must provide the data type to this property
	     */
	    type:null,
	    queryParams: {
	        pageIndex: { refreshModel: true },
	        sortBy: { refreshModel: true },
	        desc: { refreshModel: true },
	        keyWord: { refreshModel: true }, 
			o: {refreshModel: true}
	    },
	    beforeModel:function(transition, queryParams){
	        var superResult = this._super(transition, queryParams);
	        if(this.controller && this.controller.get('pagination')){
	            this.controller.get('pagination').set('isLoading', true);
	        }
	        return superResult;
	    },
	    model: function(params) {
	        //this.store.unloadAll(this.get('type'));
	        return this.store.find(this.get('type'), params);
	    },
	    setupController:function(controller, model){
	        if(model && model.type){
	            var newPagination = this.store.metadataFor(model.type.typeKey);
	            var pagination = controller.get('pagination');
	            if(!pagination){
	                pagination = PaginationShuttle.create({});
	                controller.set('pagination', pagination);
	            }
	            for(var key in newPagination){
	                Ember.set(pagination, key, newPagination[key]);
	            }
	            //Ember.set(pagination, 'keyWord', controller.get('keyWord'));
	            Ember.set(pagination, 'isLoading', false);
	            controller.updateCurrentSortingKey(controller.get('sortBy'), controller.get('desc'));
	        }
	        this._super(controller, model);
	    }
});
