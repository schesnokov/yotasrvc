package com.yota.tariffsrvc.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accId")
    private long id;

    @Column(name = "active")
    private int active;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "minutesPackageId")
    private MinutesPackage activeMinutesPackage;

    @Column(name = "minutesExpiration")
    private LocalDate minutesExpiration;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gigabytesPackageId")
    private GigabytePackage activeGigabytesPackage;

    @Column(name = "gigabytesExpiration")
    private LocalDate gigabytesExpiration;
}