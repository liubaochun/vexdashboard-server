import Ember from "ember";

export default Ember.Object.extend({
	id: null,
  	ipaddress : null,
	cpu : null,
	memory : null,
	diskSize : null,
	javaVersion : null,
	applicationType : null,
	applicatonVersion : null,
	timestamps : null, 
	cpuValues : null, 
	memoryValues : null  
});
