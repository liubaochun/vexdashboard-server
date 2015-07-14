import Ember from 'ember';

export function activeForCurrentPage(params/*, hash*/) {
  return params;
}

export default Ember.HTMLBars.makeBoundHelper(activeForCurrentPage);
