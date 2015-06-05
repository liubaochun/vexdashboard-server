import Ember from 'ember';

export default Ember.Route.extend({

  actions: {
    displayModal: function() {
      this.send('showModal', 'settings-modal');
    }
  }
});
