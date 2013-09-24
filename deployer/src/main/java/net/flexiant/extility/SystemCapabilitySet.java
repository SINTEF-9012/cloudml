
package net.flexiant.extility;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for systemCapabilitySet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="systemCapabilitySet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cloning" type="{http://extility.flexiant.net}cloning" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="email" type="{http://extility.flexiant.net}email" minOccurs="0"/>
 *         &lt;element name="emulatedDevices" type="{http://extility.flexiant.net}emulatedDevices" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="maxLimit">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://extility.flexiant.net}maxLimit" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="networking" type="{http://extility.flexiant.net}networking" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="payment" type="{http://extility.flexiant.net}payment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="publish" type="{http://extility.flexiant.net}publish" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signup" type="{http://extility.flexiant.net}signup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="snapshotting" type="{http://extility.flexiant.net}snapshotting" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vnc" type="{http://extility.flexiant.net}vnc" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "systemCapabilitySet", propOrder = {
    "cloning",
    "email",
    "emulatedDevices",
    "maxLimit",
    "networking",
    "payment",
    "publish",
    "signup",
    "snapshotting",
    "vnc"
})
public class SystemCapabilitySet {

    @XmlElement(nillable = true)
    protected List<Cloning> cloning;
    protected Email email;
    @XmlElement(nillable = true)
    protected List<EmulatedDevices> emulatedDevices;
    @XmlElement(required = true)
    protected SystemCapabilitySet.MaxLimit maxLimit;
    @XmlElement(nillable = true)
    protected List<Networking> networking;
    @XmlElement(nillable = true)
    protected List<Payment> payment;
    @XmlElement(nillable = true)
    protected List<Publish> publish;
    @XmlElement(nillable = true)
    protected List<Signup> signup;
    @XmlElement(nillable = true)
    protected List<Snapshotting> snapshotting;
    @XmlElement(nillable = true)
    protected List<Vnc> vnc;

    /**
     * Gets the value of the cloning property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cloning property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCloning().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cloning }
     * 
     * 
     */
    public List<Cloning> getCloning() {
        if (cloning == null) {
            cloning = new ArrayList<Cloning>();
        }
        return this.cloning;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link Email }
     *     
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link Email }
     *     
     */
    public void setEmail(Email value) {
        this.email = value;
    }

    /**
     * Gets the value of the emulatedDevices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the emulatedDevices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmulatedDevices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmulatedDevices }
     * 
     * 
     */
    public List<EmulatedDevices> getEmulatedDevices() {
        if (emulatedDevices == null) {
            emulatedDevices = new ArrayList<EmulatedDevices>();
        }
        return this.emulatedDevices;
    }

    /**
     * Gets the value of the maxLimit property.
     * 
     * @return
     *     possible object is
     *     {@link SystemCapabilitySet.MaxLimit }
     *     
     */
    public SystemCapabilitySet.MaxLimit getMaxLimit() {
        return maxLimit;
    }

    /**
     * Sets the value of the maxLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemCapabilitySet.MaxLimit }
     *     
     */
    public void setMaxLimit(SystemCapabilitySet.MaxLimit value) {
        this.maxLimit = value;
    }

    /**
     * Gets the value of the networking property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the networking property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNetworking().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Networking }
     * 
     * 
     */
    public List<Networking> getNetworking() {
        if (networking == null) {
            networking = new ArrayList<Networking>();
        }
        return this.networking;
    }

    /**
     * Gets the value of the payment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Payment }
     * 
     * 
     */
    public List<Payment> getPayment() {
        if (payment == null) {
            payment = new ArrayList<Payment>();
        }
        return this.payment;
    }

    /**
     * Gets the value of the publish property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the publish property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPublish().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Publish }
     * 
     * 
     */
    public List<Publish> getPublish() {
        if (publish == null) {
            publish = new ArrayList<Publish>();
        }
        return this.publish;
    }

    /**
     * Gets the value of the signup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Signup }
     * 
     * 
     */
    public List<Signup> getSignup() {
        if (signup == null) {
            signup = new ArrayList<Signup>();
        }
        return this.signup;
    }

    /**
     * Gets the value of the snapshotting property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the snapshotting property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSnapshotting().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Snapshotting }
     * 
     * 
     */
    public List<Snapshotting> getSnapshotting() {
        if (snapshotting == null) {
            snapshotting = new ArrayList<Snapshotting>();
        }
        return this.snapshotting;
    }

    /**
     * Gets the value of the vnc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vnc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVnc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vnc }
     * 
     * 
     */
    public List<Vnc> getVnc() {
        if (vnc == null) {
            vnc = new ArrayList<Vnc>();
        }
        return this.vnc;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://extility.flexiant.net}maxLimit" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class MaxLimit {

        protected List<SystemCapabilitySet.MaxLimit.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SystemCapabilitySet.MaxLimit.Entry }
         * 
         * 
         */
        public List<SystemCapabilitySet.MaxLimit.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<SystemCapabilitySet.MaxLimit.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://extility.flexiant.net}maxLimit" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected net.flexiant.extility.MaxLimit key;
            protected Integer value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link net.flexiant.extility.MaxLimit }
             *     
             */
            public net.flexiant.extility.MaxLimit getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link net.flexiant.extility.MaxLimit }
             *     
             */
            public void setKey(net.flexiant.extility.MaxLimit value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link Integer }
             *     
             */
            public Integer getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link Integer }
             *     
             */
            public void setValue(Integer value) {
                this.value = value;
            }

        }

    }

}
