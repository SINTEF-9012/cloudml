Codecs
==============

A set of codecs to load/save CloudML models from/to serialized forms (e.g. JSON, XMI).

Codecs implement this simple Java interface:

```java
  /**
  * @param content, the stream from which we can read the serialized version
  * of the model
  * @return the in-memory version of the model
  */
  public CloudMLElement load(InputStream content);

  /**
  * @param model, the in-memory version of the model
  * @param content, the stream where the serialized model will be pushed
  */
  public void save(CloudMLElement model, OutputStream content);
```

> Note that streams can easily be created from files

```java
  InputStream stream = new FileInputStream(input);
  CloudMLElement model = codec.load(stream);
  stream.close();
                
  File result = new File("my.json");
  OutputStream streamResult = new FileOutputStream(result);
  codec.save(model, streamResult);
```
