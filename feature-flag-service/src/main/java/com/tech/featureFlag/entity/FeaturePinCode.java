package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feature_pincode")
public class FeaturePinCode extends BaseEntity{


    @JoinColumn(name = "feature_id")
    @ManyToOne
    private Feature feature;

    @ManyToOne
    @JoinColumn(name = "pincode_master_id")
    private PinCodeMaster pinCodeMaster;

}