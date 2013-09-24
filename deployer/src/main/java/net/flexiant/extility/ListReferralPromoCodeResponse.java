
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listReferralPromoCodeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listReferralPromoCodeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listReferralPromoCode" type="{http://extility.flexiant.net}listResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listReferralPromoCodeResponse", propOrder = {
    "listReferralPromoCode"
})
public class ListReferralPromoCodeResponse {

    protected ListResult listReferralPromoCode;

    /**
     * Gets the value of the listReferralPromoCode property.
     * 
     * @return
     *     possible object is
     *     {@link ListResult }
     *     
     */
    public ListResult getListReferralPromoCode() {
        return listReferralPromoCode;
    }

    /**
     * Sets the value of the listReferralPromoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListResult }
     *     
     */
    public void setListReferralPromoCode(ListResult value) {
        this.listReferralPromoCode = value;
    }

}
