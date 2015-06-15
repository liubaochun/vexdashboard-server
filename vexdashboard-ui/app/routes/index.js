import Ember from 'ember';

export default Ember.Route.extend({
  beforeModel: function() {
    this.transitionTo('vexboxs.query');
  },	
  actions: {
    displayModal: function() {
      this.send('showModal', 'settings-modal');
    }
  }
});
