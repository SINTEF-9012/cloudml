
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for changeServerStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="changeServerStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serverUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newStatus" type="{http://extility.flexiant.net}serverStatus" minOccurs="0"/>
 *         &lt;element name="safe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="runtimeMetadata" type="{http://extility.flexiant.net}resourceMetadata" minOccurs="0"/>
 *         &lt;element name="when" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "changeServerStatus", propOrder = {
    "serverUUID",
    "newStatus",
    "safe",
    "runtimeMetadata",
    "when"
})
public class ChangeServerStatus {

    protected String serverUUID;
    protected ServerStatus newStatus;
    protected boolean safe;
    protected ResourceMetadata runtimeMetadata;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

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
     * Gets the value of the newStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ServerStatus }
     *     
     */
    public ServerStatus getNewStatus() {
        return newStatus;
    }

    /**
     * Sets the value of the newStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerStatus }
     *     
     */
    public void setNewStatus(ServerStatus value) {
        this.newStatus = value;
    }

    /**
     * Gets the value of the safe property.
     * 
     */
    public boolean isSafe() {
        return safe;
    }

    /**
     * Sets the value of the safe property.
     * 
     */
    public void setSafe(boolean value) {
        this.safe = value;
    }

    /**
     * Gets the value of the runtimeMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceMetadata }
     *     
     */
    public ResourceMetadata getRuntimeMetadata() {
        return runtimeMetadata;
    }

    /**
     * Sets the value of the runtimeMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceMetadata }
     *     
     */
    public void setRuntimeMetadata(ResourceMetadata value) {
        this.runtimeMetadata = value;
    }

    /**
     * Gets the value of the when property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getWhen() {
        return when;
    }

    /**
     * Sets the value of the when property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setWhen(XMLGregorianCalendar value) {
        this.when = value;
    }

}
