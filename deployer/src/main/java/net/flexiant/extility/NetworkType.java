
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for networkType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="networkType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PUBLIC"/>
 *     &lt;enumeration value="PRIVATE"/>
 *     &lt;enumeration value="IP"/>
 *     &lt;enumeration value="INTERNETWORKING"/>
 *     &lt;enumeration value="SERVICE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "networkType")
@XmlEnum
public enum NetworkType {

    PUBLIC,
    PRIVATE,
    IP,
    INTERNETWORKING,
    SERVICE;

    public String value() {
        return name();
    }

    public static NetworkType fromValue(String v) {
        return valueOf(v);
    }

}
