package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feature_role")
public class FeatureRole extends BaseEntity{

    @JoinColumn(name = "feature_id")
    @ManyToOne
    private Feature feature;

    @Column(name = "role_name", length = 50)
    private String roleName;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}