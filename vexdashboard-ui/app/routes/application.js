import Ember from 'ember';

export default Ember.Route.extend({
  
  actions: {
    showModal: function(name, model) {
	  
		var cpuCharData = {};
		var cpuDatasets = [];		
		cpuCharData.labels = model.get('timestamps');
		cpuCharData.datasets = cpuDatasets;
	  	var cpuDataset = {
			fillColor: "rgba(210, 214, 222, 1)",
			strokeColor: "rgba(210, 214, 222, 1)",
			pointColor: "rgba(210, 214, 222, 1)",
			pointStrokeColor: "#c1c7d1",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)",
			data : model.get('cpuValues')	
		};
		cpuCharData.datasets.push(cpuDataset);
		model.set('cpuCharData', cpuCharData);
		
		var memCharData = {};
		var memDatasets = [];		
		memCharData.labels = model.get('timestamps');
		memCharData.datasets = memDatasets;
	  	var memDataset = {
			fillColor: "rgba(210, 214, 222, 1)",
			strokeColor: "rgba(210, 214, 222, 1)",
			pointColor: "rgba(210, 214, 222, 1)",
			pointStrokeColor: "#c1c7d1",
			pointHighlightFill: "#fff",
			pointHighlightStroke: "rgba(220,220,220,1)",
			data : model.get('memValues')	
		};
		memCharData.datasets.push(memDataset);
		model.set('memCharData', memCharData);
      this.render(name, {
        into: 'application',
        outlet: 'modal',
        model: model
      });
    },
    removeModal: function() {
      this.disconnectOutlet({
        outlet: 'modal',
        parentView: 'application'
      });
    },

    didTransition: function() {
      this.send('removeModal');
    }
  }

});
