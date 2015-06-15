import { moduleForModel, test } from 'ember-qunit';

moduleForModel('pagination', 'Unit | Model | pagination', {
  // Specify the other units that are required for this test.
  needs: []
});

test('it exists', function(assert) {
  var model = this.subject();
  // var store = this.store();
  assert.ok(!!model);
});
