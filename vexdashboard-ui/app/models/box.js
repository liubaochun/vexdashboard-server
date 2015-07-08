import Ember from "ember";

var Box = Ember.Object.extend({
  	ipaddress : null,
	cpu : null,
	memory : null,
	diskSize : null,
	javaVersion : null,
	applicationType : null,
	applicatonVersion : null,
	level: null,
	notificationMsg: null,
	cpuValue: null, 
	memoryValue: null, 
	displayColor: "light-blue"
});

export default Box;
