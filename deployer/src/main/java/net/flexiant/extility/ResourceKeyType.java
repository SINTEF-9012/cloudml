
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resourceKeyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="resourceKeyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SYSTEM_KEY"/>
 *     &lt;enumeration value="USER_KEY"/>
 *     &lt;enumeration value="BILLING_ENTITY_KEY"/>
 *     &lt;enumeration value="ADVERTISE_KEY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "resourceKeyType")
@XmlEnum
public enum ResourceKeyType {

    SYSTEM_KEY,
    USER_KEY,
    BILLING_ENTITY_KEY,
    ADVERTISE_KEY;

    public String value() {
        return name();
    }

    public static ResourceKeyType fromValue(String v) {
        return valueOf(v);
    }

}
