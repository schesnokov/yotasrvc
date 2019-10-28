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
 * Entity represents minutes package
 */
@Entity
@Table(name = "Minutes")
@JsonSerialize
public class MinutesPackage extends BasePackage implements Serializable {

    private static final long serialVersionUID = -1723798766434132067L;

    //The account that owns the package
    @OneToOne(mappedBy = "activeMinutesPackage")
    @JsonIgnore
    private Account account;

    public MinutesPackage() {
    }

    public MinutesPackage(int amount, int dayCount, LocalDate expirationDate) {
        super(amount, dayCount, expirationDate);
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
        MinutesPackage that = (MinutesPackage) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account);
    }
}
