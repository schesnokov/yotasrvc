package com.yota.tariffsrvc.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Gigabytes")
public class GigabytePackageEntity {

    @Id
    @GeneratedValue
    private long id;
}
