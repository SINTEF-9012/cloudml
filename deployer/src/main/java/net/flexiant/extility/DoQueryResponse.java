
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for doQueryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="doQueryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryResult" type="{http://extility.flexiant.net}queryResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "doQueryResponse", propOrder = {
    "queryResult"
})
public class DoQueryResponse {

    protected QueryResult queryResult;

    /**
     * Gets the value of the queryResult property.
     * 
     * @return
     *     possible object is
     *     {@link QueryResult }
     *     
     */
    public QueryResult getQueryResult() {
        return queryResult;
    }

    /**
     * Sets the value of the queryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryResult }
     *     
     */
    public void setQueryResult(QueryResult value) {
        this.queryResult = value;
    }

}
