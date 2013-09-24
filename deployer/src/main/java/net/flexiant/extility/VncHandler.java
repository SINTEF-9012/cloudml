
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vncHandler.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vncHandler">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RAW"/>
 *     &lt;enumeration value="GUACAMOLE"/>
 *     &lt;enumeration value="ANY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vncHandler")
@XmlEnum
public enum VncHandler {

    RAW,
    GUACAMOLE,
    ANY;

    public String value() {
        return name();
    }

    public static VncHandler fromValue(String v) {
        return valueOf(v);
    }

}
