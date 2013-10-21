
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for capability.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="capability">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CREATE"/>
 *     &lt;enumeration value="DELETE"/>
 *     &lt;enumeration value="MODIFY"/>
 *     &lt;enumeration value="ATTACH_DETACH"/>
 *     &lt;enumeration value="START_STOP"/>
 *     &lt;enumeration value="PUBLISH"/>
 *     &lt;enumeration value="FETCH"/>
 *     &lt;enumeration value="CLONE"/>
 *     &lt;enumeration value="CHANGE_PERMISSIONS"/>
 *     &lt;enumeration value="ALL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "capability")
@XmlEnum
public enum Capability {

    CREATE,
    DELETE,
    MODIFY,
    ATTACH_DETACH,
    START_STOP,
    PUBLISH,
    FETCH,
    CLONE,
    CHANGE_PERMISSIONS,
    ALL;

    public String value() {
        return name();
    }

    public static Capability fromValue(String v) {
        return valueOf(v);
    }

}
