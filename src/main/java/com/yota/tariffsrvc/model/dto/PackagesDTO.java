package com.yota.tariffsrvc.model.dto;

import com.yota.tariffsrvc.model.entity.GigabytesPackage;
import com.yota.tariffsrvc.model.entity.MinutesPackage;

import java.io.Serializable;

/**
 * @author Chesnokov Sergei
 *
 * DTO for serialize and de-serialize Json with 2 packages
 */
public class PackagesDTO implements Serializable {

    private MinutesPackage minutesPackage;
    private GigabytesPackage gigabytesPackage;

    public PackagesDTO() {
    }

    public PackagesDTO(MinutesPackage minutesPackage, GigabytesPackage gigabytesPackage) {
        this.minutesPackage = minutesPackage;
        this.gigabytesPackage = gigabytesPackage;
    }

    public PackagesDTO(MinutesPackage minutesPackage) {
        this(minutesPackage, null);
    }

    public PackagesDTO(GigabytesPackage gigabytesPackage) {
        this(null, gigabytesPackage);
    }

    public MinutesPackage getMinutesPackage() {
        return minutesPackage;
    }

    public void setMinutesPackage(MinutesPackage minutesPackage) {
        this.minutesPackage = minutesPackage;
    }

    public GigabytesPackage getGigabytesPackage() {
        return gigabytesPackage;
    }

    public void setGigabytesPackage(GigabytesPackage gigabytesPackage) {
        this.gigabytesPackage = gigabytesPackage;
    }
}
