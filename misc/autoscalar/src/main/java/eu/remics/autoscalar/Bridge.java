package eu.remics.autoscalar;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.cloudml.core.DeploymentModel;

class Bridge {

    private static final Bridge bridge = new Bridge();

    public static void toXML(DeploymentModel model, String path) {
        try {
            String config = FileUtils.readFileToString(new File(bridge.getClass().getResource("/config.xml").toURI()));
            String component = FileUtils.readFileToString(new File(bridge.getClass().getResource("/component.xml").toURI()));
            String group = FileUtils.readFileToString(new File(bridge.getClass().getResource("/group.xml").toURI()));
            
            System.out.println(config);
            System.out.println(component);
            System.out.println(group);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Bridge.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String args[]) {
        toXML(null, null);
    }
}