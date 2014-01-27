/*
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
var socket;
var ready;
var tempObject;

function connect(host){  
	try{  
		socket = new WebSocket(host);  
   
		socket.onopen = function(){  
			 console.log('Socket Status: '+socket.readyState);  
			 ready=true;
		}  
  
		socket.onmessage = function(msg){ 
			
			if(msg.data.indexOf("GetSnapshot") >= 0){
				var array=msg.data.split("###");
				console.log(array[2]);
				loadDeploymentModel(array[2]);
				alertMessage("success","Deployment Model loaded",3000); 
			}
			if(msg.data.indexOf("!update") >= 0){
				alertMessage("success","New update!",3000); 
				increaseNotificationNumber();
				addNotification(msg.data);
				var json=jsyaml.load(msg.data);
				updateProperty(json.parent,json.property,json.newValue);
			}
			if(msg.data.indexOf("!created") >= 0){
				alertMessage("success","New update! "+msg.data,3000); 
				increaseNotificationNumber();
				addNotification(msg.data);
				tempObject=new Object();
			}
			if(msg.data.indexOf("!added") >= 0){
				alertMessage("success","New update! "+msg.data,3000); 
				increaseNotificationNumber();
				addNotification(msg.data);
				addInModel(tempObject);
			}
			if(msg.data.indexOf("!removed") >= 0){
				alertMessage("success","New update! "+msg.data,3000); 
				increaseNotificationNumber();
				addNotification(msg.data);
				var json=jsyaml.load(msg.data);
				removeInModel(json.removedValue);
			}
			loadDeploymentModel(JSON.stringify(deploymentModel));
		}  
  
		socket.onclose = function(){  
			 alertMessage("success",'Connection closed: '+socket.readyState,10000);  
		}
		
		socket.onerror = function(error){
			alertMessage("error",'Error message: '+socket.readyState,20000);  
		}
  
	} catch(exception){  
		alertMessage("error",'Socket Status: '+exception,20000);  
	}  
}

function send(text){  
	try{ 
		if(!ready){
			setTimeout(function(){send(text)},1000);
		}else{
			socket.send(text);
			console.log("Request sent!");
		}
	} catch(exception){  
		alertMessage("error",'Unable to send message: '+exception,10000);  
	}  
}

