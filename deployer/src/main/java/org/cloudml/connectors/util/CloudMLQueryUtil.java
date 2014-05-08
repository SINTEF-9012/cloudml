/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cloudml.connectors.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.jxpath.JXPathContext;

/**
 *
 * @author Hui Song
 */
public class CloudMLQueryUtil {
    
    private static Pattern selfPattern = Pattern.compile("@self\\{[^\\{\\}]*\\}");
    private static Pattern instPattern = Pattern.compile("@instance\\{[^\\{\\}]*\\}");
    
    public static Object cloudmlQuery(String jxpath, Object context){
        JXPathContext jxpathcontext = JXPathContext.newContext(context); 
        return jxpathcontext.getValue(jxpath);
    }
    
    public static String cloudmlStringRecover(String string, Object self, Object instance){
        while(true){
            Matcher matcher = selfPattern.matcher(string);
            if(matcher.find()){
                String rawQuery = matcher.group();
                String query = rawQuery.substring(6, rawQuery.length()-1);
                
                string = string.replaceAll(
                        Pattern.quote(rawQuery), 
                        cloudmlQuery(query, self).toString()
                );
                continue;
            }
            
            matcher = instPattern.matcher(string);
            if(matcher.find()){
                String rawQuery = matcher.group();
                String query = rawQuery.substring(10, rawQuery.length()-1);
                
                string = string.replaceAll(
                        Pattern.quote(rawQuery), 
                        cloudmlQuery(query, instance).toString()
                );
                continue;
            }
            break;
        }
        return string;
    }
    
    
    
    public static void main(String[] args){
        Matcher matcher = selfPattern.matcher("@self{properties/entry_spring}::@self{a}//bean[@id=\"dataSource\"]/property[@name=\"url\"]/@value");
        while(matcher.find()) {
            System.out.println(matcher.group());
            System.out.println("--");
        }
        
        System.out.println("12345".substring(2,4));
        
        System.out.println(cloudmlStringRecover("@self{properties/entry_spring}::@instance{a}//bean[@id=\"dataSource\"]/property[@name=\"url\"]/@value",null,null));
    }
}
