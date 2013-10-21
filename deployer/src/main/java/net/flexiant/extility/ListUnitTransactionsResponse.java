
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listUnitTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listUnitTransactionsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listUnitTransaction" type="{http://extility.flexiant.net}listResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listUnitTransactionsResponse", propOrder = {
    "listUnitTransaction"
})
public class ListUnitTransactionsResponse {

    protected ListResult listUnitTransaction;

    /**
     * Gets the value of the listUnitTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link ListResult }
     *     
     */
    public ListResult getListUnitTransaction() {
        return listUnitTransaction;
    }

    /**
     * Sets the value of the listUnitTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListResult }
     *     
     */
    public void setListUnitTransaction(ListResult value) {
        this.listUnitTransaction = value;
    }

}
