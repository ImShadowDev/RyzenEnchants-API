package dev.imshadow.ryzenenchants.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Fired when a personal or global multiplier is added, set, or removed.
 */
public class MultiplierChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public enum ChangeType { SET, ADD, REMOVE }
    public enum Scope { PERSONAL, GLOBAL }

    private final UUID playerUuid;
    private final Scope scope;
    private final ChangeType changeType;
    private final double oldValue;
    private final double newValue;

    public MultiplierChangeEvent(UUID playerUuid, Scope scope, ChangeType changeType,
                                  double oldValue, double newValue) {
        this.playerUuid = playerUuid;
        this.scope = scope;
        this.changeType = changeType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Scope getScope() {
        return scope;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public double getOldValue() {
        return oldValue;
    }

    public double getNewValue() {
        return newValue;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
