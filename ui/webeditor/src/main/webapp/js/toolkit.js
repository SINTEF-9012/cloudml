var notifications=new array();

function increaseNotificationNumber(){
	var span=$("#notificationCounter");
	var counter=parseInt(span.text());
	$("#notificationCounter").html(counter+"");
}

function addNotification(data){
	notifications.push(data);
	$("#notificationMenu").append("<li>"+data+"</li>");
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