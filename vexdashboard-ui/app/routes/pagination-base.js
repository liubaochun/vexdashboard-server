import Ember from 'ember';
import RouteHistoryMixin from '../mixins/route-history';	
import Pagination from '../models/pagination';

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
	 		pageSize: { refreshModel: true },
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
			//Go to the previous page if the current page has no data. This can happen after
			//removing all records in the last page, but the pageIndex is not auto-decreased.
			if(Ember.isArray(model) && Ember.isEmpty(model) && controller.get('pageIndex') > 0){
				controller.set('pageIndex', controller.get('pageIndex') - 1);
				this.transitionTo(this.get('routeName'));
				return;
			}
			
	        if(model && model.type){
	            var newPagination = this.store.metadataFor(model.type.typeKey);
				
	            //var pagination = controller.get('pagination');
	            //if(!pagination){
	            //    pagination = Pagination.create({});
	            //    controller.set('pagination', pagination);
	            //}
				var pagination = Pagination.create({});
				if (newPagination) {
					for(var key in newPagination){
		                Ember.set(pagination, key, newPagination[key]);
		            }
				}
	            controller.updateCurrentSortingKey(controller.get('sortBy'), controller.get('desc'));
				controller.set('pagination', pagination);
	        }
	        this._super(controller, model);
	    }
});
