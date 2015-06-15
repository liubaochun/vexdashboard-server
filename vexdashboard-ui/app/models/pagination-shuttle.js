import Ember from 'ember';

export default Ember.Object.extend({
    pageIndex : 0,
    pageSize : 20,
    totalElements : -1,
    totalPages : null,
    isFirstPage : true,
    isLastPage : false,
    hasNextPage : true,
    hasPreviousPage : false,
    keyWord : '', //This is cached locally only, server doesn't return back this info.
    isLoading:false, //used by client only, server is unaware of it.  
	firstElement : 0,
	lastElement: 0
});