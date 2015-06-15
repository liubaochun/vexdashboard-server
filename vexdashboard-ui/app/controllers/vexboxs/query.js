import PaginationBaseController from '../pagination-base-controller';

export default PaginationBaseController.extend({
	queryParams:['o', 'pageIndex', 'pageSize', 'sortBy', 'desc', 'totalElements', "currentApplicationType", "ip"],
	applicationTypes : [{displayLabel: "Core Vex", type: "COREVEX"}, 
						{displayLabel: "Vex Director", type: "DIRECTOR"},
						{displayLabel: "Vex Frontend", type: "FRONTEND"}
						],
	currentApplicationType: null,
	ip: "",
	ipAddress: "",
	actions: {
		ipChanged : function() {
			this.resetPagination();
			this.set('ip', this.get('ipAddress'));
		}
	}
	
});