
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listUnitTransactionSummaryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listUnitTransactionSummaryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listUnitTransactionSummary" type="{http://extility.flexiant.net}listResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listUnitTransactionSummaryResponse", propOrder = {
    "listUnitTransactionSummary"
})
public class ListUnitTransactionSummaryResponse {

    protected ListResult listUnitTransactionSummary;

    /**
     * Gets the value of the listUnitTransactionSummary property.
     * 
     * @return
     *     possible object is
     *     {@link ListResult }
     *     
     */
    public ListResult getListUnitTransactionSummary() {
        return listUnitTransactionSummary;
    }

    /**
     * Sets the value of the listUnitTransactionSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListResult }
     *     
     */
    public void setListUnitTransactionSummary(ListResult value) {
        this.listUnitTransactionSummary = value;
    }

}
