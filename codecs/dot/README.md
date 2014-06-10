DOT Codec
================
This codec can be used to generate DOT file that can be used to visualize a deployment model as a graph.

The dot codec can be used programmatically as follows:
```java
// A sample deployment model
final Deployment example = SshClientServer.getOneClientConnectedToOneServer().build();
        
final FileOutputStream output = new FileOutputStream(OUTPUT_FILE);
// Create an instance of a DotCodec
DotCodec aDotCodec = new DotCodec();
// Generate the DOT code from the deployment model
aDotCodec.save(example, output);
output.close();

File file = new File(OUTPUT_FILE);>
```
