
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for errorInternal.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="errorInternal">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FUNCTION_FAILED"/>
 *     &lt;enumeration value="FAILED_TO_SET_DISK_AS_BOOT_DISK"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_NETWORK"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_VLAN_ON_NETWORK"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_SUBNET"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_IMAGE"/>
 *     &lt;enumeration value="FAILED_TO_MAKE_PURCHASE"/>
 *     &lt;enumeration value="FAILED_TO_CANCEL_JOB"/>
 *     &lt;enumeration value="FAILED_TO_DETACH_DISK_FROM_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_DISK"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_NIC"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_FIREWALL"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_IMAGE"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_NETWORK"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_NIC"/>
 *     &lt;enumeration value="FAILED_TO_DELETE_SUBNET"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_FETCH_JOB"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_RESOURCE"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_ATTACH_DISK_TO_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_ATTACH_NIC_TO_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_JOB"/>
 *     &lt;enumeration value="FAILED_TO_DETACH_NIC_FROM_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_CUSTOMER"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_UNIT_BALANCE"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_PRODUCT_OFFER"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_USER"/>
 *     &lt;enumeration value="FAILED_TO_DEACTIVATE_PRODUCT_PURCHASE"/>
 *     &lt;enumeration value="FAILED_TO_FETCH_RESOURCE_JOB"/>
 *     &lt;enumeration value="FAILED_TO_FETCH_RESOURCE"/>
 *     &lt;enumeration value="FAILED_TO_FETCH_JOB"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_VDC"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_DISK"/>
 *     &lt;enumeration value="FAILED_TO_SET_BOOT_DISK"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_SNAPSHOT"/>
 *     &lt;enumeration value="FAILED_TO_GET_BOOT_DISK"/>
 *     &lt;enumeration value="FAILED_TO_MOVE_SUBNET_TO_NETWORK"/>
 *     &lt;enumeration value="FAILED_TO_CHANGE_NETWORK_FOR_NIC"/>
 *     &lt;enumeration value="FAILED_TO_ADD_IP_TO_NIC"/>
 *     &lt;enumeration value="FAILED_TO_RESIZE_DISK"/>
 *     &lt;enumeration value="FAILED_TO_UPDATE_SERVER"/>
 *     &lt;enumeration value="FAILED_TO_REMOVE_IP_FROM_NIC"/>
 *     &lt;enumeration value="FAILED_TO_MODIFY_FIREWALL"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_FIREWALL"/>
 *     &lt;enumeration value="FAILED_TO_LOAD_PERMISSIONS"/>
 *     &lt;enumeration value="FAILED_TO_STORE_CARD_DETAILS"/>
 *     &lt;enumeration value="FAILED_TO_LOAD_JOB"/>
 *     &lt;enumeration value="FQL_EXCEPTION"/>
 *     &lt;enumeration value="FAILED_TO_UNMARSHAL"/>
 *     &lt;enumeration value="FAILED_TO_CREATE_DEPLOYMENT_TEMPLATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "errorInternal")
@XmlEnum
public enum ErrorInternal {

    FUNCTION_FAILED,
    FAILED_TO_SET_DISK_AS_BOOT_DISK,
    FAILED_TO_CREATE_NETWORK,
    FAILED_TO_CREATE_VLAN_ON_NETWORK,
    FAILED_TO_CREATE_SUBNET,
    FAILED_TO_CREATE_IMAGE,
    FAILED_TO_MAKE_PURCHASE,
    FAILED_TO_CANCEL_JOB,
    FAILED_TO_DETACH_DISK_FROM_SERVER,
    FAILED_TO_DELETE_DISK,
    FAILED_TO_CREATE_NIC,
    FAILED_TO_DELETE_FIREWALL,
    FAILED_TO_DELETE_SERVER,
    FAILED_TO_DELETE_IMAGE,
    FAILED_TO_DELETE_NETWORK,
    FAILED_TO_DELETE_NIC,
    FAILED_TO_DELETE_SUBNET,
    FAILED_TO_CREATE_FETCH_JOB,
    FAILED_TO_CREATE_RESOURCE,
    FAILED_TO_CREATE_SERVER,
    FAILED_TO_ATTACH_DISK_TO_SERVER,
    FAILED_TO_ATTACH_NIC_TO_SERVER,
    FAILED_TO_CREATE_JOB,
    FAILED_TO_DETACH_NIC_FROM_SERVER,
    FAILED_TO_CREATE_CUSTOMER,
    FAILED_TO_CREATE_UNIT_BALANCE,
    FAILED_TO_CREATE_PRODUCT_OFFER,
    FAILED_TO_CREATE_USER,
    FAILED_TO_DEACTIVATE_PRODUCT_PURCHASE,
    FAILED_TO_FETCH_RESOURCE_JOB,
    FAILED_TO_FETCH_RESOURCE,
    FAILED_TO_FETCH_JOB,
    FAILED_TO_CREATE_VDC,
    FAILED_TO_CREATE_DISK,
    FAILED_TO_SET_BOOT_DISK,
    FAILED_TO_CREATE_SNAPSHOT,
    FAILED_TO_GET_BOOT_DISK,
    FAILED_TO_MOVE_SUBNET_TO_NETWORK,
    FAILED_TO_CHANGE_NETWORK_FOR_NIC,
    FAILED_TO_ADD_IP_TO_NIC,
    FAILED_TO_RESIZE_DISK,
    FAILED_TO_UPDATE_SERVER,
    FAILED_TO_REMOVE_IP_FROM_NIC,
    FAILED_TO_MODIFY_FIREWALL,
    FAILED_TO_CREATE_FIREWALL,
    FAILED_TO_LOAD_PERMISSIONS,
    FAILED_TO_STORE_CARD_DETAILS,
    FAILED_TO_LOAD_JOB,
    FQL_EXCEPTION,
    FAILED_TO_UNMARSHAL,
    FAILED_TO_CREATE_DEPLOYMENT_TEMPLATE;

    public String value() {
        return name();
    }

    public static ErrorInternal fromValue(String v) {
        return valueOf(v);
    }

}
