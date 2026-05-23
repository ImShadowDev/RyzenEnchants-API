package dev.imshadow.ryzenenchants.api;

import dev.imshadow.ryzenenchants.api.enchant.ApiEnchantDefinition;
import dev.imshadow.ryzenenchants.api.pickaxe.ApiPickaxeData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

public interface RyzenEnchantsAPI {

    ApiPickaxeData getPickaxe(UUID uuid);

    ApiPickaxeData getPickaxe(Player player);

    boolean isRyzenPickaxe(ItemStack item);

    void givePickaxe(Player player);

    long getTokens(Player player);

    long getTokens(UUID uuid);

    void giveTokens(Player player, long amount);

    boolean takeTokens(Player player, long amount);

    void setTokens(Player player, long amount);

    Collection<ApiEnchantDefinition> getEnchants();

    ApiEnchantDefinition getEnchant(String name);

    int getEnchantLevel(UUID uuid, String enchantName);

    void setEnchantLevel(UUID uuid, String enchantName, int level);

    double getEffectiveMultiplier(Player player);

    double getPersonalMultiplier(UUID uuid);

    double getGlobalMultiplier();

    void setPersonalMultiplier(UUID uuid, double value, long expiryMs);

    void addPersonalMultiplier(UUID uuid, double value, long expiryMs);

    void removePersonalMultiplier(UUID uuid);

    void setGlobalMultiplier(double value, long expiryMs);

    void removeGlobalMultiplier();
}
