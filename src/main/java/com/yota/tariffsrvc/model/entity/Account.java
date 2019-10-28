package com.yota.tariffsrvc.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Chesnokov Sergei
 *
 * Entity represents sim card
 */
@Entity
@Table(name = "Account")
@JsonSerialize
public class Account implements Serializable {

    private static final long serialVersionUID = -1709128766123132067L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accId")
    private long id;

    //Phone number
    @Column(name = "phoneNumber")
    private String phoneNumber;

    //Account status indicator
    @Column(name = "active")
    private Boolean active;

    //Package with minutes
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "minutesPackageId")
    private MinutesPackage activeMinutesPackage;

    //Package with gigabytes
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gigabytesPackageId")
    private GigabytesPackage activeGigabytesPackage;

    public Account() {
    }

    public Account(String phoneNumber, Boolean active, MinutesPackage activeMinutesPackage, GigabytesPackage activeGigabytesPackage) {
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.activeMinutesPackage = activeMinutesPackage;
        this.activeGigabytesPackage = activeGigabytesPackage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public MinutesPackage getActiveMinutesPackage() {
        return activeMinutesPackage;
    }

    public void setActiveMinutesPackage(MinutesPackage activeMinutesPackage) {
        this.activeMinutesPackage = activeMinutesPackage;
    }

    public GigabytesPackage getActiveGigabytesPackage() {
        return activeGigabytesPackage;
    }

    public void setActiveGigabytesPackage(GigabytesPackage activeGigabytesPackage) {
        this.activeGigabytesPackage = activeGigabytesPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(phoneNumber, account.phoneNumber) &&
                Objects.equals(active, account.active) &&
                Objects.equals(activeMinutesPackage, account.activeMinutesPackage) &&
                Objects.equals(activeGigabytesPackage, account.activeGigabytesPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, active, activeMinutesPackage, activeGigabytesPackage);
    }
}