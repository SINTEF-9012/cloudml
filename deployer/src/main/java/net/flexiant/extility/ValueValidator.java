
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueValidator complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="valueValidator">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validateString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validatorType" type="{http://extility.flexiant.net}validatorType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "valueValidator", propOrder = {
    "validateString",
    "validatorType"
})
public class ValueValidator {

    protected String validateString;
    protected ValidatorType validatorType;

    /**
     * Gets the value of the validateString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidateString() {
        return validateString;
    }

    /**
     * Sets the value of the validateString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidateString(String value) {
        this.validateString = value;
    }

    /**
     * Gets the value of the validatorType property.
     * 
     * @return
     *     possible object is
     *     {@link ValidatorType }
     *     
     */
    public ValidatorType getValidatorType() {
        return validatorType;
    }

    /**
     * Sets the value of the validatorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidatorType }
     *     
     */
    public void setValidatorType(ValidatorType value) {
        this.validatorType = value;
    }

}
