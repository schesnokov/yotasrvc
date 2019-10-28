package com.yota.tariffsrvc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Chesnokov Sergei
 *
 * Entity represents gigabytes package
 */
@Entity
@Table(name = "Gigabytes")
@JsonSerialize
public class GigabytesPackage extends BasePackage implements Serializable {

    private static final long serialVersionUID = -1723798766123132067L;

    //The account that owns the package
    @OneToOne(mappedBy = "activeGigabytesPackage")
    @JsonIgnore
    private Account account;

    public GigabytesPackage() {
    }

    public GigabytesPackage(int amount, int daysCount, LocalDate expirationDate) {
        super(amount, daysCount, expirationDate);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GigabytesPackage that = (GigabytesPackage) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account);
    }
}
