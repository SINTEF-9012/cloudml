/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.diversify.failure.common;

import java.util.Scanner;

/**
 *
 * @author huis
 */
public class MrtUtil {
    public static String removeYamlTag(String s){
        StringBuilder newStr = new StringBuilder();
        Scanner sc = new Scanner(s);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            int tagStart = line.indexOf("!!");
            if(tagStart>=0)
                newStr.append(line.substring(0,tagStart)+"\n");
            else
                newStr.append(line+"\n");
        }
        return newStr.toString();
    }
}
