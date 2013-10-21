
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for resourceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="resourceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLUSTER"/>
 *     &lt;enumeration value="CUSTOMER"/>
 *     &lt;enumeration value="USER"/>
 *     &lt;enumeration value="DISK"/>
 *     &lt;enumeration value="SNAPSHOT"/>
 *     &lt;enumeration value="FIREWALL"/>
 *     &lt;enumeration value="NIC"/>
 *     &lt;enumeration value="IMAGE"/>
 *     &lt;enumeration value="SERVER"/>
 *     &lt;enumeration value="SUBNET"/>
 *     &lt;enumeration value="VDC"/>
 *     &lt;enumeration value="NETWORK"/>
 *     &lt;enumeration value="SSHKEY"/>
 *     &lt;enumeration value="JOB"/>
 *     &lt;enumeration value="PRODUCTOFFER"/>
 *     &lt;enumeration value="FIREWALL_TEMPLATE"/>
 *     &lt;enumeration value="BILLING_ENTITY"/>
 *     &lt;enumeration value="GROUP"/>
 *     &lt;enumeration value="ANY"/>
 *     &lt;enumeration value="PAYMENTCARD"/>
 *     &lt;enumeration value="PAYMENTMETHOD"/>
 *     &lt;enumeration value="INVOICE"/>
 *     &lt;enumeration value="PROMOTION"/>
 *     &lt;enumeration value="REFERRAL_PROMOTION"/>
 *     &lt;enumeration value="DEPLOYMENT_TEMPLATE"/>
 *     &lt;enumeration value="DEPLOYMENT_INSTANCE"/>
 *     &lt;enumeration value="BILLING_METHOD"/>
 *     &lt;enumeration value="PRODUCT"/>
 *     &lt;enumeration value="PRODUCT_COMP_TYPE"/>
 *     &lt;enumeration value="FDL"/>
 *     &lt;enumeration value="UNIT_TRANSACTION"/>
 *     &lt;enumeration value="UNIT_TRANSACTION_SUMMARY"/>
 *     &lt;enumeration value="PRODUCT_PURCHASE"/>
 *     &lt;enumeration value="CURRENCY"/>
 *     &lt;enumeration value="TRANSACTION_LOG"/>
 *     &lt;enumeration value="PERMISSION"/>
 *     &lt;enumeration value="PROMOCODE"/>
 *     &lt;enumeration value="PURCHASED_UNITS"/>
 *     &lt;enumeration value="REFERRAL_PROMOCODE"/>
 *     &lt;enumeration value="IMAGEINSTANCE"/>
 *     &lt;enumeration value="FETCH_RESOURCE"/>
 *     &lt;enumeration value="MEASUREMENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "resourceType")
@XmlEnum
public enum ResourceType {

    CLUSTER,
    CUSTOMER,
    USER,
    DISK,
    SNAPSHOT,
    FIREWALL,
    NIC,
    IMAGE,
    SERVER,
    SUBNET,
    VDC,
    NETWORK,
    SSHKEY,
    JOB,
    PRODUCTOFFER,
    FIREWALL_TEMPLATE,
    BILLING_ENTITY,
    GROUP,
    ANY,
    PAYMENTCARD,
    PAYMENTMETHOD,
    INVOICE,
    PROMOTION,
    REFERRAL_PROMOTION,
    DEPLOYMENT_TEMPLATE,
    DEPLOYMENT_INSTANCE,
    BILLING_METHOD,
    PRODUCT,
    PRODUCT_COMP_TYPE,
    FDL,
    UNIT_TRANSACTION,
    UNIT_TRANSACTION_SUMMARY,
    PRODUCT_PURCHASE,
    CURRENCY,
    TRANSACTION_LOG,
    PERMISSION,
    PROMOCODE,
    PURCHASED_UNITS,
    REFERRAL_PROMOCODE,
    IMAGEINSTANCE,
    FETCH_RESOURCE,
    MEASUREMENT;

    public String value() {
        return name();
    }

    public static ResourceType fromValue(String v) {
        return valueOf(v);
    }

}
