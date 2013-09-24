
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for billingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="billingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MASTERBILL"/>
 *     &lt;enumeration value="REGULARBILL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "billingType")
@XmlEnum
public enum BillingType {

    MASTERBILL,
    REGULARBILL;

    public String value() {
        return name();
    }

    public static BillingType fromValue(String v) {
        return valueOf(v);
    }

}
