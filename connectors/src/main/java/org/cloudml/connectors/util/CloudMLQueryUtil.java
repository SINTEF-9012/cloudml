/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
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
