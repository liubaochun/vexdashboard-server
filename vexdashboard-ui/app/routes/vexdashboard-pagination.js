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
           
            this._super(controller, []);
        }else{
            this._super(controller, model);
        }
    },
    actions:{
       
    }
});
