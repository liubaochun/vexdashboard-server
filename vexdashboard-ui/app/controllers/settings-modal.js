import Ember from 'ember';

export default Ember.Controller.extend({
  	areaChartData : {
      labels: ["January", "February", "March", "April", "May", "June", "July"],
      datasets: [
        {
          fillColor: "rgba(210, 214, 222, 1)",
          strokeColor: "rgba(210, 214, 222, 1)",
          pointColor: "rgba(210, 214, 222, 1)",
          pointStrokeColor: "#c1c7d1",
          pointHighlightFill: "#fff",
          pointHighlightStroke: "rgba(220,220,220,1)",
          data: [65, 59, 80, 81, 56, 55, 40, 33, 62, 89, 20, 65, 22]
        }
      ]
    },	
  actions: {
    save: function() {
      // save to server
    }
  }
});
