
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for promoCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="promoCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="promoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotionUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reuse" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="used" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "promoCode", propOrder = {
    "promoCode",
    "promotionUUID",
    "reuse",
    "used"
})
@XmlSeeAlso({
    ReferralPromoCode.class
})
public class PromoCode {

    protected String promoCode;
    protected String promotionUUID;
    protected boolean reuse;
    protected boolean used;

    /**
     * Gets the value of the promoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * Sets the value of the promoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromoCode(String value) {
        this.promoCode = value;
    }

    /**
     * Gets the value of the promotionUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionUUID() {
        return promotionUUID;
    }

    /**
     * Sets the value of the promotionUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionUUID(String value) {
        this.promotionUUID = value;
    }

    /**
     * Gets the value of the reuse property.
     * 
     */
    public boolean isReuse() {
        return reuse;
    }

    /**
     * Sets the value of the reuse property.
     * 
     */
    public void setReuse(boolean value) {
        this.reuse = value;
    }

    /**
     * Gets the value of the used property.
     * 
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Sets the value of the used property.
     * 
     */
    public void setUsed(boolean value) {
        this.used = value;
    }

}
