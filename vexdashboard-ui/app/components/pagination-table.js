import Ember from 'ember';

export default Ember.Component.extend({
	//the default actions sent for updating page
    updatePageIndex:"updatePageIndex",
    //the pagination object
    pagination : null,
	indexArray : null,
	hasPreviousPage : false,
	hasNextPage : false,
	previousPageIndex : null,
	nextPageIndex : null,
	
    init:function(){
		this.updatePagination();
        this._super();
    },
    
    actions:{
    	updatePageIndex : function(pageIndexOnPage) {
			var pagination = this.get('pagination');
			if (pagination.pageIndex + 1 !== pageIndexOnPage) {
				this.sendAction("updatePageIndex", pageIndexOnPage - 1);
			}
		}
    }, 
	
	updatePaginationView : function() {
		this.updatePagination();
	}.observes('pagination.pageIndex'),
	
	updatePagination : function() {
		var paginationCurrent = this.get('pagination');
		if (paginationCurrent) {
			var indexArray = [];
			for (var i = 0; i < paginationCurrent.totalPages; i++) {
				if (paginationCurrent.pageIndex === i) {					
					indexArray.pushObject({"index" : i + 1, "isCurrent" : true});
				} else {
					indexArray.pushObject({"index" : i + 1, "isCurrent" : false});
				}
			}
			this.set('indexArray', indexArray);
			if (paginationCurrent.hasNextPage === true) {
				this.set("hasNextPage", true);
				this.set("nextPageIndex", paginationCurrent.pageIndex + 2);
			} else {
				this.set("hasNextPage", false);
				this.set("nextPageIndex", paginationCurrent.pageIndex + 1);
			}
			
			if (paginationCurrent.hasPreviousPage === true) {
				this.set("previousPageIndex", paginationCurrent.pageIndex);
				this.set("hasPreviousPage", true);
			} else {
				this.set("hasPreviousPage", false);
				this.set("previousPageIndex", paginationCurrent.pageIndex + 1);
			}
		}
	}
});
