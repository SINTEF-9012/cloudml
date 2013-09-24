
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for firewallConnectionState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="firewallConnectionState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALL"/>
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="EXISTING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "firewallConnectionState")
@XmlEnum
public enum FirewallConnectionState {

    ALL,
    NEW,
    EXISTING;

    public String value() {
        return name();
    }

    public static FirewallConnectionState fromValue(String v) {
        return valueOf(v);
    }

}
