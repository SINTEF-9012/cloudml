
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for billingEntityVAR.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="billingEntityVAR">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FROM_ADDRESS"/>
 *     &lt;enumeration value="CC_ADDRESS"/>
 *     &lt;enumeration value="BCC_ADDRESS"/>
 *     &lt;enumeration value="REPLY_TO"/>
 *     &lt;enumeration value="COMPANY_NAME"/>
 *     &lt;enumeration value="CONTROL_PANEL_URL"/>
 *     &lt;enumeration value="EMAIL_FOOTER"/>
 *     &lt;enumeration value="SUPPORT_URL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "billingEntityVAR")
@XmlEnum
public enum BillingEntityVAR {

    FROM_ADDRESS,
    CC_ADDRESS,
    BCC_ADDRESS,
    REPLY_TO,
    COMPANY_NAME,
    CONTROL_PANEL_URL,
    EMAIL_FOOTER,
    SUPPORT_URL;

    public String value() {
        return name();
    }

    public static BillingEntityVAR fromValue(String v) {
        return valueOf(v);
    }

}
