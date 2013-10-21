
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for validatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="validatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ENUM"/>
 *     &lt;enumeration value="REGEX"/>
 *     &lt;enumeration value="NUMERIC_INT"/>
 *     &lt;enumeration value="NUMERIC_DOUBLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "validatorType")
@XmlEnum
public enum ValidatorType {

    ENUM,
    REGEX,
    NUMERIC_INT,
    NUMERIC_DOUBLE;

    public String value() {
        return name();
    }

    public static ValidatorType fromValue(String v) {
        return valueOf(v);
    }

}
