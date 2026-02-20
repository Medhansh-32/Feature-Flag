package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "feature")
public class Feature extends BaseEntity{


    @Column(name = "name")
    private String featureName;

    @Column(name = "description")
    private String description;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "rollout_percentage")
    private double rolloutPercentage;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<FeaturePinCode> pinCodes;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<FeaturePlan> plans;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<FeatureRole> roles;

    public String getFeatureName() {
        return featureName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getRolloutPercentage() {
        return rolloutPercentage;
    }

    public List<FeaturePinCode> getPinCodes() {
        return pinCodes;
    }

    public List<FeaturePlan> getPlans() {
        return plans;
    }

    public List<FeatureRole> getRoles() {
        return roles;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRolloutPercentage(double rolloutPercentage) {
        this.rolloutPercentage = rolloutPercentage;
    }

    public void setPinCodes(List<FeaturePinCode> pinCodes) {
        this.pinCodes = pinCodes;
    }

    public void setPlans(List<FeaturePlan> plans) {
        this.plans = plans;
    }

    public void setRoles(List<FeatureRole> roles) {
        this.roles = roles;
    }
}
