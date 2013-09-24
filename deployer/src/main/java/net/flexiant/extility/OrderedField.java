
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for orderedField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="orderedField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aggregationFunction" type="{http://extility.flexiant.net}aggregation" minOccurs="0"/>
 *         &lt;element name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortOrder" type="{http://extility.flexiant.net}resultOrder" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderedField", propOrder = {
    "aggregationFunction",
    "fieldName",
    "sortOrder"
})
public class OrderedField {

    protected Aggregation aggregationFunction;
    protected String fieldName;
    protected ResultOrder sortOrder;

    /**
     * Gets the value of the aggregationFunction property.
     * 
     * @return
     *     possible object is
     *     {@link Aggregation }
     *     
     */
    public Aggregation getAggregationFunction() {
        return aggregationFunction;
    }

    /**
     * Sets the value of the aggregationFunction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Aggregation }
     *     
     */
    public void setAggregationFunction(Aggregation value) {
        this.aggregationFunction = value;
    }

    /**
     * Gets the value of the fieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the sortOrder property.
     * 
     * @return
     *     possible object is
     *     {@link ResultOrder }
     *     
     */
    public ResultOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultOrder }
     *     
     */
    public void setSortOrder(ResultOrder value) {
        this.sortOrder = value;
    }

}
