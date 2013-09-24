
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for openVNCConnection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="openVNCConnection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serverUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="handlerType" type="{http://extility.flexiant.net}vncHandler" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "openVNCConnection", propOrder = {
    "serverUUID",
    "handlerType"
})
public class OpenVNCConnection {

    protected String serverUUID;
    protected VncHandler handlerType;

    /**
     * Gets the value of the serverUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerUUID() {
        return serverUUID;
    }

    /**
     * Sets the value of the serverUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerUUID(String value) {
        this.serverUUID = value;
    }

    /**
     * Gets the value of the handlerType property.
     * 
     * @return
     *     possible object is
     *     {@link VncHandler }
     *     
     */
    public VncHandler getHandlerType() {
        return handlerType;
    }

    /**
     * Sets the value of the handlerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link VncHandler }
     *     
     */
    public void setHandlerType(VncHandler value) {
        this.handlerType = value;
    }

}
