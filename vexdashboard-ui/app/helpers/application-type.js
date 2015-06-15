import Ember from 'ember';

export function applicationType(params/*, hash*/) {
	console.log("TEST XXXXX helper");
  return params;
}

export default Ember.HTMLBars.makeBoundHelper(applicationType);
