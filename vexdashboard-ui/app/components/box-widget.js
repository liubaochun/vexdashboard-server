import Ember from 'ember';

export default Ember.Component.extend({
    isCollapsed: false,
    isClosed: false,

    closable: false,
    collapsable: true,

    title: null,
    colorSceme: null,
    buttons: false,

	vexbox: null,

    classNames: ['box'],
    classNameBindings: ['boxClass','backgroundClass','solid:box-solid'],

    /***************************
     **	External states
     ****************************/
    isLoading : false,

	init:function(){
		this._super();
		var vexbox = this.get('vexbox');
        if (vexbox) {
			if (vexbox.applicationType === 'COREVEX') {
				this.set('title', 'VEX CORE');
			} else if (vexbox.applicationType === 'DIRECTOR') {
				this.set('title', 'VEX DIRECTOR');
			} else if (vexbox.applicationType === 'FRONTEND') {
				this.set('title', 'VEX FRONTEND');
			}
		}
    },

    /***************************
     **	Computed states
     ****************************/
    boxClass: function(){
        var colorSceme = this.get('colorSceme');
        if(!colorSceme) { return '';}
        return 'box-'+colorSceme;
    }.property('colorSceme'),


    buttonClass: function(){
        var buttons = this.get('buttons'),
            colorSceme = this.get('colorSceme');
        if(!buttons) {return 'btn-box-tool';}
        if(!colorSceme) { colorSceme='default';}
        return 'btn-xs btn-'+colorSceme;
    }.property('buttons','colorSceme'),

    backgroundClass: function(){
        var background = this.get('background');
        if(!background) {return '';}
        return 'box-solid bg-'+background;
    }.property('background'),


    labelClass: function(){
        var colorSceme = this.get('colorSceme');
        if(!colorSceme) { return '';}
        return 'label-'+colorSceme;
    }.property('colorSceme'),


    /***************************
     **	Actions
     ****************************/

    actions: {
        toggleCollapse: function(){
		  	var vexbox = this.get('vexbox');
			this.sendAction('showModal', vexbox.get('id'));
            /*if(!this.get('collapsable')){return;}
            var box = this.$();
            this.toggleProperty('isCollapsed');
            var bf = box.find(".box-body, .box-footer");
            var isCollapsed = this.get('isCollapsed');
            if (isCollapsed){
                bf.slideUp(200, function () {

                });
            }
            else {
                bf.slideDown(200, function () {

                });
            }*/
        },
        close: function(){
            this.$().slideUp();
        }
    }
});
