
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resultOrder.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="resultOrder">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ASC"/>
 *     &lt;enumeration value="DESC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "resultOrder")
@XmlEnum
public enum ResultOrder {

    ASC,
    DESC;

    public String value() {
        return name();
    }

    public static ResultOrder fromValue(String v) {
        return valueOf(v);
    }

}
