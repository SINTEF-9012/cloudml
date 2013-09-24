
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for attachDisk complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attachDisk">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serverUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diskUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="index" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "attachDisk", propOrder = {
    "serverUUID",
    "diskUUID",
    "index",
    "when"
})
public class AttachDisk {

    protected String serverUUID;
    protected String diskUUID;
    protected int index;
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
     * Gets the value of the diskUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiskUUID() {
        return diskUUID;
    }

    /**
     * Sets the value of the diskUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiskUUID(String value) {
        this.diskUUID = value;
    }

    /**
     * Gets the value of the index property.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
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
