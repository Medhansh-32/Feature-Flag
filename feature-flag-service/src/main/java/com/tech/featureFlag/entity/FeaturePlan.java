package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "feature_plan")
public class FeaturePlan extends BaseEntity{

    @JoinColumn(name = "feature_id")
    @ManyToOne
    private Feature feature;

    @Column(name = "plan_type", length = 50)
    private String planType;


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

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }
}