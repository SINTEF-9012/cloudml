
package net.flexiant.extility;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for limits.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="limits">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MAX_VDCS"/>
 *     &lt;enumeration value="MAX_VLANS"/>
 *     &lt;enumeration value="MAX_SUBNETS"/>
 *     &lt;enumeration value="MAX_RAM"/>
 *     &lt;enumeration value="MAX_CPUS"/>
 *     &lt;enumeration value="MAX_SERVERS"/>
 *     &lt;enumeration value="MAX_DISKS"/>
 *     &lt;enumeration value="MAX_STORAGEGB"/>
 *     &lt;enumeration value="MAX_SNAPSHOTS"/>
 *     &lt;enumeration value="MAX_IMAGES"/>
 *     &lt;enumeration value="MAX_CUSTOMER_USERS"/>
 *     &lt;enumeration value="CREDIT_LIMIT"/>
 *     &lt;enumeration value="NO_3DS_28_DAY_SPEND_LIMIT"/>
 *     &lt;enumeration value="OVERALL_28_DAY_SPEND_LIMIT"/>
 *     &lt;enumeration value="CUTOFF_BALANCE"/>
 *     &lt;enumeration value="MAX_IPv4_ADDRESSES"/>
 *     &lt;enumeration value="MAX_IPv6_SUBNETS"/>
 *     &lt;enumeration value="MAX_NETWORK_PUBLIC"/>
 *     &lt;enumeration value="MAX_NETWORK_PRIVATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "limits")
@XmlEnum
public enum Limits {

    MAX_VDCS("MAX_VDCS"),
    MAX_VLANS("MAX_VLANS"),
    MAX_SUBNETS("MAX_SUBNETS"),
    MAX_RAM("MAX_RAM"),
    MAX_CPUS("MAX_CPUS"),
    MAX_SERVERS("MAX_SERVERS"),
    MAX_DISKS("MAX_DISKS"),
    MAX_STORAGEGB("MAX_STORAGEGB"),
    MAX_SNAPSHOTS("MAX_SNAPSHOTS"),
    MAX_IMAGES("MAX_IMAGES"),
    MAX_CUSTOMER_USERS("MAX_CUSTOMER_USERS"),
    CREDIT_LIMIT("CREDIT_LIMIT"),
    @XmlEnumValue("NO_3DS_28_DAY_SPEND_LIMIT")
    NO_3_DS_28_DAY_SPEND_LIMIT("NO_3DS_28_DAY_SPEND_LIMIT"),
    OVERALL_28_DAY_SPEND_LIMIT("OVERALL_28_DAY_SPEND_LIMIT"),
    CUTOFF_BALANCE("CUTOFF_BALANCE"),
    @XmlEnumValue("MAX_IPv4_ADDRESSES")
    MAX_I_PV_4_ADDRESSES("MAX_IPv4_ADDRESSES"),
    @XmlEnumValue("MAX_IPv6_SUBNETS")
    MAX_I_PV_6_SUBNETS("MAX_IPv6_SUBNETS"),
    MAX_NETWORK_PUBLIC("MAX_NETWORK_PUBLIC"),
    MAX_NETWORK_PRIVATE("MAX_NETWORK_PRIVATE");
    private final String value;

    Limits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Limits fromValue(String v) {
        for (Limits c: Limits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
