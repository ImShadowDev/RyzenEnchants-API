package dev.imshadow.ryzenenchants.api.pickaxe;

import java.util.Map;
import java.util.UUID;

public interface ApiPickaxeData {

    UUID getOwner();

    String getOwnerName();

    int getLevel();

    double getEnergy();

    int getDurability();

    int getSlotsMax();

    int getSlotsUsed();

    int getEnchantLevel(String enchantName);

    boolean hasEnchant(String enchantName);

    Map<String, Integer> getEnchants();
}
