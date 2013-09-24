
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteUserFromGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteUserFromGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteUserFromGroup", propOrder = {
    "userUUID",
    "groupUUID"
})
public class DeleteUserFromGroup {

    protected String userUUID;
    protected String groupUUID;

    /**
     * Gets the value of the userUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserUUID() {
        return userUUID;
    }

    /**
     * Sets the value of the userUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserUUID(String value) {
        this.userUUID = value;
    }

    /**
     * Gets the value of the groupUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupUUID() {
        return groupUUID;
    }

    /**
     * Sets the value of the groupUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupUUID(String value) {
        this.groupUUID = value;
    }

}
