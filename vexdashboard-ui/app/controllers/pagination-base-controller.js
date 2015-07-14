import Ember from 'ember';	
import Pagination from '../models/pagination';
import CurrentSortingKey from '../models/current-sorting-key';

export default Ember.ArrayController.extend({
	//default values. Subclasses should override the default value to customize it's own default behaviour.
	    defaultSortBy : '',
	    defaultDesc : false,
	    //fields declaration
	    o : '1ea15585-1075-4833-86c6-9321695b5ce4',
		keyWord : '',
	    pageIndex : 0,
	    pageSize : Ember.computed.alias('pagination.pageSize'),
		
	    //properties used by the pagination component	    
	    pagination: Pagination.create({}),
	    //this can be observed by others to detect what key is currently sorted
		currentSortingKey: null,
		
	    init : function(){
	        this.set('pagination', Pagination.create({}));
		    this.set('currentSortingKey', CurrentSortingKey.create({"key":this.get('sortBy'), "desc": this.get('desc')}));
		    this._super();
		},

		updateCurrentSortingKey:function(key, desc){
			this.get('currentSortingKey').set('key', key ? key : this.get('sortBy'));
			this.get('currentSortingKey').set('desc', desc ? desc : this.get('desc'));
		}
});
