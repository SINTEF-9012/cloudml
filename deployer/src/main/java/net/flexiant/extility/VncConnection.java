
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vncConnection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vncConnection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serverName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vncHandler" type="{http://extility.flexiant.net}vncHandler" minOccurs="0"/>
 *         &lt;element name="vncPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vncToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webSocketPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vncConnection", propOrder = {
    "serverName",
    "vncHandler",
    "vncPassword",
    "vncToken",
    "webSocketPath"
})
public class VncConnection {

    protected String serverName;
    protected VncHandler vncHandler;
    protected String vncPassword;
    protected String vncToken;
    protected String webSocketPath;

    /**
     * Gets the value of the serverName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the value of the serverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerName(String value) {
        this.serverName = value;
    }

    /**
     * Gets the value of the vncHandler property.
     * 
     * @return
     *     possible object is
     *     {@link VncHandler }
     *     
     */
    public VncHandler getVncHandler() {
        return vncHandler;
    }

    /**
     * Sets the value of the vncHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link VncHandler }
     *     
     */
    public void setVncHandler(VncHandler value) {
        this.vncHandler = value;
    }

    /**
     * Gets the value of the vncPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVncPassword() {
        return vncPassword;
    }

    /**
     * Sets the value of the vncPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVncPassword(String value) {
        this.vncPassword = value;
    }

    /**
     * Gets the value of the vncToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVncToken() {
        return vncToken;
    }

    /**
     * Sets the value of the vncToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVncToken(String value) {
        this.vncToken = value;
    }

    /**
     * Gets the value of the webSocketPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebSocketPath() {
        return webSocketPath;
    }

    /**
     * Sets the value of the webSocketPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebSocketPath(String value) {
        this.webSocketPath = value;
    }

}
