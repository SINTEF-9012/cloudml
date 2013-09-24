
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ipType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ipType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IPV4"/>
 *     &lt;enumeration value="IPV6"/>
 *     &lt;enumeration value="INVALID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ipType")
@XmlEnum
public enum IpType {

    @XmlEnumValue("IPV4")
    IPV_4("IPV4"),
    @XmlEnumValue("IPV6")
    IPV_6("IPV6"),
    INVALID("INVALID");
    private final String value;

    IpType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IpType fromValue(String v) {
        for (IpType c: IpType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
