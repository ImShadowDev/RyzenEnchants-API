package dev.imshadow.ryzenenchants.api.enchant;

public interface ApiEnchantDefinition {

    String getName();

    String getDisplayName();

    boolean isEnabled();

    int getMaxLevel();

    String getTriggerType();

    double getChance(int level);

    long calculateCost(int currentLevel, int levels);

    boolean isTogglable();

    int getMinPickaxeLevel();
}
