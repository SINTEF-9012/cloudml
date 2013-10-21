
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for measuredValue complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="measuredValue">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}pseudoResource">
 *       &lt;sequence>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="resourceUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billingEntityUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="measureKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="measurement" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="measurementType" type="{http://extility.flexiant.net}measureType" minOccurs="0"/>
 *         &lt;element name="measurementAsValue" type="{http://extility.flexiant.net}value" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "measuredValue", propOrder = {
    "timestamp",
    "resourceUUID",
    "customerUUID",
    "billingEntityUUID",
    "measureKey",
    "measurement",
    "measurementType",
    "measurementAsValue"
})
public class MeasuredValue
    extends PseudoResource
{

    protected long timestamp;
    protected String resourceUUID;
    protected String customerUUID;
    protected String billingEntityUUID;
    protected String measureKey;
    protected double measurement;
    protected MeasureType measurementType;
    protected Value measurementAsValue;

    /**
     * Gets the value of the timestamp property.
     * 
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     */
    public void setTimestamp(long value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the resourceUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceUUID() {
        return resourceUUID;
    }

    /**
     * Sets the value of the resourceUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceUUID(String value) {
        this.resourceUUID = value;
    }

    /**
     * Gets the value of the customerUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerUUID() {
        return customerUUID;
    }

    /**
     * Sets the value of the customerUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerUUID(String value) {
        this.customerUUID = value;
    }

    /**
     * Gets the value of the billingEntityUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingEntityUUID() {
        return billingEntityUUID;
    }

    /**
     * Sets the value of the billingEntityUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingEntityUUID(String value) {
        this.billingEntityUUID = value;
    }

    /**
     * Gets the value of the measureKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasureKey() {
        return measureKey;
    }

    /**
     * Sets the value of the measureKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasureKey(String value) {
        this.measureKey = value;
    }

    /**
     * Gets the value of the measurement property.
     * 
     */
    public double getMeasurement() {
        return measurement;
    }

    /**
     * Sets the value of the measurement property.
     * 
     */
    public void setMeasurement(double value) {
        this.measurement = value;
    }

    /**
     * Gets the value of the measurementType property.
     * 
     * @return
     *     possible object is
     *     {@link MeasureType }
     *     
     */
    public MeasureType getMeasurementType() {
        return measurementType;
    }

    /**
     * Sets the value of the measurementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureType }
     *     
     */
    public void setMeasurementType(MeasureType value) {
        this.measurementType = value;
    }

    /**
     * Gets the value of the measurementAsValue property.
     * 
     * @return
     *     possible object is
     *     {@link Value }
     *     
     */
    public Value getMeasurementAsValue() {
        return measurementAsValue;
    }

    /**
     * Sets the value of the measurementAsValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Value }
     *     
     */
    public void setMeasurementAsValue(Value value) {
        this.measurementAsValue = value;
    }

}
