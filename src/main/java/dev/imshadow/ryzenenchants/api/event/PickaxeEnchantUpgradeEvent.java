package dev.imshadow.ryzenenchants.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PickaxeEnchantUpgradeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String enchantName;
    private final int previousLevel;
    private final int newLevel;
    private final long cost;
    private boolean cancelled;

    public PickaxeEnchantUpgradeEvent(Player player, String enchantName,
                                       int previousLevel, int newLevel, long cost) {
        super(player);
        this.enchantName = enchantName;
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
        this.cost = cost;
    }

    /** The internal name of the enchant being upgraded. */
    public String getEnchantName() {
        return enchantName;
    }

    /** The level before the upgrade. */
    public int getPreviousLevel() {
        return previousLevel;
    }

    /** The level after the upgrade. */
    public int getNewLevel() {
        return newLevel;
    }

    /** Total token cost of the upgrade. */
    public long getCost() {
        return cost;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
