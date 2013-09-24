
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for disk complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="disk">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://extility.flexiant.net}diskStatus" minOccurs="0"/>
 *         &lt;element name="imageUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snapshotUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serverUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="index" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="imageName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snapshotName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serverName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="storageUnitUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="iso" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="storageCapabilities" type="{http://extility.flexiant.net}storageCapability" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "disk", propOrder = {
    "size",
    "status",
    "imageUUID",
    "snapshotUUID",
    "serverUUID",
    "index",
    "imageName",
    "snapshotName",
    "serverName",
    "storageUnitUUID",
    "iso",
    "storageCapabilities"
})
public class Disk
    extends VirtualResource
{

    protected long size;
    protected DiskStatus status;
    protected String imageUUID;
    protected String snapshotUUID;
    protected String serverUUID;
    protected int index;
    protected String imageName;
    protected String snapshotName;
    protected String serverName;
    protected String storageUnitUUID;
    protected boolean iso;
    @XmlElement(nillable = true)
    protected List<StorageCapability> storageCapabilities;

    /**
     * Gets the value of the size property.
     * 
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     */
    public void setSize(long value) {
        this.size = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link DiskStatus }
     *     
     */
    public DiskStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiskStatus }
     *     
     */
    public void setStatus(DiskStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the imageUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageUUID() {
        return imageUUID;
    }

    /**
     * Sets the value of the imageUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageUUID(String value) {
        this.imageUUID = value;
    }

    /**
     * Gets the value of the snapshotUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnapshotUUID() {
        return snapshotUUID;
    }

    /**
     * Sets the value of the snapshotUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnapshotUUID(String value) {
        this.snapshotUUID = value;
    }

    /**
     * Gets the value of the serverUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerUUID() {
        return serverUUID;
    }

    /**
     * Sets the value of the serverUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerUUID(String value) {
        this.serverUUID = value;
    }

    /**
     * Gets the value of the index property.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the value of the imageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Sets the value of the imageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageName(String value) {
        this.imageName = value;
    }

    /**
     * Gets the value of the snapshotName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnapshotName() {
        return snapshotName;
    }

    /**
     * Sets the value of the snapshotName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnapshotName(String value) {
        this.snapshotName = value;
    }

    /**
     * Gets the value of the serverName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the value of the serverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerName(String value) {
        this.serverName = value;
    }

    /**
     * Gets the value of the storageUnitUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStorageUnitUUID() {
        return storageUnitUUID;
    }

    /**
     * Sets the value of the storageUnitUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStorageUnitUUID(String value) {
        this.storageUnitUUID = value;
    }

    /**
     * Gets the value of the iso property.
     * 
     */
    public boolean isIso() {
        return iso;
    }

    /**
     * Sets the value of the iso property.
     * 
     */
    public void setIso(boolean value) {
        this.iso = value;
    }

    /**
     * Gets the value of the storageCapabilities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the storageCapabilities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStorageCapabilities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StorageCapability }
     * 
     * 
     */
    public List<StorageCapability> getStorageCapabilities() {
        if (storageCapabilities == null) {
            storageCapabilities = new ArrayList<StorageCapability>();
        }
        return this.storageCapabilities;
    }

}
