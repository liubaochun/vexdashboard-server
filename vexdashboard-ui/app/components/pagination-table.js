import Ember from 'ember';

export default Ember.Component.extend({
	showSearchBox : true,
    showDeselectAll : false,
    showPagesBox : true,
    //the default actions sent for updating page
    updatePageIndex:"updatePageIndex",
    search:"search",
    deselectAll:"deselectAll",
    //the pagination object
    pagination : null,
    init:function(){
        this._super();
		this.get('pagination');
    },
    startCount:function(){
        var pagination = this.get('pagination');
        return pagination.totalElements > 0 ? pagination.pageIndex * pagination.pageSize + 1 : 0;
    }.property('pagination.pageIndex', 'pagination.pageSize', 'pagination.totalElements'),

    endCount:function(){
        var pagination = this.get('pagination');
        var endCount = (pagination.pageIndex + 1) * pagination.pageSize;
        if(endCount > pagination.totalElements) {
			endCount = pagination.totalElements;
		}
        return endCount;
    }.property('pagination.pageIndex', 'pagination.pageSize', 'pagination.totalElements'),

    actions:{
        updatePageIndex:function(pageIndex){
            this.sendAction('updatePageIndex', this.get('pagination').pageIndex + pageIndex);
        },
        search:function(){
            this.sendAction('search', this.get('pagination').get('keyWord'));
        },
        deselectAll:function(){
            this.sendAction('deselectAll');
        }
    }
});
