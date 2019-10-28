package com.yota.tariffsrvc.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Chesnokov Sergei
 *
 * SuperClass for Gigabytes and Minutes package entities
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasePackage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    //The amount of minutes or gigabytes
    @Column(name = "amount")
    private int amount;

    //The period of package validity
    @Column(name = "daysCount")
    private int daysCount;

    //The date of package expiration
    @Column(name = "expirationDate")
    private LocalDate expirationDate;

    public BasePackage() {
    }

    public BasePackage(int amount, int daysCount, LocalDate expirationDate) {
        this.amount = amount;
        this.daysCount = daysCount;
        this.expirationDate = expirationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate minutesExpiration) {
        this.expirationDate = minutesExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasePackage that = (BasePackage) o;
        return id == that.id &&
                amount == that.amount &&
                daysCount == that.daysCount &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, daysCount, expirationDate);
    }
}
