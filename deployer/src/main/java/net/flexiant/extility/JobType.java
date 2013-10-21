
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="jobType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CREATE_DISK"/>
 *     &lt;enumeration value="DELETE_DISK"/>
 *     &lt;enumeration value="ATTACH_DISK"/>
 *     &lt;enumeration value="DETACH_DISK"/>
 *     &lt;enumeration value="RESIZE_DISK"/>
 *     &lt;enumeration value="CLONE_DISK"/>
 *     &lt;enumeration value="RENAME_DISK"/>
 *     &lt;enumeration value="CREATE_SNAPSHOT"/>
 *     &lt;enumeration value="DELETE_SNAPSHOT"/>
 *     &lt;enumeration value="ADD_IP_TO_NIC"/>
 *     &lt;enumeration value="ATTACH_NIC"/>
 *     &lt;enumeration value="CREATE_NIC"/>
 *     &lt;enumeration value="DELETE_NIC"/>
 *     &lt;enumeration value="REMOVE_IP_FROM_NIC"/>
 *     &lt;enumeration value="CREATE_SERVER"/>
 *     &lt;enumeration value="DELETE_SERVER"/>
 *     &lt;enumeration value="MODIFY_SERVER"/>
 *     &lt;enumeration value="REVERT_SERVER"/>
 *     &lt;enumeration value="CREATE_SUBNET"/>
 *     &lt;enumeration value="MOVE_SUBNET"/>
 *     &lt;enumeration value="DELETE_SUBNET"/>
 *     &lt;enumeration value="CREATE_VDC"/>
 *     &lt;enumeration value="DELETE_VDC"/>
 *     &lt;enumeration value="CREATE_VLAN"/>
 *     &lt;enumeration value="DELETE_VLAN"/>
 *     &lt;enumeration value="CREATE_IMAGE_TEMPLATE"/>
 *     &lt;enumeration value="DELETE_IMAGE_TEMPLATE"/>
 *     &lt;enumeration value="START_SERVER"/>
 *     &lt;enumeration value="SHUTDOWN_SERVER"/>
 *     &lt;enumeration value="KILL_SERVER"/>
 *     &lt;enumeration value="REBOOT_SERVER"/>
 *     &lt;enumeration value="REVERT_DISK"/>
 *     &lt;enumeration value="CANCEL_JOB"/>
 *     &lt;enumeration value="SCHEDULED_JOB"/>
 *     &lt;enumeration value="MODIFY_DISK"/>
 *     &lt;enumeration value="DETACH_NIC"/>
 *     &lt;enumeration value="FETCH_DISK"/>
 *     &lt;enumeration value="FETCH_IMAGE"/>
 *     &lt;enumeration value="FIREWALL_CREATE"/>
 *     &lt;enumeration value="FIREWALL_RULE_ADD"/>
 *     &lt;enumeration value="FIREWALL_RULE_MODIFY"/>
 *     &lt;enumeration value="DELETE_FIREWALL_RULE"/>
 *     &lt;enumeration value="MODIFY_FIREWALL"/>
 *     &lt;enumeration value="DELETE_FIREWALL"/>
 *     &lt;enumeration value="MAKE_SERVER_VISIBLE"/>
 *     &lt;enumeration value="DISK_MIGRATE"/>
 *     &lt;enumeration value="CLONE_SERVER"/>
 *     &lt;enumeration value="FETCH_RESOURCE"/>
 *     &lt;enumeration value="CREATE_NETWORK"/>
 *     &lt;enumeration value="DELETE_NETWORK"/>
 *     &lt;enumeration value="MODIFY_RESOURCE"/>
 *     &lt;enumeration value="ATTACH_SSHKEY"/>
 *     &lt;enumeration value="DETACH_SSHKEY"/>
 *     &lt;enumeration value="ATTACH_SUBNET"/>
 *     &lt;enumeration value="DELETE_USER"/>
 *     &lt;enumeration value="DELETE_JOB"/>
 *     &lt;enumeration value="DELETE_PRODUCT_OFFER"/>
 *     &lt;enumeration value="CREATE_SSHKEY"/>
 *     &lt;enumeration value="DELETE_SSHKEY"/>
 *     &lt;enumeration value="CREATE_FIREWALL_TEMPLATE"/>
 *     &lt;enumeration value="MODIFY_FIREWALL_TEMPLATE"/>
 *     &lt;enumeration value="APPLY_FIREWALL_TEMPLATE"/>
 *     &lt;enumeration value="DELETE_FIREWALL_TEMPLATE"/>
 *     &lt;enumeration value="PUBLISH_IMAGE"/>
 *     &lt;enumeration value="REVOKE_IMAGE"/>
 *     &lt;enumeration value="MODIFY_SSHKEY"/>
 *     &lt;enumeration value="MODIFY_SNAPSHOT"/>
 *     &lt;enumeration value="MODIFY_NETWORK"/>
 *     &lt;enumeration value="MODIFY_SUBNET"/>
 *     &lt;enumeration value="MODIFY_NIC"/>
 *     &lt;enumeration value="MODIFY_VDC"/>
 *     &lt;enumeration value="MODIFY_IMAGE"/>
 *     &lt;enumeration value="CREATE_GROUP"/>
 *     &lt;enumeration value="MODIFY_GROUP"/>
 *     &lt;enumeration value="DELETE_GROUP"/>
 *     &lt;enumeration value="DELETE_PAYMENT_CARD"/>
 *     &lt;enumeration value="DEPLOY_TEMPLATE"/>
 *     &lt;enumeration value="CREATE_TEMPLATE"/>
 *     &lt;enumeration value="UPDATE_INSTANCE_STATE"/>
 *     &lt;enumeration value="MODIFY_TEMPLATE"/>
 *     &lt;enumeration value="MODIFY_TEMPLATE_INSTANCE"/>
 *     &lt;enumeration value="DELETE_TEMPLATE"/>
 *     &lt;enumeration value="DELETE_TEMPLATE_INSTANCE"/>
 *     &lt;enumeration value="PUBLISH_TEMPLATE"/>
 *     &lt;enumeration value="REVOKE_TEMPLATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "jobType")
@XmlEnum
public enum JobType {

    CREATE_DISK,
    DELETE_DISK,
    ATTACH_DISK,
    DETACH_DISK,
    RESIZE_DISK,
    CLONE_DISK,
    RENAME_DISK,
    CREATE_SNAPSHOT,
    DELETE_SNAPSHOT,
    ADD_IP_TO_NIC,
    ATTACH_NIC,
    CREATE_NIC,
    DELETE_NIC,
    REMOVE_IP_FROM_NIC,
    CREATE_SERVER,
    DELETE_SERVER,
    MODIFY_SERVER,
    REVERT_SERVER,
    CREATE_SUBNET,
    MOVE_SUBNET,
    DELETE_SUBNET,
    CREATE_VDC,
    DELETE_VDC,
    CREATE_VLAN,
    DELETE_VLAN,
    CREATE_IMAGE_TEMPLATE,
    DELETE_IMAGE_TEMPLATE,
    START_SERVER,
    SHUTDOWN_SERVER,
    KILL_SERVER,
    REBOOT_SERVER,
    REVERT_DISK,
    CANCEL_JOB,
    SCHEDULED_JOB,
    MODIFY_DISK,
    DETACH_NIC,
    FETCH_DISK,
    FETCH_IMAGE,
    FIREWALL_CREATE,
    FIREWALL_RULE_ADD,
    FIREWALL_RULE_MODIFY,
    DELETE_FIREWALL_RULE,
    MODIFY_FIREWALL,
    DELETE_FIREWALL,
    MAKE_SERVER_VISIBLE,
    DISK_MIGRATE,
    CLONE_SERVER,
    FETCH_RESOURCE,
    CREATE_NETWORK,
    DELETE_NETWORK,
    MODIFY_RESOURCE,
    ATTACH_SSHKEY,
    DETACH_SSHKEY,
    ATTACH_SUBNET,
    DELETE_USER,
    DELETE_JOB,
    DELETE_PRODUCT_OFFER,
    CREATE_SSHKEY,
    DELETE_SSHKEY,
    CREATE_FIREWALL_TEMPLATE,
    MODIFY_FIREWALL_TEMPLATE,
    APPLY_FIREWALL_TEMPLATE,
    DELETE_FIREWALL_TEMPLATE,
    PUBLISH_IMAGE,
    REVOKE_IMAGE,
    MODIFY_SSHKEY,
    MODIFY_SNAPSHOT,
    MODIFY_NETWORK,
    MODIFY_SUBNET,
    MODIFY_NIC,
    MODIFY_VDC,
    MODIFY_IMAGE,
    CREATE_GROUP,
    MODIFY_GROUP,
    DELETE_GROUP,
    DELETE_PAYMENT_CARD,
    DEPLOY_TEMPLATE,
    CREATE_TEMPLATE,
    UPDATE_INSTANCE_STATE,
    MODIFY_TEMPLATE,
    MODIFY_TEMPLATE_INSTANCE,
    DELETE_TEMPLATE,
    DELETE_TEMPLATE_INSTANCE,
    PUBLISH_TEMPLATE,
    REVOKE_TEMPLATE;

    public String value() {
        return name();
    }

    public static JobType fromValue(String v) {
        return valueOf(v);
    }

}
