
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for paymentProvider.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="paymentProvider">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INPAY"/>
 *     &lt;enumeration value="DATACASH"/>
 *     &lt;enumeration value="QUICKPAY"/>
 *     &lt;enumeration value="INVOICE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "paymentProvider")
@XmlEnum
public enum PaymentProvider {

    INPAY,
    DATACASH,
    QUICKPAY,
    INVOICE;

    public String value() {
        return name();
    }

    public static PaymentProvider fromValue(String v) {
        return valueOf(v);
    }

}
