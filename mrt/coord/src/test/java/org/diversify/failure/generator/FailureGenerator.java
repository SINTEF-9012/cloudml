package org.diversify.failure.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class FailureGenerator extends Thread
{
    public static int INTERVAL = 1000;
    
    public static int COMMAND_TIMEOUT = 0;
    
    Random random = new Random(System.currentTimeMillis());
    public Transformer transformer = null;
    
    
    public void run(){
        while(true){
            try {
                sleep(INTERVAL);
            } catch (InterruptedException ex) {
                Logger.getLogger(FailureGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            Map<String, String> states = getStates();
            System.out.printf("Current states: %s\n", states);
            List<String> elements = new ArrayList<String>(states.keySet());
            if(elements.size()==0)
                continue;
            List<String> toFail = new ArrayList<String>();
            toFail.add(elements.get(random.nextInt(elements.size())));
            transformer.issueFailCommand(toFail);
            
        }
    }
    
    public Map<String, String> getStates(){
        return transformer.checkState(COMMAND_TIMEOUT);
    }
   
    public static void main(String[] args) throws InterruptedException, IOException{
        
        Properties props = new Properties();
        props.load(new FileInputStream("conf.prop"));
        
        
        
        FailureGenerator generator = new FailureGenerator();
        MrtComm mrt = new MrtComm(props.getProperty("server.uri", "ws://127.0.0.1:9000"));
        COMMAND_TIMEOUT = Integer.parseInt(props.getProperty("cmd.timeout"));
        INTERVAL = Integer.parseInt(props.getProperty("interval"));
        
        
        Transformer transformer = new Transformer();
        //transformer.
        
        mrt.connectBlocking();
        
        mrt.transformer = transformer;
        transformer.mrt = mrt;
        generator.transformer = transformer;
        
        generator.start();
    }
}
