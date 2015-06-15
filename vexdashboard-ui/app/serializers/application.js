import DS from 'ember-data';
import Ember from 'ember';

export default DS.RESTSerializer.extend({
		serialize: function(record, options){
			var json = {};
			if (options && options.includeId) {
				var id = Ember.get(record, 'id');
			    if (id) {
			    	json[Ember.get(this, 'primaryKey')] = id;
			    }
			}
			var _this = this;
			record.eachAttribute(function(key, attribute) {
				this.serializeAttribute(record, json, key, attribute);
			}, this);
			record.eachRelationship(function(key, relationship) {
				var key = relationship.key;
				var childrenRecord = Ember.get(record, key);
				key = this.keyForRelationship ? this.keyForRelationship(key, relationship.kind) : key;
				if(!Ember.isNone(childrenRecord)){
					if (relationship.kind === 'belongsTo') {
						json[key] = _this.serialize(childrenRecord, options);
					} else if (relationship.kind === 'hasMany') {
						var hasManyJson = [];
						for(var index = 0; index<childrenRecord.content.length; index++ ){
							var oneOfMany = childrenRecord.content[index];
							hasManyJson[index] = _this.serialize(oneOfMany, options);
						}
						//Do NOT save an empty array to the server
						if(hasManyJson.length > 0){
							json[key] = hasManyJson;
						}
					}
				}
			}, this);
						
		},

	    serializeIntoHash: function(hash, type, record, options){
	        this._super(hash, type, record, options);
	        var root = Ember.String.camelize(type.typeKey);
	        var rootObject = hash[root];
	        for (var key in rootObject){
	            hash[key] = rootObject[key];
	        }
	        delete hash[root];
	    },

	    serializeAttribute: function (record, json, key, attribute){
	        //Do not serialize attributes with null value
	        if(Ember.get(record, key)){
	            this._super(record, json, key, attribute);
	        }
	    },

	    /**
	     * This is called to extract the pagination data returned from MetaMore server.
	     * @param store
	     * @param type
	     * @param payload
	     */
	    extractMeta: function(store, type, payload) {
	        var paginationKeys = ['pageIndex','pageSize','totalElements','totalPages','isFirstPage','isLastPage','hasNextPage','hasPreviousPage', 'firstElement', 'lastElement'];
	        var isPaginationPayload = (payload !== undefined);
	        //check whether the payload is a pagination payload, which should contain all keys in paginationKeys
	        for(var i = 0; i<paginationKeys.length && isPaginationPayload; i++){
	            isPaginationPayload = (payload[paginationKeys[i]] != undefined);
	        }

	        if(isPaginationPayload){
	            if(!payload['meta'])
	                payload['meta'] = {};
	            //1. move the pagination data to metadata of the model
	            for(var i = 0; i<paginationKeys.length; i++){
	                payload['meta'][paginationKeys[i]] = payload[paginationKeys[i]];
	                delete payload[paginationKeys[i]];
	            }
	            //2. rename the key 'contents' to the name of the Model type
	            if(payload.contents !== undefined){
	                payload[type.typeKey] = payload['contents'];
	                delete payload['contents'];
	            }
	            //3. clean up existing pagination metadata because the Ember.merge will throw error if the key already exists
	            var type = store.modelFor(type);
	            var existingMeta = store.typeMapFor(type).metadata;
	            for(var i = 0; i<paginationKeys.length && isPaginationPayload; i++){
	                delete existingMeta[paginationKeys[i]];
	            }
	        }
	        this._super(store, type, payload);
	    },

	    extractSingle: function(store, primaryType, rawPayload, recordId) {
	        rawPayload = this.beforeExtraPayload(primaryType, rawPayload);
	        return this._super(store, primaryType, rawPayload, recordId);
	    },

	    extractArray: function(store, primaryType, rawPayload){
	        rawPayload = this.beforeExtraPayload(primaryType, rawPayload);
	        return this._super(store, primaryType, rawPayload);
	    },

	    beforeExtraPayload: function(type, hash){
	        if(!hash)
	            return hash;

	        var newPayload = {};
	        var typeKey = type.typeKey;
	        //We should use the existing payload structure if it has already been wrapped with the type.typeKey
	        if(hash[typeKey]){
	            newPayload[typeKey] = hash[typeKey];
	        }else{
	            newPayload[typeKey] = hash;
	        }
	        //delete hash;

	        if(Ember.isArray(newPayload[typeKey])){
	            for(var index = 0; index<newPayload[typeKey].length; index++ ){
	                //Add an id to the obj to avoid the issue 'You must include an `id` in a hash passed to `push`'
	                if(!newPayload[typeKey][index].id)
	                    newPayload[typeKey][index].id = index;
	                this.normalizePayloadObject(type, newPayload[typeKey][index], newPayload);
	            }
	        }else{
	            //Add an id to the obj to avoid the issue 'You must include an `id` in a hash passed to `push`'
	            if(!newPayload[typeKey].id){
	                newPayload[typeKey].id = 0;
				}
	            this.normalizePayloadObject(type, newPayload[typeKey], newPayload);
	        }
	        return newPayload;
	    },

	    normalizePayloadObject:function(type, hash, root){
	        var _this = this;
	        type.eachRelationship(function(key, relationship) {
	            if(_this.keyForRelationship){
	                key = _this.keyForRelationship(key, relationship);
	            }
	            var obj = hash[key];
	            if(!obj)
	                return;
	            var type = relationship.type.typeKey;
	            if(!root[type]){
	                root[type] = [];
	            }
	            if (relationship.kind === 'belongsTo') {
	                var newIndex = root[type].length;
	                if(!obj.id){ //If the object has no ID, set it's id to the current length of the array
	                    obj.id = newIndex;
	                }
	                root[type][newIndex] = obj;
	                hash[key] = obj.id;
	                _this.normalizePayloadObject(relationship.type, obj, root);
	            }else if (relationship.kind === 'hasMany') {
	                for(var i=0; i<obj.length; i++){
	                    var subObj = obj[i];
	                    var newIndex = root[type].length;
	                    if(!subObj.id){ //If the object has no ID, set it's id to the current length of the array
	                        subObj.id = newIndex;
	                    }
	                    root[type][newIndex] = subObj;
	                    obj[i] = subObj.id;
	                    _this.normalizePayloadObject(relationship.type, subObj, root);
	                }
	            }
	        });
	    },

	    rootForType: function(type) {
	        var typeString = type.toString();
	        Ember.assert('Your model must not be anonymous. It was ' + type, typeString.charAt(0) !== '(');
	        var parts = typeString.split('.');
	        var name = parts[parts.length - 1];
	        return name.toLowerCase();
	    }
});
