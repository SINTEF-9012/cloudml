CloudML comes along with a Websocket server, which allows you to load, modify and enact CloudML models. The documentation below describes how to install, run, and interact with the server.

# Installing CloudML WebSocket Server
The CloudML WebSocket Server is a Java application distributed as an executable jar together with a lib folder that contains all required dependencies.

# Starting CloudML WebSocket Server
The server can be started by simply executing the following command. There is no deployment model loaded when the server start. By default the server listen on port 9000.
```shell
java -jar cloudml-websocket.jar
```

You can change this port number using the following command.
```shell
java -jar cloudml-websocket.jar portnumber
```

# WebSocket commands

CloudML WebSocket supports four kinds of commands, i.e., _getSnapshot_ for querying, _commit_ for modification, _listen_ for registering listeners  about changes from other clients, and _extend_ for high level commands defined in CloudML facade.

We first list some examples below for a quick view of what the commands look like, and after that, we explain the syntax for each kind of commands.

- Query the whole deployment model

```yaml
!getSnapshot
  path : /
```

- Query the _status_ (an attribute) of a component instance named _sensapp-sl1_:

```yaml
!getSnapshot
  path : /componentInstances[name='sensapp-sl1']/status
```

- Change the value of the above attribute

```yaml
!commit 
  modifications:
    - !set
      parent : /componentInstances[name='sensapp-sl1']
      keyValues:
        status : 'RUNNING'
```
- Create and a new Provider element and add it into the deployment model's provider list

```yaml
!commit
  modifications:
    - !createAndAdd
      parent : /
      property : providers
      type : Provider
      keyValues :
        name : provider1
        credentials : ""
```

- Deploy the current component model

```yaml
!extended {
   name : Deploy
}
```

- Listen to any changes from any clients

```yaml
!listenToAny
```

- Create a snapshot of a VM:
```yaml
!extended { name: Snapshot, params: [xyz] }
```

- Create an image of a VM:
```yaml
!extended { name: Image, params: [xyz] }
```

- Scale out a VM
```yaml
!extended { name: ScaleOut, params: [xyz] }
```

The samples above are the simpliest but most frequently used ones. We will show the command syntax, which supports more complex, and therefore powerful, commands.

## YAML and XPath

You may have guessed it from the examples, the commands are formatted as YAML, and uses XPath to point to the relevant model elements.

An exhausted YAML specification can be find [here](http://www.yaml.org/spec/1.2/spec.html). But to be short, YAML just have three basic structures:

- **Scala** i.e., a single value.```'abc'```, ```xyz```, ```true```, ```123```, etc.
- **Sequence**, in the following two alternative structures:

``` [abc, def, 123]```

or 

```
- abc
- def
- 123
```

- **Map**, also in two basic structures:

``` { abc : 123, def : 456, m : true }```

or

```
abc : 123
def : 456
m : true
```

A map can be also interpreted as an object, whose type is indicated by a tag, such as ```!getSnapshot```. Now, the keys (```abc```, ```m```) correspond to the attribute names in the object, and the values (```123```, ```true```) are the values for the corresponding attributes.

So, the sample commands above can be also writen in a more compact way:


```yaml
!getSnapshot { path : /componentInstances[name='sensapp-sl1']/status }
!commit { modifications: [ !set { parent : /componentInstances[name='sensapp-sl1'], keyValues: { status : 'RUNNING' } ] }
```

It is hightly recommended that a whitespace is used every time before and after a structural symbol such as ```{```, ```-```, ```:```}, etc. Also, it is worth nothing that in the multi-line format, the indents are meaningful, just like in Python.

All the paths are written in XPath, which navigates the object graphs in Java. A specification can be found [here](http://commons.apache.org/proper/commons-jxpath/). 

##Query

**Syntax**:
```
instruction !getSnapshot{
	path : XPath
	codec? : String
	multimaps? : Map<String, XPath>
}
```

- ```path``` is the the element or attribute to query.
- ```codec``` indicate the return format. By default, it is the raw object, which means that you get a string from obj.toString(). If path is ```/```, codec can be ```json```.
- ```multimaps``` is used to get multiple attribute values together, each with a new name to show on the result. For example:

```
!getSnapshot 
  path : "/componentInstances[name='sensapp-sl1']" 
  multimaps : { status : status, cpu : properties/cpu, timestamp : properties/cpu-timestamp }
```

The result will be something like ```{status : RUNNING, cpu : 80, timestamp : 1234343}```

##Modification

**Syntax**

```
instruction !commit{
	modifications* : Modification
}
```

It is just a sequence of modifications, which are executed together like a transaction.

Below, I will explain the syntax of modifications.

```
modification !set{
	parent : XPath
	keyValues : Map<Property, Object>
}
```

```!set``` is used to update attribute values. each attribute and its value is specified as a key:value tuple, e.g., ```!set { parent : "/componentInstances[name='sensapp-sl1']", keyValues : { status : RUNNING, properties/cpu : 80 } } ```


```
modification !create{
	type : Type
	initializer : List<Parameter>
	keyValues : Map<Property, Object>
}
```

```!create``` creates a new element. ```type``` is a simply a class name. ```initializer``` is list of type-name tuples used to fill the parameters in the constructor. ```keyValues``` is the same as in ```!set```, and is used to set the attribute values after creation. Here is an example

```
modification !add{
	parent : XPath
	property : Property
	crossRef : XPath    //This is used to add existing objects as a cross-reference
	newValue : Object   //simple value
	index : Object      //optional for list, but mandatory for maps
}
```

Add a new value (```newValue```) or an existing model element (```crossRef```) to ```parent.property```.

The above two commands are usually used together, and we have a command for this:

```
modification !createAndAdd{
	parent : XPath
	property : Property
	type : Type
	initializer : List<Parameter>
	keyValues : Map<Property, Object>
	index : Object
}
```

Here is an example:

```
   !createAndAdd
     parent : /
     property : nodeInstances
     type : NodeInstance
     initializer : 
       - {type: String, value: ni1}
       - {type: Node, value: !xpath /nodeTypes/node1}
```

Finally, the following commands remove a value or object from a list.

```
modification !remove{
	parent : XPath
	property : Property
	index : Object       //-- choose one from index, value, or crossRef
	value : Object       //-- But for map, we always need an index
	crossRef : XPath 
}
```

##High level commands

```
instruction !extended{
	name : String
	params* : String
}
```

We have supported ```LoadDeployment```, ```Deploy```, and ```Start```, three Facade commands.

If the last params is too long (or too structural) for yaml, you can send a supplimentary command just after an extended command, started with a ```!additional```. The content following this tag is treated as plain text, not yaml.

For example: the following too commands together load a deployment model from the input json string:
```
!extended { name : LoadDeployment }
```

```
!additional
json-string:{
json-string:{
   "eClass" : "net.cloudml.core:CloudMLModel",
   "name" : "cloudbees-deployment",
   "providers" : [{
...
```
