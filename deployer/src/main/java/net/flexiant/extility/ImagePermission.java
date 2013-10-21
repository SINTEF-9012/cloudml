
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for imagePermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="imagePermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="canBeDetachedFromServer" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canBeSecondaryDisk" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canClone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canConsole" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canCreateServer" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canHaveAdditionalDisks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canImage" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canSnapshot" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canStart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "imagePermission", propOrder = {
    "canBeDetachedFromServer",
    "canBeSecondaryDisk",
    "canClone",
    "canConsole",
    "canCreateServer",
    "canHaveAdditionalDisks",
    "canImage",
    "canSnapshot",
    "canStart"
})
public class ImagePermission {

    protected boolean canBeDetachedFromServer;
    protected boolean canBeSecondaryDisk;
    protected boolean canClone;
    protected boolean canConsole;
    protected boolean canCreateServer;
    protected boolean canHaveAdditionalDisks;
    protected boolean canImage;
    protected boolean canSnapshot;
    protected boolean canStart;

    /**
     * Gets the value of the canBeDetachedFromServer property.
     * 
     */
    public boolean isCanBeDetachedFromServer() {
        return canBeDetachedFromServer;
    }

    /**
     * Sets the value of the canBeDetachedFromServer property.
     * 
     */
    public void setCanBeDetachedFromServer(boolean value) {
        this.canBeDetachedFromServer = value;
    }

    /**
     * Gets the value of the canBeSecondaryDisk property.
     * 
     */
    public boolean isCanBeSecondaryDisk() {
        return canBeSecondaryDisk;
    }

    /**
     * Sets the value of the canBeSecondaryDisk property.
     * 
     */
    public void setCanBeSecondaryDisk(boolean value) {
        this.canBeSecondaryDisk = value;
    }

    /**
     * Gets the value of the canClone property.
     * 
     */
    public boolean isCanClone() {
        return canClone;
    }

    /**
     * Sets the value of the canClone property.
     * 
     */
    public void setCanClone(boolean value) {
        this.canClone = value;
    }

    /**
     * Gets the value of the canConsole property.
     * 
     */
    public boolean isCanConsole() {
        return canConsole;
    }

    /**
     * Sets the value of the canConsole property.
     * 
     */
    public void setCanConsole(boolean value) {
        this.canConsole = value;
    }

    /**
     * Gets the value of the canCreateServer property.
     * 
     */
    public boolean isCanCreateServer() {
        return canCreateServer;
    }

    /**
     * Sets the value of the canCreateServer property.
     * 
     */
    public void setCanCreateServer(boolean value) {
        this.canCreateServer = value;
    }

    /**
     * Gets the value of the canHaveAdditionalDisks property.
     * 
     */
    public boolean isCanHaveAdditionalDisks() {
        return canHaveAdditionalDisks;
    }

    /**
     * Sets the value of the canHaveAdditionalDisks property.
     * 
     */
    public void setCanHaveAdditionalDisks(boolean value) {
        this.canHaveAdditionalDisks = value;
    }

    /**
     * Gets the value of the canImage property.
     * 
     */
    public boolean isCanImage() {
        return canImage;
    }

    /**
     * Sets the value of the canImage property.
     * 
     */
    public void setCanImage(boolean value) {
        this.canImage = value;
    }

    /**
     * Gets the value of the canSnapshot property.
     * 
     */
    public boolean isCanSnapshot() {
        return canSnapshot;
    }

    /**
     * Sets the value of the canSnapshot property.
     * 
     */
    public void setCanSnapshot(boolean value) {
        this.canSnapshot = value;
    }

    /**
     * Gets the value of the canStart property.
     * 
     */
    public boolean isCanStart() {
        return canStart;
    }

    /**
     * Sets the value of the canStart property.
     * 
     */
    public void setCanStart(boolean value) {
        this.canStart = value;
    }

}
