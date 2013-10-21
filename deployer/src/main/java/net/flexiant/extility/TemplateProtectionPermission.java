
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for templateProtectionPermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="templateProtectionPermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="canDeleteIndividually" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canIndividuallyStartStop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="canModify" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "templateProtectionPermission", propOrder = {
    "canDeleteIndividually",
    "canIndividuallyStartStop",
    "canModify"
})
public class TemplateProtectionPermission {

    protected boolean canDeleteIndividually;
    protected boolean canIndividuallyStartStop;
    protected boolean canModify;

    /**
     * Gets the value of the canDeleteIndividually property.
     * 
     */
    public boolean isCanDeleteIndividually() {
        return canDeleteIndividually;
    }

    /**
     * Sets the value of the canDeleteIndividually property.
     * 
     */
    public void setCanDeleteIndividually(boolean value) {
        this.canDeleteIndividually = value;
    }

    /**
     * Gets the value of the canIndividuallyStartStop property.
     * 
     */
    public boolean isCanIndividuallyStartStop() {
        return canIndividuallyStartStop;
    }

    /**
     * Sets the value of the canIndividuallyStartStop property.
     * 
     */
    public void setCanIndividuallyStartStop(boolean value) {
        this.canIndividuallyStartStop = value;
    }

    /**
     * Gets the value of the canModify property.
     * 
     */
    public boolean isCanModify() {
        return canModify;
    }

    /**
     * Sets the value of the canModify property.
     * 
     */
    public void setCanModify(boolean value) {
        this.canModify = value;
    }

}
