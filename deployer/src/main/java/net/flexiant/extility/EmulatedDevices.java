
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emulatedDevices.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="emulatedDevices">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALLOW_ENABLED"/>
 *     &lt;enumeration value="ALLOW_DISABLED"/>
 *     &lt;enumeration value="ALLOW_ANY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "emulatedDevices")
@XmlEnum
public enum EmulatedDevices {

    ALLOW_ENABLED,
    ALLOW_DISABLED,
    ALLOW_ANY;

    public String value() {
        return name();
    }

    public static EmulatedDevices fromValue(String v) {
        return valueOf(v);
    }

}
