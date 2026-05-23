package dev.imshadow.ryzenenchants.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PickaxeBlockSellEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String blockType;
    private final int blockCount;
    private double amount;
    private boolean cancelled;

    public PickaxeBlockSellEvent(Player player, String blockType, int blockCount, double amount) {
        super(player);
        this.blockType = blockType;
        this.blockCount = blockCount;
        this.amount = amount;
    }

    /** Material name of the block being sold (e.g. "diamond_ore"). */
    public String getBlockType() {
        return blockType;
    }

    /** Number of blocks being sold in this batch. */
    public int getBlockCount() {
        return blockCount;
    }

    /** Total payout before multipliers. Can be modified. */
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
