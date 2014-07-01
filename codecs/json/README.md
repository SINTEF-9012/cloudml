JSON Codec
==========

###Usage

This codec makes it possible to load/save CloudML models from/to JSON files.

It can be used programmatically directly as follows:
```java
Codec jsonCodec=new JsonCodec();
OutputStream streamResult=new java.io.FileOutputStream("sensappTEST.json");
jsonCodec.save(model,streamResult);
```

Or via the Facade:
```java
CloudML cml= Factory.getInstance().getCloudML();
cml.fireAndWait(new LoadDeployment("C:\\sensappAdmin-v2.json"));
cml.fireAndWait(new Deploy());
```

Or via the Shell using the following commands:
```java
load deployment from file.json
store deployment in file.json
```


###The JSON Syntax
Please for details about the concepts of the metamodel refer to the [wiki page](https://github.com/SINTEF-9012/cloudml/wiki).

Name of the deployment model and list of providers:
```json
{
	"eClass" : "net.cloudml.core:CloudMLModel",
	"name" : "cloudbees-deployment",
	"providers" : [{
			"eClass" : "net.cloudml.core:Provider",
			"name" : "CloudBees",
			"credentials" : "cloudbees.credential",
			"properties" : [{
					"eClass" : "net.cloudml.core:Property",
					"name" : "account",
					"value" : "mod4cloud"
				}
			]
		}
	],
```

The "credentials" attribute refers to a file (i.e., should be a path to the file) containing the credentials to access to the specified provider API. This content of this file should be as follows:
```
login=xxxx
passwd=xxxxx
```

The table below summarize for each provider supported by CloudML the value to be written in the name attribute as well as  the properties that should be associated to each provider.

| Provider | name | Property |
| -------- | ---- | -------- |
| OpenStack nova | "openstack-nova" | A property called endpoint describing where can be accessed the API|
| Amazon EC2| "aws-ec2" | None                                                                           |
| Flexiant FCO | "flexiant" |  A property called endpoint describing where can be accessed the API       |
| CloudBees(container and database) | "CloudBees" | A property called account  (cf. listing)           |
| Beanstalk | "beanstalk" |                                                                                       |
| Amazon RDS | "rds" |                                                                                       |
| Amazon SQS | "sqs" |                                                                                       |

For instance, in the case of Amazon AWS login corresponds to the your access key id whilst passwd corresponds the secret access key.

List of internal components
```json
"internalComponents" : [{
			"eClass" : "net.cloudml.core:InternalComponent",
			"name" : "granny-war",
			"properties" : [{
					"eClass" : "net.cloudml.core:Property",
					"name" : "warfile",
					"value" : "granny-common.war"
				}, {
					"eClass" : "net.cloudml.core:Property",
					"name" : "temp-warfile",
					"value" : "granny-common-temp.war"
				}
			],
			"requiredPorts" : [{
					"eClass" : "net.cloudml.core:RequiredPort",
					"name" : "dbr",
					"isLocal" : false,
					"portNumber" : "0",
					"isMandatory" : true,
					"component" : "internalComponents[granny-war]"
				}
			],
			"requiredExecutionPlatform" : {
				"eClass" : "net.cloudml.core:RequiredExecutionPlatform",
				"name" : "tomcat",
				"owner" : "internalComponents[granny-war]"
			}
		}
	],
```

List of internal components instances
```json
"internalComponentInstances" : [{
			"eClass" : "net.cloudml.core:InternalComponentInstance",
			"name" : "granny-war-i",
			"type" : "internalComponents[granny-war]",
			"requiredPortInstances" : [{
					"eClass" : "net.cloudml.core:RequiredPortInstance",
					"name" : "dbr-452191065",
					"type" : "internalComponents[granny-war]/requiredPorts[dbr]"
				}
			],
			"requiredExecutionPlatformInstance" : {
				"eClass" : "net.cloudml.core:RequiredExecutionPlatformInstance",
				"name" : "tomcat-592730653",
				"owner" : "internalComponentInstances[granny-war-i]",
				"type" : "internalComponents[granny-war]/requiredExecutionPlatform[tomcat]"
			}
		}
	],
```

List of VMs
```json
"vms" : [{
			"eClass" : "net.cloudml.core:VM",
			"name" : "ML",
			"minRam" : "1024",
			"maxRam" : "0",
			"minCores" : "1",
			"maxCores" : "0",
			"minStorage" : "10",
			"maxStorage" : "0",
			"os" : "ubuntu",
			"is64os" : true,
			"securityGroup" : "SensApp",
			"sshKey" : "cloudml",
			"groupName" : "sensapp",
			"privateKey" : "cloudml.pem",
			"provider" : "providers[openstack-nova]",
			"imageId" : "RegionOne/9e2877b8-799e-4c87-a9f7-48140b021ba4",
			"properties" : [{
					"eClass" : "net.cloudml.core:Property",
					"name" : "KeyPath",
					"value" : "./cloudml.pem"
				}
			],
			"providedExecutionPlatforms" : [{
					"eClass" : "net.cloudml.core:ProvidedExecutionPlatform",
					"name" : "m1Provided",
					"owner" : "vms[ML]"
				}
			]
		}
```
For now, in the case of Flexiant the attribute **groupName is equal to the VDC name**...
The securityCroup specified in the deployment model should already exist and be associated to your cloud account before you start the actual deployment.

		
List of VM instances
```json
"vmInstances" : [{
			"eClass" : "net.cloudml.core:VMInstance",
			"name" : "sensapp-ml1",
			"publicAddress" : "no address given",
			"type" : "vms[ML]",
			"providedExecutionPlatformInstances" : [{
					"eClass" : "net.cloudml.core:ProvidedExecutionPlatformInstance",
					"name" : "m1Provided-541856653",
					"owner" : "vmInstances[sensapp-ml1]",
					"type" : "vms[ML]/providedExecutionPlatforms[m1Provided]"
				}
			]
		}
```

List of external components
```json
"externalComponents" : [{
			"eClass" : "net.cloudml.core:ExternalComponent",
			"name" : "granny-cloudml",
			"provider" : "providers[CloudBees]",
			"providedExecutionPlatforms" : [{
					"eClass" : "net.cloudml.core:ProvidedExecutionPlatform",
					"name" : "tomcatProvided",
					"owner" : "externalComponents[granny-cloudml]"
				}
			]
		}
```

List of external component instances
```json
"externalComponentInstances" : [{
			"eClass" : "net.cloudml.core:ExternalComponentInstance",
			"name" : "granny-cloudml1",
			"type" : "externalComponents[granny-cloudml]",
			"providedExecutionPlatformInstances" : [{
					"eClass" : "net.cloudml.core:ProvidedExecutionPlatformInstance",
					"name" : "tomcatProvided-478693748",
					"owner" : "externalComponentInstances[granny-cloudml1]",
					"type" : "externalComponents[granny-cloudml]/providedExecutionPlatforms[tomcatProvided]"
				}
			]
		}
```

List of Relationships
```json
"relationships" : [{
			"eClass" : "net.cloudml.core:Relationship",
			"name" : "dbrel",
			"requiredPort" : "internalComponents[granny-war]/requiredPorts[dbr]",
			"providedPort" : "externalComponents[cbdb]/providedPorts[db]",
			]
		}
	],
```

List of Relationship instances
```json
"relationshipInstances" : [{
			"eClass" : "net.cloudml.core:RelationshipInstance",
			"name" : "dbreli",
			"type" : "relationships[dbrel]",
			"requiredPortInstance" : "internalComponentInstances[granny-war-i]/requiredPortInstances[dbr-452191065]",
			"providedPortInstance" : "externalComponentInstances[cbdb1]/providedPortInstances[db-803038999]"
		}
	],
```

List of Execution bindings
```json
"executesInstances" : [{
			"eClass" : "net.cloudml.core:ExecuteInstance",
			"name" : "runOn1983021251",
			"providedExecutionPlatformInstance" : "externalComponentInstances[granny-cloudml1]/providedExecutionPlatformInstances[tomcatProvided-478693748]",
			"requiredExecutionPlatformInstance" : "internalComponentInstances[granny-war-i]/requiredExecutionPlatformInstances[tomcat-592730653]"
		}
	]
```
