This package contains all the components realted to the monitoring activity
# Status Monitor
This component need that a configuration file named monitoring.properties is loaded in the root folder of Cloudml

Here an example of this file:

activated=true
frequency=60

According to what is specified in the file the components will start a monitoring activity on the machines and in case of changes in the status will update the model.

Here more in detail the way in which it work:
-during the deployment all connectors are sent to this component 
-the component analyze the connector and use it to manage the connection with the provider
-when the deployment is complete the component starts a thread that according to the specified frequency will check the status
