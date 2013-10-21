
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cloning.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cloning">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISK"/>
 *     &lt;enumeration value="SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cloning")
@XmlEnum
public enum Cloning {

    DISK,
    SERVER;

    public String value() {
        return name();
    }

    public static Cloning fromValue(String v) {
        return valueOf(v);
    }

}
