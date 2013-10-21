
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for networking.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="networking">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VLAN"/>
 *     &lt;enumeration value="PVIP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "networking")
@XmlEnum
public enum Networking {

    VLAN,
    PVIP;

    public String value() {
        return name();
    }

    public static Networking fromValue(String v) {
        return valueOf(v);
    }

}
