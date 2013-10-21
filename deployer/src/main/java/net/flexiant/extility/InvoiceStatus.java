
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invoiceStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="invoiceStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="VOID"/>
 *     &lt;enumeration value="UNPAID"/>
 *     &lt;enumeration value="PAID"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "invoiceStatus")
@XmlEnum
public enum InvoiceStatus {

    PENDING,
    VOID,
    UNPAID,
    PAID;

    public String value() {
        return name();
    }

    public static InvoiceStatus fromValue(String v) {
        return valueOf(v);
    }

}
