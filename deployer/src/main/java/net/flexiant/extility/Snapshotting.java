
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for snapshotting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="snapshotting">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISK"/>
 *     &lt;enumeration value="SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "snapshotting")
@XmlEnum
public enum Snapshotting {

    DISK,
    SERVER;

    public String value() {
        return name();
    }

    public static Snapshotting fromValue(String v) {
        return valueOf(v);
    }

}
