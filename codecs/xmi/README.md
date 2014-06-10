XMI Codec
==========

###Usage

This codec makes it possible to load/save CloudML models from/to XMI files.

It can be used programmatically directly as follows:
```java
Codec xmiCodec=new XmiCodec();
OutputStream streamResult=new java.io.FileOutputStream("sensappTEST.xmi");
xmiCodec.save(model,streamResult);
```

Or via the Facade:
```java
```

Or via the Shell using the following commands:
```java
load deployment from file.xmi
store deployment in file.xmi
```

