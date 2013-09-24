
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listProductPurchasesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listProductPurchasesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listProductPurchases" type="{http://extility.flexiant.net}listResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listProductPurchasesResponse", propOrder = {
    "listProductPurchases"
})
public class ListProductPurchasesResponse {

    protected ListResult listProductPurchases;

    /**
     * Gets the value of the listProductPurchases property.
     * 
     * @return
     *     possible object is
     *     {@link ListResult }
     *     
     */
    public ListResult getListProductPurchases() {
        return listProductPurchases;
    }

    /**
     * Sets the value of the listProductPurchases property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListResult }
     *     
     */
    public void setListProductPurchases(ListResult value) {
        this.listProductPurchases = value;
    }

}
