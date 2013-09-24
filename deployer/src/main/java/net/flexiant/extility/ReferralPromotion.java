
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for referralPromotion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="referralPromotion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}promotion">
 *       &lt;sequence>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="invitorProductOfferUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inviteeMinimumPurchase" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unitCost" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="maxOutStanding" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "referralPromotion", propOrder = {
    "priority",
    "invitorProductOfferUUID",
    "inviteeMinimumPurchase",
    "unitCost",
    "maxOutStanding"
})
public class ReferralPromotion
    extends Promotion
{

    protected int priority;
    protected String invitorProductOfferUUID;
    protected int inviteeMinimumPurchase;
    protected double unitCost;
    protected int maxOutStanding;

    /**
     * Gets the value of the priority property.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Gets the value of the invitorProductOfferUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvitorProductOfferUUID() {
        return invitorProductOfferUUID;
    }

    /**
     * Sets the value of the invitorProductOfferUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvitorProductOfferUUID(String value) {
        this.invitorProductOfferUUID = value;
    }

    /**
     * Gets the value of the inviteeMinimumPurchase property.
     * 
     */
    public int getInviteeMinimumPurchase() {
        return inviteeMinimumPurchase;
    }

    /**
     * Sets the value of the inviteeMinimumPurchase property.
     * 
     */
    public void setInviteeMinimumPurchase(int value) {
        this.inviteeMinimumPurchase = value;
    }

    /**
     * Gets the value of the unitCost property.
     * 
     */
    public double getUnitCost() {
        return unitCost;
    }

    /**
     * Sets the value of the unitCost property.
     * 
     */
    public void setUnitCost(double value) {
        this.unitCost = value;
    }

    /**
     * Gets the value of the maxOutStanding property.
     * 
     */
    public int getMaxOutStanding() {
        return maxOutStanding;
    }

    /**
     * Sets the value of the maxOutStanding property.
     * 
     */
    public void setMaxOutStanding(int value) {
        this.maxOutStanding = value;
    }

}
