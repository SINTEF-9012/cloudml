
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for snapshotType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="snapshotType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISK"/>
 *     &lt;enumeration value="SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "snapshotType")
@XmlEnum
public enum SnapshotType {

    DISK,
    SERVER;

    public String value() {
        return name();
    }

    public static SnapshotType fromValue(String v) {
        return valueOf(v);
    }

}
