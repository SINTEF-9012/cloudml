
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultRow" type="{http://extility.flexiant.net}resultRow" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="totalCount" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="listFrom" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="listTo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryResult", propOrder = {
    "resultRow",
    "totalCount",
    "listFrom",
    "listTo"
})
public class QueryResult {

    protected List<ResultRow> resultRow;
    protected long totalCount;
    protected int listFrom;
    protected int listTo;

    /**
     * Gets the value of the resultRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultRow }
     * 
     * 
     */
    public List<ResultRow> getResultRow() {
        if (resultRow == null) {
            resultRow = new ArrayList<ResultRow>();
        }
        return this.resultRow;
    }

    /**
     * Gets the value of the totalCount property.
     * 
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * Sets the value of the totalCount property.
     * 
     */
    public void setTotalCount(long value) {
        this.totalCount = value;
    }

    /**
     * Gets the value of the listFrom property.
     * 
     */
    public int getListFrom() {
        return listFrom;
    }

    /**
     * Sets the value of the listFrom property.
     * 
     */
    public void setListFrom(int value) {
        this.listFrom = value;
    }

    /**
     * Gets the value of the listTo property.
     * 
     */
    public int getListTo() {
        return listTo;
    }

    /**
     * Sets the value of the listTo property.
     * 
     */
    public void setListTo(int value) {
        this.listTo = value;
    }

}
