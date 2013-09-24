
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="jobStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUCCESSFUL"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="CANCELLED"/>
 *     &lt;enumeration value="SUSPENDED"/>
 *     &lt;enumeration value="NOT_STARTED"/>
 *     &lt;enumeration value="WAITING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "jobStatus")
@XmlEnum
public enum JobStatus {

    SUCCESSFUL,
    IN_PROGRESS,
    FAILED,
    CANCELLED,
    SUSPENDED,
    NOT_STARTED,
    WAITING;

    public String value() {
        return name();
    }

    public static JobStatus fromValue(String v) {
        return valueOf(v);
    }

}
