
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DELETED"/>
 *     &lt;enumeration value="DISABLED"/>
 *     &lt;enumeration value="CLOSED"/>
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="ADMIN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "status")
@XmlEnum
public enum Status {

    DELETED,
    DISABLED,
    CLOSED,
    ACTIVE,
    ADMIN;

    public String value() {
        return name();
    }

    public static Status fromValue(String v) {
        return valueOf(v);
    }

}
