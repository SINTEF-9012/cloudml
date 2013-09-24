
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for referralStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="referralStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AWAITING_PURCHASE"/>
 *     &lt;enumeration value="LIVE"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="EXPIRED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "referralStatus")
@XmlEnum
public enum ReferralStatus {

    AWAITING_PURCHASE,
    LIVE,
    COMPLETED,
    EXPIRED;

    public String value() {
        return name();
    }

    public static ReferralStatus fromValue(String v) {
        return valueOf(v);
    }

}
