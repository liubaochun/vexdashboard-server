import DS from 'ember-data';
import Ember from 'ember';

var inflector = Ember.Inflector.inflector;
inflector.irregular('vexbox', 'vexboxs');

export default DS.Model.extend({
  createdOn: DS.attr('LocalDate'),
  lastUpdatedOn: DS.attr('LocalDate'),
  organizationId: DS.attr('string'),
  publishStatus: DS.attr('string'),
  ipaddress: DS.attr('string'),
  cpu: DS.attr('string'),
  memory: DS.attr('string'),
  diskSize: DS.attr('string'),
  javaVersion: DS.attr('string'),
  applicationType: DS.attr('string'),
  applicatonVersion: DS.attr('string'),
  thresholds: DS.hasMany('Threshold')
});
