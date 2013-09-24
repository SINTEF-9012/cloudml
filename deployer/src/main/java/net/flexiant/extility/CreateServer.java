
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for createServer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createServer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonServer" type="{http://extility.flexiant.net}server" minOccurs="0"/>
 *         &lt;element name="sshKeyUUIDList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "createServer", propOrder = {
    "skeletonServer",
    "sshKeyUUIDList",
    "when"
})
public class CreateServer {

    protected Server skeletonServer;
    protected List<String> sshKeyUUIDList;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

    /**
     * Gets the value of the skeletonServer property.
     * 
     * @return
     *     possible object is
     *     {@link Server }
     *     
     */
    public Server getSkeletonServer() {
        return skeletonServer;
    }

    /**
     * Sets the value of the skeletonServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Server }
     *     
     */
    public void setSkeletonServer(Server value) {
        this.skeletonServer = value;
    }

    /**
     * Gets the value of the sshKeyUUIDList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sshKeyUUIDList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSshKeyUUIDList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSshKeyUUIDList() {
        if (sshKeyUUIDList == null) {
            sshKeyUUIDList = new ArrayList<String>();
        }
        return this.sshKeyUUIDList;
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
