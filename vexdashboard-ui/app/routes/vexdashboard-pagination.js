import Ember from 'ember';
import PaginationBase from './pagination-base';

export default PaginationBase.extend({
    model: function(params) {
        return this._super(params).then(null, function(reason){
            console.log('error occurred!!!');
            return {paginationError: {responseText: reason.responseText, status: reason.status, statusText: reason.statusText}};
        });
    },
    setupController:function(controller, model){
        //Setup error message when server returns error for a page request.
        if(model && model.paginationError){
            var pagination = controller.get('pagination');
            if(!pagination){
                pagination = Pagination.create({});
                Ember.set(pagination, 'totalPages', 0);
                Ember.set(pagination, 'totalElements', 0);
                Ember.set(pagination, 'isLastPage', true);
                Ember.set(pagination, 'hasNextPage', false);
                Ember.set(pagination, 'hasPreviousPage', false);
                controller.set('pagination', pagination);
            }
            Ember.set(pagination, 'keyWord', controller.get('keyWord'));
            Ember.set(pagination, 'isLoading', false);
            //controller.updateCurrentSortingKey(controller.get('sortBy'), controller.get('desc'));
            var errorMsg = model.paginationError.responseText.match(/<h1>.*<\/h1>/);
            if(errorMsg){
                controller.set('error', errorMsg[0].replace(/<h1>|<\/h1>|.*Fault: /g, ''));
            }else{
                controller.set('error', "status code:" + model.paginationError.status + "<br>" + model.paginationError.statusText);
            }
            this._super(controller, []);
        }else{
            this._super(controller, model);
        }
    },
    actions:{
       
    }
});
