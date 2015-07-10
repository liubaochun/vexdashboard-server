import VexdashboardPagination from '../vexdashboard-pagination';
import Box from '../../models/box';
import Boxprofile from '../../models/boxprofile';

export default VexdashboardPagination.extend({
	elementsPerRow : '3',
	type: "vexbox",
	contextualRoutes:['vexboxs.view'],
	queryParams: {
        pageIndex: { refreshModel: true },
        sortBy: { refreshModel: true },
        desc: { refreshModel: true },
		o: {refreshModel: true},
		currentApplicationType: {refreshModel: true},
		ip: {refreshModel: true},
    },
	setupController:function(controller, model){
		var table =[];
		
		if (model) {
			var totalElements = model.get('meta').totalElements;
			var rows, columns;
			columns = 3;
			if (totalElements > 0) {
				rows = totalElements / columns; 
				rows = Math.ceil(rows);
			} else {
				rows = 0;
			}
			controller.set('rows', rows);
			controller.set('columns', columns);
			var elements = model.get('content');
			for (var i = 0; i < rows; i++) {
				var newRow = [];
				var cellColor = "light-blue";
				if ( i%2 === 1) {
					cellColor = "green";
				}
				for (var j = 0; j < columns; j++) {
					var index = i * columns + j; 
					if (index < elements.length) {
						var box = Box.create({
									id : elements[index].get('id'),
						      		ipaddress : elements[index].get('ipaddress'),
									cpu : elements[index].get('cpu'),
									memory : elements[index].get('memory'),
									diskSize : elements[index].get('diskSize'),
									javaVersion : elements[index].get('javaVersion'),
									applicationType : elements[index].get('applicationType'),
									applicatonVersion : elements[index].get('applicatonVersion'),
									displayColor: cellColor
						    		});
						newRow.pushObject(box);
					} else {
						newRow.pushObject(null);
					}
				}
				table.pushObject(newRow);
			}
			controller.set('table', table);
		} else {
			controller.set('rows', 0);
			controller.set('columns', 0);
			controller.set('table', null);
		}
		if (controller.get('fetchFromBackend') === true) {
			controller.set('fetchFromBackend', false);
		} else {
			controller.set('fetchFromBackend', true);
		}
	}, 
	
	actions: {
	    displayModal: function(id) {
			var boxProfile = Boxprofile.create({});
			var _this = this;
			if (id) {
				Ember.$.ajax({
					url : 'http://localhost:8080/vexdashboard/core/boxstatus/ip/' + id,
					type : 'GET',
					accepts: 'application/json',
					success : function(data) {	
						boxProfile.set('ipAddress', data.vexBox.ipaddress);
						boxProfile.set('cpu', data.vexBox.cpu);
						boxProfile.set('memory', data.vexBox.memory);
						boxProfile.set('diskSize', data.vexBox.diskSize);
						boxProfile.set('javaVersion', data.vexBox.javaVersion);
						boxProfile.set('applicationType', data.vexBox.applicationType);
						boxProfile.set('applicatonVersion', data.vexBox.applicatonVersion);
						var newTimestamps = [];
						for (var i = 0; i < data.timestamps.length; i++) {
							if (i % 40 === 0 || i === data.timestamps.length - 1) {
								var tmpDate = new Date(data.timestamps[i]);
								var tmpDateStr = tmpDate.getHours() + ":" + tmpDate.getMinutes() + ":" + tmpDate.getSeconds();
								newTimestamps.push(tmpDateStr);
							} else {
								newTimestamps.push('');
							}
						}
						boxProfile.set('timestamps', newTimestamps);
						boxProfile.set('cpuValues', data.cpuValues);
						boxProfile.set('memValues', data.memValues);
						_this.send('showModal', 'settings-modal', boxProfile);			
					}, 
					error : function() {
						console.log('FAILED TO GET BOX PROFILE!!');
					}
				});
			}
			
	    }
	  }
});