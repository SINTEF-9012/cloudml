
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signup.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="signup">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MANUAL_SIGNUP"/>
 *     &lt;enumeration value="AUTO_SIGNUP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "signup")
@XmlEnum
public enum Signup {

    MANUAL_SIGNUP,
    AUTO_SIGNUP;

    public String value() {
        return name();
    }

    public static Signup fromValue(String v) {
        return valueOf(v);
    }

}
