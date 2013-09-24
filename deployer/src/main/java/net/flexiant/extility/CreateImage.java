
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for createImage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createImage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonImage" type="{http://extility.flexiant.net}image" minOccurs="0"/>
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
@XmlType(name = "createImage", propOrder = {
    "skeletonImage",
    "when"
})
public class CreateImage {

    protected Image skeletonImage;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar when;

    /**
     * Gets the value of the skeletonImage property.
     * 
     * @return
     *     possible object is
     *     {@link Image }
     *     
     */
    public Image getSkeletonImage() {
        return skeletonImage;
    }

    /**
     * Sets the value of the skeletonImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Image }
     *     
     */
    public void setSkeletonImage(Image value) {
        this.skeletonImage = value;
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
