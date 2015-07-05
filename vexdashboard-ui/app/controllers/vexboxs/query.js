import PaginationBaseController from '../pagination-base-controller';

export default PaginationBaseController.extend({
	socketService: Ember.inject.service('websockets'),
	
	queryParams:['o', 'pageIndex', 'pageSize', 'sortBy', 'desc', 'totalElements', "currentApplicationType", "ip"],
	applicationTypes : [{displayLabel: "Core Vex", type: "COREVEX"}, 
						{displayLabel: "Vex Director", type: "DIRECTOR"},
						{displayLabel: "Vex Frontend", type: "FRONTEND"}
						],
	currentApplicationType: null,
	ip: "",
	ipAddress: "",
	socket: null,
	
	init: function() {
	    this._super.apply(this, arguments);

	    /*
	    * 2) The next step you need to do is to create your actual websocket. Calling socketFor
	    * will retrieve a cached websocket if one exists or in this case it
	    * will create a new one for us.
	    */
	    var socket = this.get('socketService').socketFor('ws://localhost:8080/vexdashboard/websocket/echoAnnotation');
		console.log('222222');
	    /*
	    * 3) The final step is to define your event handlers. All event handlers
	    * are added via the `on` method and take 3 arguments: event name, callback
	    * function, and the context in which to invoke the callback. All 3 arguments
	    * are required.
	    */
	    socket.on('open', this.myOpenHandler, this);
	    socket.on('message', this.myMessageHandler, this);
	    socket.on('close', function(event) {
	      // anonymous functions work as well
	    }, this);
		console.log('333333');
	  },
	myOpenHandler: function(event) {
	    console.log('On open event has been called: ' + event);
	},

	myMessageHandler: function(event) {
		console.log('Message: ' + event.data);
	},
	
	actions: {
		ipChanged : function() {
			this.resetPagination();
			this.set('ip', this.get('ipAddress'));
		}
	}, 
	
	tableChanged : function() {
		console.log("table changed.")
		var boxIds = [];
		if (this.get('rows')) {
			var rows = this.get('rows');
			if (rows > 1) {
				var table = this.get('table');
				for (var i = 0; i < table.length; i++) {
					var row = table[i];
					for (var j = 0; j < row.length; j++) {
						var cell = row[j];
						boxIds.pushObject(cell.id)
					}
				}
			}
		}
		var socket = this.get('socketService').socketFor('ws://localhost:8080/vexdashboard/websocket/echoAnnotation');
		if (socket.readyState() === WebSocket.OPEN) {
			socket.send('table changed.');
		} else {
			Ember.run.later(this, function() {
				socket.send('table changed. mmmmmm');
			}, 500);
		}
		console.log('1111111');
	}.observes('table'), 
	

	
});