import PaginationBaseController from '../pagination-base-controller';
import Box from '../../models/box';

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
	fetchFromBackend : false,
	
	init: function() {
	    this._super.apply(this, arguments);
	  },
	myOpenHandler: function(event) {
	    console.log('On open event has been called: ' + event);
	},

	myMessageHandler: function(event) {
		console.log(event.data);
		Ember.run.once(this, function() {
			var response = JSON.parse(event.data);
			var status = response.status;
			var boxes = [];
			var boxNumber = status.length;
			for (var i = 0; i < status.length; i++) {
				var ipAddress = status[i].ipaddress;
				var level = status[i].level;
				var notificationMsg = status[i].notificationMsg;
				var metrics = status[i].metrics;
				var cpuValue = "";
				var memoryValue = "";
				for (var j = 0; j < metrics.length; j++) {
					if (metrics[j].type === "CPU") {
						cpuValue = metrics[j].value;
					} else if (metrics[j].type == "MEMORY") {
						memoryValue = metrics[j].value;
					}
				}
				var box = Box.create({
				      		ipaddress: ipAddress,
							level : level,
							notificationMsg : notificationMsg,
							cpuValue : cpuValue,
							memoryValue : memoryValue
						});
				boxes.pushObject(box);				
			}
			
			var rows = this.get('rows');
			var columns = this.get('columns');
			var newTable = [];
			var oldTable = this.get('table');
			
			for (var m = 0; m < rows; m++) {
				var newRow = [];
				for (var n = 0; n < columns; n++) {
					if (oldTable[m][n]) {
						var oldBox = oldTable[m][n];
						var newBox = Box.create({
							id : oldBox.get('id'),
							ipaddress : oldBox.get('ipaddress'),
							cpu : oldBox.get('cpu'),
							memory : oldBox.get('memory'),
							diskSize : oldBox.get('diskSize'),
							javaVersion : oldBox.get('javaVersion'),
							applicationType : oldBox.get('applicationType'),
							applicatonVersion : oldBox.get('applicatonVersion'), 
							displayColor : oldBox.get('displayColor'),
							isRed : true,
							isYellow : false,
							isGreen : false
						});
						for (var k = 0; k < boxes.length; k++) {
							if (newBox.get('ipaddress') === boxes[k].get('ipaddress')) {								
								newBox.set('level', boxes[k].get('level').toLowerCase());
								if (boxes[k].get('level') === 'RED') {
									newBox.set('isRed', true);
									newBox.set('isGreen', false);
									newBox.set('isYellow', false);
								} else if(boxes[k].get('level') === 'GREEN') {
									newBox.set('isGreen', true);
									newBox.set('isRed', false);
									newBox.set('isYellow', false);
								} else if(boxes[k].get('level') === 'YELLOW') {
									newBox.set('isYellow', true);
									newBox.set('isGreen', false);
									newBox.set('isRed', false);
								}
								newBox.set('notificationMsg', boxes[k].get('notificationMsg'));
								newBox.set('cpuValue', boxes[k].get('cpuValue'));
								newBox.set('memoryValue', boxes[k].get('memoryValue'));								
							}
						}
					}
					newRow.pushObject(newBox);								
				}
				newTable.pushObject(newRow);
			}
			this.set('table', newTable);
			console.log("xxxxx");
		});	
	},
	
	actions: {
		ipChanged : function() {
			this.resetPagination();
			this.set('ip', this.get('ipAddress'));
		}
	}, 
	
	tableChanged : function() {
		console.log("table changed.")
		var boxIps = [];
		if (this.get('rows')) {
			var rows = this.get('rows');
			if (rows > 1) {
				var table = this.get('table');
				for (var i = 0; i < table.length; i++) {
					var row = table[i];
					for (var j = 0; j < row.length; j++) {
						var cell = row[j];
						if (cell) {
							boxIps.pushObject(cell.get('ipaddress'));
						}		
					}
				}
			}
		}
		var ipaddresses = '{\"ipaddresses\": [';
		for (var i = 0; i < boxIps.length; i++) {
			ipaddresses += '\"';
			ipaddresses += boxIps[i];
			ipaddresses += '\"';
			if (i === boxIps.length - 1) {
				
			} else {
				ipaddresses += ',';
			}
		}
		ipaddresses = ipaddresses + ']}';
		var socket = this.get('socketService').socketFor('ws://localhost:8080/vexdashboard/websocket/status');
		socket.on('message', this.myMessageHandler, this);
		socket.on('open', this.myOpenHandler, this);
	    socket.on('close', function(event) {
	      // anonymous functions work as well
	    }, this);
		if (socket.readyState() === WebSocket.OPEN) {
			socket.send(ipaddresses);
			//socket.send('{\"ipaddresses\": [\"192.168.204.1\", \"192.168.204.101\"]}');
		} else {
			Ember.run.later(this, function() {
				socket.send(ipaddresses);
				//socket.send('{\"ipaddresses\": [\"192.168.204.1\", \"192.168.204.101\"]}');
			}, 1000);
		}
		console.log('1111111');
	}.observes('fetchFromBackend'), 
	

	
});