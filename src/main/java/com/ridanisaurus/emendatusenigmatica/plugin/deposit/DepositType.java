package com.ridanisaurus.emendatusenigmatica.plugin.deposit;

public enum DepositType {
    VANILLA("emendatusenigmatica:vanilla_deposit"),
    SPHERE("emendatusenigmatica:sphere_deposit"),
    GEODE("emendatusenigmatica:geode_deposit"),
    DIKE("emendatusenigmatica:dike_deposit"),
    DENSE("emendatusenigmatica:dense_deposit"),
    TEST("emendatusenigmatica:test_deposit");

    private final String type;

    DepositType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
