
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for server complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="server">
 *   &lt;complexContent>
 *     &lt;extension base="{http://extility.flexiant.net}virtualResource">
 *       &lt;sequence>
 *         &lt;element name="cpu" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ram" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="initialUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="initialPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://extility.flexiant.net}serverStatus" minOccurs="0"/>
 *         &lt;element name="serverKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snapshotUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disks" type="{http://extility.flexiant.net}disk" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nics" type="{http://extility.flexiant.net}nic" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sshkeys" type="{http://extility.flexiant.net}sshKey" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="disableEmulatedDevices" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="imageName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imagePermission" type="{http://extility.flexiant.net}imagePermission" minOccurs="0"/>
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
@XmlType(name = "server", propOrder = {
    "cpu",
    "ram",
    "initialUser",
    "initialPassword",
    "status",
    "serverKey",
    "imageUUID",
    "snapshotUUID",
    "disks",
    "nics",
    "sshkeys",
    "disableEmulatedDevices",
    "imageName",
    "imagePermission",
    "storageCapabilities"
})
public class Server
    extends VirtualResource
{

    protected int cpu;
    protected int ram;
    protected String initialUser;
    protected String initialPassword;
    protected ServerStatus status;
    protected String serverKey;
    protected String imageUUID;
    protected String snapshotUUID;
    @XmlElement(nillable = true)
    protected List<Disk> disks;
    @XmlElement(nillable = true)
    protected List<Nic> nics;
    @XmlElement(nillable = true)
    protected List<SshKey> sshkeys;
    protected boolean disableEmulatedDevices;
    protected String imageName;
    protected ImagePermission imagePermission;
    @XmlElement(nillable = true)
    protected List<StorageCapability> storageCapabilities;

    /**
     * Gets the value of the cpu property.
     * 
     */
    public int getCpu() {
        return cpu;
    }

    /**
     * Sets the value of the cpu property.
     * 
     */
    public void setCpu(int value) {
        this.cpu = value;
    }

    /**
     * Gets the value of the ram property.
     * 
     */
    public int getRam() {
        return ram;
    }

    /**
     * Sets the value of the ram property.
     * 
     */
    public void setRam(int value) {
        this.ram = value;
    }

    /**
     * Gets the value of the initialUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialUser() {
        return initialUser;
    }

    /**
     * Sets the value of the initialUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialUser(String value) {
        this.initialUser = value;
    }

    /**
     * Gets the value of the initialPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialPassword() {
        return initialPassword;
    }

    /**
     * Sets the value of the initialPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialPassword(String value) {
        this.initialPassword = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link ServerStatus }
     *     
     */
    public ServerStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerStatus }
     *     
     */
    public void setStatus(ServerStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the serverKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerKey() {
        return serverKey;
    }

    /**
     * Sets the value of the serverKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerKey(String value) {
        this.serverKey = value;
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
     * Gets the value of the disks property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disks property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Disk }
     * 
     * 
     */
    public List<Disk> getDisks() {
        if (disks == null) {
            disks = new ArrayList<Disk>();
        }
        return this.disks;
    }

    /**
     * Gets the value of the nics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nic }
     * 
     * 
     */
    public List<Nic> getNics() {
        if (nics == null) {
            nics = new ArrayList<Nic>();
        }
        return this.nics;
    }

    /**
     * Gets the value of the sshkeys property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sshkeys property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSshkeys().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SshKey }
     * 
     * 
     */
    public List<SshKey> getSshkeys() {
        if (sshkeys == null) {
            sshkeys = new ArrayList<SshKey>();
        }
        return this.sshkeys;
    }

    /**
     * Gets the value of the disableEmulatedDevices property.
     * 
     */
    public boolean isDisableEmulatedDevices() {
        return disableEmulatedDevices;
    }

    /**
     * Sets the value of the disableEmulatedDevices property.
     * 
     */
    public void setDisableEmulatedDevices(boolean value) {
        this.disableEmulatedDevices = value;
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
     * Gets the value of the imagePermission property.
     * 
     * @return
     *     possible object is
     *     {@link ImagePermission }
     *     
     */
    public ImagePermission getImagePermission() {
        return imagePermission;
    }

    /**
     * Sets the value of the imagePermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImagePermission }
     *     
     */
    public void setImagePermission(ImagePermission value) {
        this.imagePermission = value;
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
