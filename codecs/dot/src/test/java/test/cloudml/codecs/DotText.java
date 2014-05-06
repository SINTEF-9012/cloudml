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
 */
package test.cloudml.codecs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper methods that verify dot snippets using regular expressions
 */
public class DotText {

    public static int countOccurences(String dotText, String label) {
        final Pattern pattern = Pattern.compile(label, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(dotText);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static int nodeCount(String dotText) {
        return countOccurences(dotText, "^\\s*node_\\d+\\s+\\[[^\\]]+\\];$");
    }

    public static int edgeCount(String dotText) {
        return countOccurences(dotText, "^\\s*node_\\d+\\s+->\\s+node_\\d+\\s+\\[[^\\]]+\\];$");
    }

    public static int digraphCount(String dotText) {
        return countOccurences(dotText, "digraph");
    }

}
