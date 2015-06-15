import Ember from 'ember';
import PaginationShuttle from '../models/pagination-shuttle';
import PaginationCurSortKey from '../models/pagination-cur-sort-key';

export default Ember.ArrayController.extend({
	//default values. Subclasses should override the default value to customize it's own default behaviour.
	    defaultSortBy:'',
	    defaultDesc: false,
	    //fields declaration
	    queryParams:['o', 'keyWord', 'pageIndex', 'pageSize', 'sortBy', 'desc', 'totalElements'],
		o : '1ea15585-1075-4833-86c6-9321695b5ce4',
		keyWord:'',
	    pageIndex: 0,
	    pageSize: 30,
	    sortBy:function(){
	        return this.get('defaultSortBy');
	    }.property('defaultSortBy'),
	    desc:function(){
	        return this.get('defaultDesc');
	    }.property('defaultDesc'),
		totalElements: -1,
		
	    //properties used by the pagination component	    
	    pagination: PaginationShuttle.create({}),
	    //this can be observed by others to detect what key is currently sorted
	    currentSortingKey:PaginationCurSortKey.create({"key":''}),
	    init:function(){
	        this._super();
	        this.get('currentSortingKey').set('key', this.get('sortBy'));
	        this.get('currentSortingKey').set('desc', this.get('desc'));
	    },
	    actions:{
	        sortBy:function(key){
	            this.set('sortBy', key);
	            this.toggleProperty('desc');
	            this.updateCurrentSortingKey(key);
	        },
	        updatePageIndex:function(pageIndex){
	            this.set('pageIndex', pageIndex);
	        },
	        search:function(){
	            this.updateCurrentSortingKey();
	            this.set('pageIndex', 0);
	        }
	    },

	    resetPagination:function(){
	        this.set('pageIndex', 0);
	        this.set('totalElements', -1);
	        this.set('sortBy', this.get('defaultSortBy'));
	        this.set('desc', this.get('defaultDesc'));
	        //this.set('keyWord', '');
	        this.set('pagination.pageIndex', 0);
	        this.set('pagination.keyWord', '');
	    },
	    updateCurrentSortingKey:function(key, desc){
	        this.get('currentSortingKey').set('key', key ? key : this.get('sortBy'));
	        this.get('currentSortingKey').set('desc', desc ? desc : this.get('desc'));
	    }
});
