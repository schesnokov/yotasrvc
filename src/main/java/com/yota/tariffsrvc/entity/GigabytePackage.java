package com.yota.tariffsrvc.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "Gigabytes")
public class GigabytePackage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "packageName")
    private String packageName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "daysCount")
    private int daysCount;

    @OneToMany(mappedBy = "activeGigabytesPackage")
    private Set<Account> accounts;
}
