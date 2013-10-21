
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for storageCapability.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="storageCapability">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLONE"/>
 *     &lt;enumeration value="SNAPSHOT"/>
 *     &lt;enumeration value="CHILDREN_PERSIST_ON_DELETE"/>
 *     &lt;enumeration value="CHILDREN_PERSIST_ON_REVERT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "storageCapability")
@XmlEnum
public enum StorageCapability {

    CLONE,
    SNAPSHOT,
    CHILDREN_PERSIST_ON_DELETE,
    CHILDREN_PERSIST_ON_REVERT;

    public String value() {
        return name();
    }

    public static StorageCapability fromValue(String v) {
        return valueOf(v);
    }

}
