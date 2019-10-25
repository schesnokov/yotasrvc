package com.yota.tariffsrvc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Account")
public class AccountEntity {

    @Id
    @GeneratedValue
    private long id;

    private int minutes;

    private double gigabytes;

    private int active;

    private List<MinutesPackageEntity> minutesPackage;

    private List<GigabytePackageEntity> gigabytePackage;

}
