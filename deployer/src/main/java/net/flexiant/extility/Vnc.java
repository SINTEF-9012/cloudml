
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vnc.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vnc">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RAW"/>
 *     &lt;enumeration value="GUACAMOLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vnc")
@XmlEnum
public enum Vnc {

    RAW,
    GUACAMOLE;

    public String value() {
        return name();
    }

    public static Vnc fromValue(String v) {
        return valueOf(v);
    }

}
