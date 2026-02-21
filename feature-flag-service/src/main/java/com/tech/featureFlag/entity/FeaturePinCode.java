package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "feature_pincode")
public class FeaturePinCode extends BaseEntity{


    @JoinColumn(name = "feature_id")
    @ManyToOne
    private Feature feature;

    @ManyToOne
    @JoinColumn(name = "pincode_master_id")
    private PinCodeMaster pinCodeMaster;

    @Column(name = "is_active")
    private Boolean isActive;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public PinCodeMaster getPinCodeMaster() {
        return pinCodeMaster;
    }

    public void setPinCodeMaster(PinCodeMaster pinCodeMaster) {
        this.pinCodeMaster = pinCodeMaster;
    }
}
