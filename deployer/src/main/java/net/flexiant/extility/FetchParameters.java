
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fetchParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fetchParameters">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}pseudoResource">
 *       &lt;sequence>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkSum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="makeImage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="genPassword" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="networkUUIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchParameters", propOrder = {
    "url",
    "authUserName",
    "authPassword",
    "checkSum",
    "defaultUserName",
    "makeImage",
    "genPassword",
    "networkUUIDs"
})
public class FetchParameters
    extends PseudoResource
{

    protected String url;
    protected String authUserName;
    protected String authPassword;
    protected String checkSum;
    protected String defaultUserName;
    protected boolean makeImage;
    protected boolean genPassword;
    @XmlElement(nillable = true)
    protected List<String> networkUUIDs;

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the authUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthUserName() {
        return authUserName;
    }

    /**
     * Sets the value of the authUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthUserName(String value) {
        this.authUserName = value;
    }

    /**
     * Gets the value of the authPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * Sets the value of the authPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthPassword(String value) {
        this.authPassword = value;
    }

    /**
     * Gets the value of the checkSum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * Sets the value of the checkSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckSum(String value) {
        this.checkSum = value;
    }

    /**
     * Gets the value of the defaultUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultUserName() {
        return defaultUserName;
    }

    /**
     * Sets the value of the defaultUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultUserName(String value) {
        this.defaultUserName = value;
    }

    /**
     * Gets the value of the makeImage property.
     * 
     */
    public boolean isMakeImage() {
        return makeImage;
    }

    /**
     * Sets the value of the makeImage property.
     * 
     */
    public void setMakeImage(boolean value) {
        this.makeImage = value;
    }

    /**
     * Gets the value of the genPassword property.
     * 
     */
    public boolean isGenPassword() {
        return genPassword;
    }

    /**
     * Sets the value of the genPassword property.
     * 
     */
    public void setGenPassword(boolean value) {
        this.genPassword = value;
    }

    /**
     * Gets the value of the networkUUIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the networkUUIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNetworkUUIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNetworkUUIDs() {
        if (networkUUIDs == null) {
            networkUUIDs = new ArrayList<String>();
        }
        return this.networkUUIDs;
    }

}
