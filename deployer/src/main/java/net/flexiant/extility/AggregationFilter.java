
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for aggregationFilter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="aggregationFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aggregationField" type="{http://extility.flexiant.net}aggregationField" minOccurs="0"/>
 *         &lt;element name="aggregationValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="condition" type="{http://extility.flexiant.net}condition" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "aggregationFilter", propOrder = {
    "aggregationField",
    "aggregationValue",
    "condition"
})
public class AggregationFilter {

    protected AggregationField aggregationField;
    protected double aggregationValue;
    protected Condition condition;

    /**
     * Gets the value of the aggregationField property.
     * 
     * @return
     *     possible object is
     *     {@link AggregationField }
     *     
     */
    public AggregationField getAggregationField() {
        return aggregationField;
    }

    /**
     * Sets the value of the aggregationField property.
     * 
     * @param value
     *     allowed object is
     *     {@link AggregationField }
     *     
     */
    public void setAggregationField(AggregationField value) {
        this.aggregationField = value;
    }

    /**
     * Gets the value of the aggregationValue property.
     * 
     */
    public double getAggregationValue() {
        return aggregationValue;
    }

    /**
     * Sets the value of the aggregationValue property.
     * 
     */
    public void setAggregationValue(double value) {
        this.aggregationValue = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link Condition }
     *     
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Condition }
     *     
     */
    public void setCondition(Condition value) {
        this.condition = value;
    }

}
