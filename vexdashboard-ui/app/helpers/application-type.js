import Ember from 'ember';

export function applicationType(params) {
       if (!Ember.isBlank(params[0])) {
               if (params[0] === "COREVEX") {
                       return "CORE VEX";
               } else if (params[1] === "DIRECTOR") {
                       return "VEX DIRECTOR";
               } else if (params[2] === "FRONTEND") {
                       return "VEX FRONTEND";
               }
       }
 }

export default Ember.HTMLBars.makeBoundHelper(applicationType);