import VexdashboardPagination from '../vexdashboard-pagination';
import Box from '../../models/box';

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
						      		ipaddress: elements[index].get('ipaddress'),
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
	    displayModal: function() {
	      this.send('showModal', 'settings-modal', 'xxxxxx');
	    }
	  }
});