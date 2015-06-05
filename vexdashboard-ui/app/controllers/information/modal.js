import Ember from 'ember';

export default Ember.Controller.extend({
    actions: {
      cancel: function() {
        this.send('closeModal');
      }
    }
});
