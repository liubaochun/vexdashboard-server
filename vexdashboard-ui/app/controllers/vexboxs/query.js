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
	
	init: function() {
	    this._super.apply(this, arguments);

	    /*
	    * 2) The next step you need to do is to create your actual websocket. Calling socketFor
	    * will retrieve a cached websocket if one exists or in this case it
	    * will create a new one for us.
	    */
	    var socket = this.get('socketService').socketFor('ws://localhost:8080/vexdashboard/websocket/echoAnnotation');

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
	}
	
});