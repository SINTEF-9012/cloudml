
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for query complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="query">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aggregationFields" type="{http://extility.flexiant.net}aggregationField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="aggregationFilters" type="{http://extility.flexiant.net}aggregationFilter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="groupByFields" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="limit" type="{http://extility.flexiant.net}queryLimit" minOccurs="0"/>
 *         &lt;element name="outputFields" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="resourceType" type="{http://extility.flexiant.net}resourceType" minOccurs="0"/>
 *         &lt;element name="searchFilter" type="{http://extility.flexiant.net}searchFilter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query", propOrder = {
    "aggregationFields",
    "aggregationFilters",
    "groupByFields",
    "limit",
    "outputFields",
    "resourceType",
    "searchFilter"
})
public class Query {

    @XmlElement(nillable = true)
    protected List<AggregationField> aggregationFields;
    @XmlElement(nillable = true)
    protected List<AggregationFilter> aggregationFilters;
    @XmlElement(nillable = true)
    protected List<String> groupByFields;
    protected QueryLimit limit;
    @XmlElement(nillable = true)
    protected List<String> outputFields;
    protected ResourceType resourceType;
    protected SearchFilter searchFilter;

    /**
     * Gets the value of the aggregationFields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregationFields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregationFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AggregationField }
     * 
     * 
     */
    public List<AggregationField> getAggregationFields() {
        if (aggregationFields == null) {
            aggregationFields = new ArrayList<AggregationField>();
        }
        return this.aggregationFields;
    }

    /**
     * Gets the value of the aggregationFilters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aggregationFilters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAggregationFilters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AggregationFilter }
     * 
     * 
     */
    public List<AggregationFilter> getAggregationFilters() {
        if (aggregationFilters == null) {
            aggregationFilters = new ArrayList<AggregationFilter>();
        }
        return this.aggregationFilters;
    }

    /**
     * Gets the value of the groupByFields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupByFields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupByFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGroupByFields() {
        if (groupByFields == null) {
            groupByFields = new ArrayList<String>();
        }
        return this.groupByFields;
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link QueryLimit }
     *     
     */
    public QueryLimit getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryLimit }
     *     
     */
    public void setLimit(QueryLimit value) {
        this.limit = value;
    }

    /**
     * Gets the value of the outputFields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outputFields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutputFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOutputFields() {
        if (outputFields == null) {
            outputFields = new ArrayList<String>();
        }
        return this.outputFields;
    }

    /**
     * Gets the value of the resourceType property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceType }
     *     
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Sets the value of the resourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceType }
     *     
     */
    public void setResourceType(ResourceType value) {
        this.resourceType = value;
    }

    /**
     * Gets the value of the searchFilter property.
     * 
     * @return
     *     possible object is
     *     {@link SearchFilter }
     *     
     */
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    /**
     * Sets the value of the searchFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchFilter }
     *     
     */
    public void setSearchFilter(SearchFilter value) {
        this.searchFilter = value;
    }

}
