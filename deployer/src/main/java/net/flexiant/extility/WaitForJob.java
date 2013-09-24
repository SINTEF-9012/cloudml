
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for waitForJob complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="waitForJob">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="jobUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="noWaitForChildren" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "waitForJob", propOrder = {
    "jobUUID",
    "noWaitForChildren"
})
public class WaitForJob {

    protected String jobUUID;
    protected boolean noWaitForChildren;

    /**
     * Gets the value of the jobUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobUUID() {
        return jobUUID;
    }

    /**
     * Sets the value of the jobUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobUUID(String value) {
        this.jobUUID = value;
    }

    /**
     * Gets the value of the noWaitForChildren property.
     * 
     */
    public boolean isNoWaitForChildren() {
        return noWaitForChildren;
    }

    /**
     * Sets the value of the noWaitForChildren property.
     * 
     */
    public void setNoWaitForChildren(boolean value) {
        this.noWaitForChildren = value;
    }

}
