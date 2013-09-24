
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for emailType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="emailType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACCOUNT_APPROVAL"/>
 *     &lt;enumeration value="ACCOUNT_ACTIVATION"/>
 *     &lt;enumeration value="INVITE_USER"/>
 *     &lt;enumeration value="REVOKE_USER"/>
 *     &lt;enumeration value="ACCOUNT_CANCELLATION"/>
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="AUTO_TOP_UP_SUCCESS"/>
 *     &lt;enumeration value="AUTO_TOP_UP_FAIL"/>
 *     &lt;enumeration value="PASSWORD_RESET_LINK"/>
 *     &lt;enumeration value="NEW_PASSWORD_DETAILS"/>
 *     &lt;enumeration value="ZERO_BALANCE"/>
 *     &lt;enumeration value="LOW_BALANCE"/>
 *     &lt;enumeration value="GENERAL_EMAIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "emailType")
@XmlEnum
public enum EmailType {

    ACCOUNT_APPROVAL,
    ACCOUNT_ACTIVATION,
    INVITE_USER,
    REVOKE_USER,
    ACCOUNT_CANCELLATION,
    INVOICE,
    AUTO_TOP_UP_SUCCESS,
    AUTO_TOP_UP_FAIL,
    PASSWORD_RESET_LINK,
    NEW_PASSWORD_DETAILS,
    ZERO_BALANCE,
    LOW_BALANCE,
    GENERAL_EMAIL;

    public String value() {
        return name();
    }

    public static EmailType fromValue(String v) {
        return valueOf(v);
    }

}
