package com.ridanisaurus.emendatusenigmatica.plugin.deposit;

import org.jetbrains.annotations.Nullable;

public enum DepositType {
    VANILLA ("emendatusenigmatica:vanilla_deposit"),
    SPHERE  ("emendatusenigmatica:sphere_deposit"),
    GEODE   ("emendatusenigmatica:geode_deposit"),
    DIKE    ("emendatusenigmatica:dike_deposit"),
    DENSE   ("emendatusenigmatica:dense_deposit");

    private final String type;

    DepositType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static @Nullable DepositType typeOf(String value) {
        for (DepositType depositType : DepositType.values()) {
            if (depositType.getType().equals(value)) return depositType;
        }
        return null;
    }
}
