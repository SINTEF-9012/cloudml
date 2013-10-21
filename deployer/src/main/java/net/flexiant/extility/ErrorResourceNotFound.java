
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for errorResourceNotFound.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="errorResourceNotFound">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NO_CUSTOMER_FOUND"/>
 *     &lt;enumeration value="NO_USER_FOUND"/>
 *     &lt;enumeration value="NO_BILLING_ENTITY_FOUND"/>
 *     &lt;enumeration value="NO_PRODUCT_OFFER_FOUND"/>
 *     &lt;enumeration value="NO_PRODUCT_PURCHASE_FOUND"/>
 *     &lt;enumeration value="NO_UNIT_TRANSACTION_FOUND"/>
 *     &lt;enumeration value="NO_INVOICE_FOUND"/>
 *     &lt;enumeration value="NO_PRODUCT_FOUND"/>
 *     &lt;enumeration value="NO_PERIOD_FOUND"/>
 *     &lt;enumeration value="NO_PROMOTION_FOUND"/>
 *     &lt;enumeration value="NO_PROMOTION_CODE_FOUND"/>
 *     &lt;enumeration value="NO_CURRENCY_FOUND"/>
 *     &lt;enumeration value="NO_PROMOCODE_FOUND"/>
 *     &lt;enumeration value="NO_REFERRAL_SCHEME_FOUND"/>
 *     &lt;enumeration value="NO_REFERRAL_PROMO_CODE_FOUND"/>
 *     &lt;enumeration value="NO_UNIT_BALANCE_FOUND"/>
 *     &lt;enumeration value="NO_VDC_FOUND"/>
 *     &lt;enumeration value="NO_IMAGE_FOUND"/>
 *     &lt;enumeration value="NO_IMAGE_PERMISSION_FOUND"/>
 *     &lt;enumeration value="NO_JOB_FOUND"/>
 *     &lt;enumeration value="NO_SERVER_FOUND"/>
 *     &lt;enumeration value="NO_RESOURCE_FOUND"/>
 *     &lt;enumeration value="NO_DISK_FOUND"/>
 *     &lt;enumeration value="NO_SERVER_DETAILS_FOUND"/>
 *     &lt;enumeration value="FIREWALL_NOT_FOUND"/>
 *     &lt;enumeration value="BOOT_DISK_NOT_FOUND"/>
 *     &lt;enumeration value="NO_NIC_FOUND"/>
 *     &lt;enumeration value="NO_SNAPSHOT_FOUND"/>
 *     &lt;enumeration value="NO_NETWORK_FOUND"/>
 *     &lt;enumeration value="NO_SUBNET_FOUND"/>
 *     &lt;enumeration value="NO_FIREWALL_FOUND"/>
 *     &lt;enumeration value="NO_PRODUCT_COMPONENT_FOUND"/>
 *     &lt;enumeration value="NO_SSH_KEY_FOUND"/>
 *     &lt;enumeration value="NO_RESOURCE_KEY_FOUND"/>
 *     &lt;enumeration value="NO_RESOURCE_UUID_FOUND"/>
 *     &lt;enumeration value="NO_FETCH_PARAMS_FOUND"/>
 *     &lt;enumeration value="NO_CLUSTER_FOUND"/>
 *     &lt;enumeration value="NO_FIREWALL_TEMPLATE_FOUND"/>
 *     &lt;enumeration value="NO_PRODUCT_OFFER_UUID_FOUND"/>
 *     &lt;enumeration value="NO_SNAPSHOT_UUID_FOUND"/>
 *     &lt;enumeration value="NO_FIREWALL_UUID_FOUND"/>
 *     &lt;enumeration value="NO_IMAGE_UUID_FOUND"/>
 *     &lt;enumeration value="NO_NIC_UUID_FOUND"/>
 *     &lt;enumeration value="NO_SERVER_UUID_FOUND"/>
 *     &lt;enumeration value="NO_SUBNET_UUID_FOUND"/>
 *     &lt;enumeration value="NO_VDC_UUID_FOUND"/>
 *     &lt;enumeration value="NO_NETWORK_UUID_FOUND"/>
 *     &lt;enumeration value="NO_CLUSTER_UUID_FOUND"/>
 *     &lt;enumeration value="NO_JOB_UUID_FOUND"/>
 *     &lt;enumeration value="NO_FIREWALL_TEMPLATE_UUID_FOUND"/>
 *     &lt;enumeration value="NO_PAYMENT_CARD_FOUND"/>
 *     &lt;enumeration value="NO_GROUP_FOUND"/>
 *     &lt;enumeration value="NO_IP_FOUND"/>
 *     &lt;enumeration value="NO_STORAGE_UNIT_FOUND"/>
 *     &lt;enumeration value="NO_PAYMENT_METHOD_FOUND"/>
 *     &lt;enumeration value="NO_UNLOCKED_USER_FOUND"/>
 *     &lt;enumeration value="NO_DEPLOYMENT_TEMPLATE_FOUND"/>
 *     &lt;enumeration value="NO_DEPLOYMENT_INSTANCE_FOUND"/>
 *     &lt;enumeration value="NO_TEMPLATE_PROTECTION_PERMISSION_FOUND"/>
 *     &lt;enumeration value="NO_FDL_CODE_BLOCK_FOUND"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "errorResourceNotFound")
@XmlEnum
public enum ErrorResourceNotFound {

    NO_CUSTOMER_FOUND,
    NO_USER_FOUND,
    NO_BILLING_ENTITY_FOUND,
    NO_PRODUCT_OFFER_FOUND,
    NO_PRODUCT_PURCHASE_FOUND,
    NO_UNIT_TRANSACTION_FOUND,
    NO_INVOICE_FOUND,
    NO_PRODUCT_FOUND,
    NO_PERIOD_FOUND,
    NO_PROMOTION_FOUND,
    NO_PROMOTION_CODE_FOUND,
    NO_CURRENCY_FOUND,
    NO_PROMOCODE_FOUND,
    NO_REFERRAL_SCHEME_FOUND,
    NO_REFERRAL_PROMO_CODE_FOUND,
    NO_UNIT_BALANCE_FOUND,
    NO_VDC_FOUND,
    NO_IMAGE_FOUND,
    NO_IMAGE_PERMISSION_FOUND,
    NO_JOB_FOUND,
    NO_SERVER_FOUND,
    NO_RESOURCE_FOUND,
    NO_DISK_FOUND,
    NO_SERVER_DETAILS_FOUND,
    FIREWALL_NOT_FOUND,
    BOOT_DISK_NOT_FOUND,
    NO_NIC_FOUND,
    NO_SNAPSHOT_FOUND,
    NO_NETWORK_FOUND,
    NO_SUBNET_FOUND,
    NO_FIREWALL_FOUND,
    NO_PRODUCT_COMPONENT_FOUND,
    NO_SSH_KEY_FOUND,
    NO_RESOURCE_KEY_FOUND,
    NO_RESOURCE_UUID_FOUND,
    NO_FETCH_PARAMS_FOUND,
    NO_CLUSTER_FOUND,
    NO_FIREWALL_TEMPLATE_FOUND,
    NO_PRODUCT_OFFER_UUID_FOUND,
    NO_SNAPSHOT_UUID_FOUND,
    NO_FIREWALL_UUID_FOUND,
    NO_IMAGE_UUID_FOUND,
    NO_NIC_UUID_FOUND,
    NO_SERVER_UUID_FOUND,
    NO_SUBNET_UUID_FOUND,
    NO_VDC_UUID_FOUND,
    NO_NETWORK_UUID_FOUND,
    NO_CLUSTER_UUID_FOUND,
    NO_JOB_UUID_FOUND,
    NO_FIREWALL_TEMPLATE_UUID_FOUND,
    NO_PAYMENT_CARD_FOUND,
    NO_GROUP_FOUND,
    NO_IP_FOUND,
    NO_STORAGE_UNIT_FOUND,
    NO_PAYMENT_METHOD_FOUND,
    NO_UNLOCKED_USER_FOUND,
    NO_DEPLOYMENT_TEMPLATE_FOUND,
    NO_DEPLOYMENT_INSTANCE_FOUND,
    NO_TEMPLATE_PROTECTION_PERMISSION_FOUND,
    NO_FDL_CODE_BLOCK_FOUND;

    public String value() {
        return name();
    }

    public static ErrorResourceNotFound fromValue(String v) {
        return valueOf(v);
    }

}
