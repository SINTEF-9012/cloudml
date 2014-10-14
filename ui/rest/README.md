CloudML comes along with a RESTful server, which allows you to load, modify and enact CloudML models. The documentation below describes how to install, run, and interact with the server.

# Installing CloudML WebSocket Server
The CloudML WebSocket Server is a Java application distributed as an executable jar together with a lib folder that contains all required dependencies.

# Starting CloudML WebSocket Server
The server can be started by simply executing the following command. There is no deployment model loaded when the server start. By default the server listen on port 9000.
```shell
java -jar cloudml-rest.jar
```

You can change this port number using the following command.
```shell
java -jar cloudml-rest.jar portnumber
```

# RESTful commands

RESTful commands follow the same format of the WebSocket API. A most directway to launch these commands is to send an HTTP POST request to the server, e.g., ```http://localhost:9002/```, and the content of this POST is simply a plain text of the command, such as:

```
!commit
  modifications:   
  - !createAndAdd
     parent : /
     property : componentInstances
     type : VMInstance
     initializer : 
       - { type: String, value: newVMInstance }
       - { type: VM, value: !xpath '/components[name="SL"]' }
     keyValues : 
       name : newVMInstance
```

#Shotcut commands

##Query

Queries come with a simpler way by using HTTP GET, such as

```
http://localhost:9002/snapshot?path=/componentInstances
```

which queries out all the component instances. 

If no path parameter is set, it returns the whole model in Json:

```
http://localhost:9002/snapshot
```

##Modification

A ```commit``` command can be launched in a shorter way by a HTTP POST to ```http://localhost:9002/commit```

The content is the modifications, such as

```
!createAndAdd
parent : /
property : providers
type : Provider
keyValues :
  name : provider2
  credentials : !FileCredential 'c:/temp/cloudbees.credential'
```