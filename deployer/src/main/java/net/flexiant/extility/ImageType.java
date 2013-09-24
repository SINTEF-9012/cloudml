
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for imageType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="imageType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISK"/>
 *     &lt;enumeration value="SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "imageType")
@XmlEnum
public enum ImageType {

    DISK,
    SERVER;

    public String value() {
        return name();
    }

    public static ImageType fromValue(String v) {
        return valueOf(v);
    }

}
