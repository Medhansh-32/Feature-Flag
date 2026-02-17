package com.tech.featureFlag.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pincode_master")
public class PinCodeMaster extends BaseEntity{

    @Column(name = "pincode")
    private String pinCode;
    private String city;
    private String state;
    private String region;
    private boolean active;
}

