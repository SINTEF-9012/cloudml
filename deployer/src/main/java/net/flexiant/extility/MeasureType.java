
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for measureType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="measureType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="KB"/>
 *     &lt;enumeration value="MB"/>
 *     &lt;enumeration value="GB"/>
 *     &lt;enumeration value="NUMERIC"/>
 *     &lt;enumeration value="STRING"/>
 *     &lt;enumeration value="RESOURCE_UUID"/>
 *     &lt;enumeration value="UNIT"/>
 *     &lt;enumeration value="CURRENCY"/>
 *     &lt;enumeration value="TB"/>
 *     &lt;enumeration value="B"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "measureType")
@XmlEnum
public enum MeasureType {

    KB,
    MB,
    GB,
    NUMERIC,
    STRING,
    RESOURCE_UUID,
    UNIT,
    CURRENCY,
    TB,
    B;

    public String value() {
        return name();
    }

    public static MeasureType fromValue(String v) {
        return valueOf(v);
    }

}
