
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for publish.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="publish">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ANY"/>
 *     &lt;enumeration value="BE_ADMIN"/>
 *     &lt;enumeration value="VALIDATED_CUSTOMER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "publish")
@XmlEnum
public enum Publish {

    ANY,
    BE_ADMIN,
    VALIDATED_CUSTOMER;

    public String value() {
        return name();
    }

    public static Publish fromValue(String v) {
        return valueOf(v);
    }

}
