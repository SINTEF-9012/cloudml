Facade
======

The *CloudML facade* is the API of CloudML: it provides a common way to programmatically trigger the key feature provided by CloudML. Among those key features are:

 - Opening CloudML in different formats (e.g., XMI, JSON, DOT)
 - Modifying a CloudML models in memory
 - Saving changes made on CloudML models
 - Deploying a CloudML model
 
From a technical point of view, the facade follows the Command Pattern [1]: each feature can be triggered by sending the related command to the facade. Command can be executed either in a synchronous or asynchronous way. Synchronous command will block the client execution until completion, whereas asynchronous commands will let the user continue its own activity while the command in running.

Below is the Java interface of the Facade (from CloudML v2.0):

```
public interface CloudML extends CommandHandler{

    public void fireAndForget(CloudMlCommand command);

    public void fireAndWait(CloudMlCommand command);

    public void register(EventHandler handler);

    public void terminate();
    
    public Deployment getDeploymentModel();
    
}
```

## Available Commands ##

The available commands are available in the Java package `org.cloudml.facade.commands`. Each command is reified as a separate Java class. The available commands are summarized in the table below:

 Command Name              | Description                                    | Implemented? | Java Class (in `org.cloudml.facade.commands`) 
:--------------------------|:-----------------------------------------------|:----|:-----------------------
 deploy                    | deploy the model currently in memory   | Yes |Deploy
 destroy                   | terminate the instance whose ID is given | Yes | Destroy
 detach                    | remove an existing communication between two components |  | Detach
 install                   | install a given component type on the selected platform | | Install
 instantiate               | create a new instance of the selected component type | | Instantiate
 list component instances  | list the component instances available in the current model | Yes | ListComponentInstance
 list component            | list the compoment types available in the current model | Yes | ListComponent
 load credentials          | load the credentials needed to access CloudProviders | |   LoadCredentials
 load deployment           | load a CloudML deployment model | Yes | LoadDeployment
 snaphot                   | create a visual snapshot of the deployment model | Yes | Snapshot
 start component           | Start a given component instance               | | StartComponent
 stop component            | Stop a given component  instance               | | StopComponent
 store credentials         | Store the credentials in use on disk           | | Store credentials
 store deployment          | Store the current deployment model on disk     | Yes | StoreDeployment
 uninstall                 | Uninstall a component instance from the platform currently supporting it | No | Uninstall
 upload                    | Upload a file on one of the VM described in the model | yes | Upload
 view component            | Show the details of a given component type | yes | ViewComponent
 view component instance   | Show the details of a given component instance | Yes | ViewComponentInstance
 
## Events ##

The user of this API can also register to events emitted while commands are executed. The table below summarizes the available events. Registered user will be notified when such events occurs

Event Name   | Description | Type | Implemented? | Java Class (in `org.cloudml.facade.events`)
:------------|:------------|:-----|:-------------|:------------
Message | A single notification that something has happened. This is used to convey, information, warnings or errors| MESSAGE | Yes | Message
component list | Contain the list of requested component types |  DATA | Yes | ComponentList
component data | Contain the details of a selected component type | DATA | Yes | ComponentData
component instance list | Contain the list of requested component instances | DATA | Yes | ComponentInstanceList
component instance data | Contain the details of a specific component instance | DATA | Yes | ComponentInstanceData


## References ##

1. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). Design patterns: elements of reusable object-oriented software. Pearson Education.
