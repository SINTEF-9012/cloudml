
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dryRunTemplateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dryRunTemplateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dryRunResult" type="{http://extility.flexiant.net}dryRunResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dryRunTemplateResponse", propOrder = {
    "dryRunResult"
})
public class DryRunTemplateResponse {

    protected DryRunResult dryRunResult;

    /**
     * Gets the value of the dryRunResult property.
     * 
     * @return
     *     possible object is
     *     {@link DryRunResult }
     *     
     */
    public DryRunResult getDryRunResult() {
        return dryRunResult;
    }

    /**
     * Sets the value of the dryRunResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DryRunResult }
     *     
     */
    public void setDryRunResult(DryRunResult value) {
        this.dryRunResult = value;
    }

}
