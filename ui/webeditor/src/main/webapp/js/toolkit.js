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
var notifications = [];
var counter=0;

function increaseNotificationNumber(){
        counter++;
        $(".badge").text(counter+"");
}

function addNotification(data){
        notifications.push(data+"");
        $("#listNotifications").prepend("<li>"+data+"</li>");
}

function updateProgress (evt) {
    // evt is an ProgressEvent.
    if (evt.lengthComputable) {
      var percentLoaded = Math.round((evt.loaded / evt.total) * 100);
      // Increase the progress bar length.
      if (percentLoaded < 100) {
        progress.css("width",percentLoaded + '%');
      }
    }
  }
  

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}

String.prototype.uncapitalize = function() {
    return this.charAt(0).toLowerCase() + this.slice(1);
}

String.prototype.uncapitalizes = function(n) {
    var result="";
    for(i=0;i<n;i++){
        result+=this.charAt(i).toLowerCase();
    }
    return result + this.slice(n);
}

  //Alert Messages
function alertMessage(type,message,timeout) {
        alertDiv = $(document.createElement('div'));

        switch (type) {
                case "success":
                        alertDiv.attr("class","alert alert-success fade in")
                                        .html("<b>Success.</b> "+message);
                        break;
                case "error":
                        alertDiv.attr("class","alert alert-error fade in")
                                        .html("<b>Error.</b> "+message);
                        break;
                case "warning":
                        alertDiv.attr("class","alert fade in")
                                        .html("<b>Warning.</b> "+message);
                        break;
                default:
                        break;
        }

        alertDiv.append(
                        $(document.createElement('a'))
                                .attr("class","close")
                                .attr("data-dismiss","alert")
                                .html("&times;")
                );

        $('#alert-div').append(alertDiv);
        if(typeof timeout!='undefined')
                window.setTimeout(function() { $('#alert-div').find(':contains('+message+')').remove(); }, timeout);
}
