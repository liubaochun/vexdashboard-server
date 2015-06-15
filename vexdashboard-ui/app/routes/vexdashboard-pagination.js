import Ember from 'ember';
import PaginationBase from './pagination-base';
import PaginationShuttle from '../models/pagination-shuttle';

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
                pagination = PaginationShuttle.create({});
                Ember.set(pagination, 'totalPages', 0);
                Ember.set(pagination, 'totalElements', 0);
                Ember.set(pagination, 'isLastPage', true);
                Ember.set(pagination, 'hasNextPage', false);
                Ember.set(pagination, 'hasPreviousPage', false);
                controller.set('pagination', pagination);
            }
            Ember.set(pagination, 'keyWord', controller.get('keyWord'));
            Ember.set(pagination, 'isLoading', false);
            controller.updateCurrentSortingKey(controller.get('sortBy'), controller.get('desc'));
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
        showDeleteModal: function(id, confirmMessage) {
            Ember.$('#pendingrequestsmodal').modal({ keyboard: false });
            this.controller.set("toBeDeletedId", id);
            if(confirmMessage){
                this.controller.set("confirmMessage", confirmMessage);
            }
            this.controller.set("multiple", false);
        },
        deleteConfirmedly: function() {
            Ember.$('#pendingrequestsmodal').modal('hide');
            var toBeDeletedId = this.controller.get('toBeDeletedId');
            this.delete(toBeDeletedId);
            this.controller.set('toBeDeletedId', null);
            this.controller.set("confirmMessage", null);
        },
        showDeleteMultipleModal:function(confirmMessage){
            Ember.$('#pendingrequestsmodal').modal({ keyboard: false });
            if(confirmMessage){
                this.controller.set("confirmMessage", confirmMessage);
            }
            this.controller.set("multiple", true);
        },
        deleteMultiple:function(){
            Ember.$('#pendingrequestsmodal').modal('hide');
            this.controller.set("confirmMessage", null);
            this.send('deleteSelected');
        }
    },
    delete: function(id){
        var _this = this;
        var record = this.store.getById(this.get('type'), id);
        record.deleteRecord();
        record.save().then(function(response){
            _this.refresh();
            _this.controller.set('message', _this.onDeleteSuccess(record, response));
        }, function(response){
            if(response.status === 200){
                _this.refresh();
                _this.controller.set('message', _this.onDeleteSuccess(record, response));
            }else{
                //rollback data from the local cache, instead of reload it from the server.
                record.rollback();
                _this.controller.set('error', _this.onDeleteFailure(record, response));
            }
        });
    },
    /**
     * Override this method to return message for successful deletion of a record
     * @param record
     * @param response
     * @returns {string}
     */
    onDeleteSuccess: function(record, response){
        return "";
    },
    /**
     * Override this method to return error message for failed deletion of a record
     * @param record
     * @param response
     * @returns {string}
     */
    onDeleteFailure: function(record, response){
        return "";
    }	
});
