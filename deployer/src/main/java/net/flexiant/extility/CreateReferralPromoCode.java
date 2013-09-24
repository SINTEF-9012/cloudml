
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createReferralPromoCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createReferralPromoCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referralPromoCode" type="{http://extility.flexiant.net}referralPromoCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createReferralPromoCode", propOrder = {
    "referralPromoCode"
})
public class CreateReferralPromoCode {

    protected ReferralPromoCode referralPromoCode;

    /**
     * Gets the value of the referralPromoCode property.
     * 
     * @return
     *     possible object is
     *     {@link ReferralPromoCode }
     *     
     */
    public ReferralPromoCode getReferralPromoCode() {
        return referralPromoCode;
    }

    /**
     * Sets the value of the referralPromoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferralPromoCode }
     *     
     */
    public void setReferralPromoCode(ReferralPromoCode value) {
        this.referralPromoCode = value;
    }

}
