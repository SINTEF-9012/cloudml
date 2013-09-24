
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for condition.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="condition">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="IS_EQUAL_TO"/>
 *     &lt;enumeration value="IS_NOT_EQUAL_TO"/>
 *     &lt;enumeration value="IS_GREATER_THAN"/>
 *     &lt;enumeration value="IS_LESS_THAN"/>
 *     &lt;enumeration value="IS_GREATER_THAN_OR_EQUAL_TO"/>
 *     &lt;enumeration value="IS_LESS_THAN_OR_EQUAL_TO"/>
 *     &lt;enumeration value="CONTAINS"/>
 *     &lt;enumeration value="NOT_CONTAINS"/>
 *     &lt;enumeration value="STARTS_WITH"/>
 *     &lt;enumeration value="NOT_STARTS_WITH"/>
 *     &lt;enumeration value="ENDS_WITH"/>
 *     &lt;enumeration value="NOT_ENDS_WITH"/>
 *     &lt;enumeration value="BETWEEN"/>
 *     &lt;enumeration value="NOT_BETWEEN"/>
 *     &lt;enumeration value="LATER_THAN"/>
 *     &lt;enumeration value="EARLIER_THAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "condition")
@XmlEnum
public enum Condition {

    IS_EQUAL_TO,
    IS_NOT_EQUAL_TO,
    IS_GREATER_THAN,
    IS_LESS_THAN,
    IS_GREATER_THAN_OR_EQUAL_TO,
    IS_LESS_THAN_OR_EQUAL_TO,
    CONTAINS,
    NOT_CONTAINS,
    STARTS_WITH,
    NOT_STARTS_WITH,
    ENDS_WITH,
    NOT_ENDS_WITH,
    BETWEEN,
    NOT_BETWEEN,
    LATER_THAN,
    EARLIER_THAN;

    public String value() {
        return name();
    }

    public static Condition fromValue(String v) {
        return valueOf(v);
    }

}
