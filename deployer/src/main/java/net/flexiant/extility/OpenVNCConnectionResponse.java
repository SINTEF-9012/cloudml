
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for openVNCConnectionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="openVNCConnectionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vncConnection" type="{http://extility.flexiant.net}vncConnection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "openVNCConnectionResponse", propOrder = {
    "vncConnection"
})
public class OpenVNCConnectionResponse {

    protected VncConnection vncConnection;

    /**
     * Gets the value of the vncConnection property.
     * 
     * @return
     *     possible object is
     *     {@link VncConnection }
     *     
     */
    public VncConnection getVncConnection() {
        return vncConnection;
    }

    /**
     * Sets the value of the vncConnection property.
     * 
     * @param value
     *     allowed object is
     *     {@link VncConnection }
     *     
     */
    public void setVncConnection(VncConnection value) {
        this.vncConnection = value;
    }

}
