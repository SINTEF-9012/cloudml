package org.cloudml.mrt.coord.cmd.gen;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@SuppressWarnings("all")
public class CloudMLCmds {
  public CloudMLCmds() {
    yamlConstructor = new Constructor();
    yamlConstructor.addTypeDescription(new TypeDescription(GetSnapshot.class, "!getSnapshot"));
    yamlConstructor.addTypeDescription(new TypeDescription(Add.class, "!add"));
    yamlConstructor.addTypeDescription(new TypeDescription(Create.class, "!create"));
    yamlConstructor.addTypeDescription(new TypeDescription(Commit.class, "!commit"));
    yamlConstructor.addTypeDescription(new TypeDescription(Set.class, "!set"));
    yaml = new Yaml(yamlConstructor);
    
  }
  
  public final static CloudMLCmds INSTANCE = new CloudMLCmds();
  
  private Constructor yamlConstructor;
  
  private Yaml yaml;
  
  private TypeDescription nouse;
  
  public Yaml getYaml() {
    return this.yaml;
  }
}
