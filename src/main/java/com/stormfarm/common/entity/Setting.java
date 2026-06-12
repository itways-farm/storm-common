package com.stormfarm.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settings")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Setting {
    @Id
    @Column(name = "config_key", nullable = false)
    private String configKey;

    @Column(name = "config_value")
    private String value;

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getConfigValue() { return value; }
    public void setConfigValue(String value) { this.value = value; }

    // Backward-compat aliases
    public String getKey() { return configKey; }
    public void setKey(String key) { this.configKey = key; }
}
