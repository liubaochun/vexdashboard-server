import VexdashboardPagination from '../vexdashboard-pagination';

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
				for (var j = 0; j < columns; j++) {
					var index = i * columns + j; 
					if (index < elements.length) {
						newRow.pushObject(elements[index]);
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
	}
});