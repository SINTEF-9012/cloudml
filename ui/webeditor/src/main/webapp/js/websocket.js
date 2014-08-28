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
        if(socket != null){
            if(socket.readyState != 1){
                socket = new WebSocket(host);
            }else{
                if(socket.url.indexOf (host) != -1){
                    alertMessage("success","Already connected to this CloudML server", 3000);
                }else{
                    // we are using a different input URL
                    socket = new WebSocket(host);
                }
            }
            
        }else{
            socket = new WebSocket(host);
        }
        socket.onopen = function(){  
            alertMessage("success","Connected to CloudML server",3000); 
            ready=true;
        }  

        socket.onmessage = function(msg){ 
            if(msg.data.indexOf("GetSnapshot") >= 0){
                var array=msg.data.split("###");

                /* *********************
                TODO refactor this dirty hack - checks if a function loadDeploymentModel is visible 
                 (called from demo.js in case we are running index.html with the low-level graph editor);
                 if it is not - calls the getData function in the graphview.js (also checks if the 'array' that contains the message is valid...sorta....)
                * *********************/

                if(typeof loadDeploymentModel  != 'undefined'){
                    loadDeploymentModel(array[2]);
                    alertMessage("success","Deployment Model loaded",3000); 
                }
                
                if(typeof getData  != 'undefined'){
                    if(array instanceof Array)
                        getData(array[2]);
                }
            }
            if(msg.data.indexOf("!update") >= 0){
                alertMessage("success","New update!",3000); 
                increaseNotificationNumber();
                addNotification(msg.data);
                try{
                    var json=jsyaml.load(msg.data, { schema : jsyamlSchema});
                }catch(error){
                    console.log(error);
                }
                if(typeof updateProperty  != 'undefined'){
                    updateProperty(json.parent,json.property,json.newValue);
                }
                if(typeof graphViewUpdateJSON  != 'undefined'){
                    graphViewUpdateJSON(json.parent,json.property,json.newValue);
                }
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
                
                /* *********************
                TODO refactor this dirty hack - checks if a function loadDeploymentModel is visible 
                 (called from demo.js in case we are running index.html with the low-level graph editor);
                 if it is not - calls the getData function in the graphview.js (also checks if the 'array' that contains the message is valid...sorta....)
                * *********************/
                if(typeof addInModel  != 'undefined'){
                    addInModel(tempObject);
                    alertMessage("success","Deployment Model loaded",3000); 
                }else{// addInModel not visible
                }
                // simply send a message to retrieve the entire model which will update the graph view
                setTimeout(function(){send("!getSnapshot {path : /}");}, 2000);
                
            }
            if(msg.data.indexOf("!removed") >= 0){
                alertMessage("success","New update! "+msg.data,3000); 
                increaseNotificationNumber();
                addNotification(msg.data);
//                var json=jsyaml.load(msg.data);
                if(typeof removeInModel != 'undefined')
                    removeInModel(json.removedValue);
            }

            if(typeof loadDeploymentModel != 'undefined'){
                loadDeploymentModel(JSON.stringify(deploymentModel));
            }
        }  

        socket.onclose = function(){
            ready=false;
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
        }
    } catch(exception){  
        alertMessage("error",'Unable to send message: '+exception,10000);  
    }  
}
