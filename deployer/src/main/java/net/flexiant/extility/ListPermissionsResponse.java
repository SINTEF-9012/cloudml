
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listPermissionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listPermissionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="permissions" type="{http://extility.flexiant.net}listResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listPermissionsResponse", propOrder = {
    "permissions"
})
public class ListPermissionsResponse {

    protected ListResult permissions;

    /**
     * Gets the value of the permissions property.
     * 
     * @return
     *     possible object is
     *     {@link ListResult }
     *     
     */
    public ListResult getPermissions() {
        return permissions;
    }

    /**
     * Sets the value of the permissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListResult }
     *     
     */
    public void setPermissions(ListResult value) {
        this.permissions = value;
    }

}
