import Ember from 'ember';
import RouteHistory from '../controllers/route-history';

export default Ember.Mixin.create({
	/**
	     * sub routes can override this property to provide the routes that want to return the current page when
	     * they deactivates. For example, user is visiting the 2nd page of the posts page, and then click one of
	     * the post to edit, the page should return back to the 2nd page instead of the 1th page when user clicks
	     * save after editing the post, in this case, we can add the 'post.edit' to the contextualRoutes.
	     */
	    contextualRoutes:[],
	    popLastRoute:function(){
	        return this.controllerFor('route.history').popObject();
	    },
	    beforeModel:function(transition, queryParams){
	        var superResult = this._super(transition, queryParams);
	        //go to the first page if the last route is not one of the routes defined in contextualRoutes
	        if(this.controller){
	            var lastRoute = this.popLastRoute();
	            var targetRoute = this.router.router.activeTransition.targetName;
	            if(lastRoute && targetRoute && targetRoute.indexOf(lastRoute) === 0 && targetRoute.indexOf('.index') > 0 && lastRoute.indexOf('.index') < 0) {
					    lastRoute = lastRoute + ".index";
				}
	            

	            if(targetRoute !== lastRoute && this.get('contextualRoutes')){
	                var isContextualRoute = false;
	                this.get('contextualRoutes').some(function(item){
	                    if(lastRoute === item){ //This may not be strict
	                        isContextualRoute = true;
	                        return true;
	                    }
	                    return false;
	                });
	                if(!isContextualRoute){
	                    this.controller.resetPagination(); //reset the pageIndex to 0
	                    this.transitionTo(targetRoute); //re-transition to the same route with new pageIndex
	                }
	            }
	        }
	        return superResult;
	    },
	    afterModel:function(resolvedModel, transition, queryParams){
	        var superResult = this._super(resolvedModel, transition, queryParams);
	        //tracks and saves the route name of current route so later the target route can use
	        this.controllerFor('route.history').clear(); //we just need the latest one, clear any added before.
	        this.controllerFor('route.history').pushObject(this.get('routeName'));
	        return superResult;
	    }
});
