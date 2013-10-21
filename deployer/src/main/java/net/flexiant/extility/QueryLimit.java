
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryLimit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryLimit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="loadChildren" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="maxRecords" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orderBy" type="{http://extility.flexiant.net}orderedField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="to" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryLimit", propOrder = {
    "from",
    "loadChildren",
    "maxRecords",
    "orderBy",
    "to"
})
public class QueryLimit {

    protected int from;
    protected boolean loadChildren;
    protected int maxRecords;
    @XmlElement(nillable = true)
    protected List<OrderedField> orderBy;
    protected int to;

    /**
     * Gets the value of the from property.
     * 
     */
    public int getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     */
    public void setFrom(int value) {
        this.from = value;
    }

    /**
     * Gets the value of the loadChildren property.
     * 
     */
    public boolean isLoadChildren() {
        return loadChildren;
    }

    /**
     * Sets the value of the loadChildren property.
     * 
     */
    public void setLoadChildren(boolean value) {
        this.loadChildren = value;
    }

    /**
     * Gets the value of the maxRecords property.
     * 
     */
    public int getMaxRecords() {
        return maxRecords;
    }

    /**
     * Sets the value of the maxRecords property.
     * 
     */
    public void setMaxRecords(int value) {
        this.maxRecords = value;
    }

    /**
     * Gets the value of the orderBy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orderBy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrderBy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrderedField }
     * 
     * 
     */
    public List<OrderedField> getOrderBy() {
        if (orderBy == null) {
            orderBy = new ArrayList<OrderedField>();
        }
        return this.orderBy;
    }

    /**
     * Gets the value of the to property.
     * 
     */
    public int getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     */
    public void setTo(int value) {
        this.to = value;
    }

}
