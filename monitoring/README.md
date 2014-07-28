This package contains all the components related to the monitoring activity
# Status
These components need that a configuration file named monitoring.properties is loaded in the root folder of Cloudml

Here an example of this file:

activated=true
frequency=60

According to what is specified in the file the components will start to monitor the status of the machines and, in case of changes, will update the model.

Here more in detail the way in which it work:
-during the deployment a StatusMonitor is instantiates
- every time a new connector is created is sent to the StatusMonitor 
-the component analyzes the connectors and uses them to manage the connection with the provider
-when the deployment is complete the component starts a thread that, according to the specified frequency, will check the status
-only in case of changes in the status (the component has an internal cache) the model will be updated using an MRT's coordinator
Implementation notes (READ CAREFULLY IN CASE YOU WANT TO CHANGE SOMETHING):
The StatusMonitor is offers all the methods to start and pause the monitoring of status and to flush the cache.
There are some modules (one for each provider) in the modules folder.
In case a new provider is added to Cloudml a new module should be created and used with the same logic used for the others.
Inside each module is performed the mapping between the names of statuses used by the provider and the ones used in Cloudml.
#Synchronization
The classes in this folder manage the communication from CloudML to the MODAClouds monitoring platform
These components need that a configuration file named monitoringPlatform.properties is loaded in the root folder of Cloudml
Here an example of this file:
use=true
address=http://192.168.11.6:8170
At the end of deployment and after each change in deployment model the model is sent to the MODAClouds Monitoring Platform so that the monitoring activity can be update accordingly.
The class MonitoringSynch contains three methods to send:
- the entire model (used during monitoring platform bootstrapping)
-the added external components (used at runtime)
-the removed internal components (use at runtime)
The class Filter is the one in which the actual mapping from CLoudML’s model to Monitoring Platform’s is performed.
Two extra classes ModelUpdates and ModelUpdatesExclusionStrategy are uses as support to create the Json.
The class Monitoring API offers a mapping of the APIs of the Monitoring Platform in order to facilitate the use from CloudML.
NOTE: This aspect is not exploited for the moment.
Implementation notes (READ CAREFULLY IN CASE YOU WANT TO CHANGE SOMETHING):
All these classes can be easily extracted from CloudML and putted in a separated project.
If there is the need to change the mapping between the two models the methods in Filter are easy to be updated.
If there is the need to send also the InternalComponents to the Monitoring Platform a new list should be added in ModelUpdates class importing the right class.

#MetricObserver
This subfolder contains a metric observer that can be used to retrieve monitoring data in CloudML.


