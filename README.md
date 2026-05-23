<div align="center">

<img src="https://img.shields.io/badge/Java-8+-orange?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Spigot-1.8.8+-yellow?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Version-1.0.0-5865F2?style=for-the-badge"/>
<img src="https://img.shields.io/badge/License-Commercial-e74c3c?style=for-the-badge"/>

# RyzenEnchants API

Hook into pickaxe data, tokens, enchants, and multipliers from your own plugin — without touching internals.

</div>

---

## Requirements

| Dependency | Required |
|---|---|
| [WorldGuard](https://dev.bukkit.org/projects/worldguard) | ✅ Required |
| [NBTAPI](https://www.spigotmc.org/resources/item-nbt-api.7939/) | ✅ Required |
| [Vault](https://www.spigotmc.org/resources/vault.34315/) | ⚠ Optional (economy features) |
| [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) | ⚠ Optional |

---

## Installation

The API jar is `compileOnly`. It should **never** end up shaded into your plugin — the full RyzenEnchants plugin handles everything at runtime.

**Gradle**
```groovy
dependencies {
    compileOnly files('libs/RyzenEnchants-API-1.0.0.jar')
}
```

**Maven**
```xml
<dependency>
    <groupId>dev.imshadow</groupId>
    <artifactId>RyzenEnchants-API</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

In your `plugin.yml`, declare the dependency. Use `soft-depend` if your plugin can run without RyzenEnchants, or `depend` if it can't:

```yaml
soft-depend: [RyzenEnchants]
```

---

## Getting Started

Everything goes through `RyzenEnchantsProvider`. If you're using `soft-depend`, always check `isAvailable()` first — calling `get()` when the plugin isn't loaded throws `IllegalStateException`.

```java
public class MyPlugin extends JavaPlugin {

    private RyzenEnchantsAPI api = null;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("RyzenEnchants")) {
            api = RyzenEnchantsProvider.get();
            getLogger().info("RyzenEnchants found. Integration enabled.");
        } else {
            getLogger().warning("RyzenEnchants not found. Integration disabled.");
        }
    }
}
```

One object, and you have access to everything.

---

## Pickaxe

```java
// Get pickaxe data by player or UUID
ApiPickaxeData data = api.getPickaxe(player);
ApiPickaxeData data = api.getPickaxe(uuid);

// Check if an item is a RyzenEnchants pickaxe
boolean isRyzen = api.isRyzenPickaxe(item);

// Give a pickaxe to a player
api.givePickaxe(player);

// Read data
data.getOwner();           // UUID
data.getOwnerName();       // Last known username
data.getLevel();           // Pickaxe level (starts at 1)
data.getEnergy();          // Current energy
data.getDurability();      // Current durability
data.getSlotsMax();        // Total enchant slots
data.getSlotsUsed();       // Slots currently in use

// Enchants on the pickaxe
data.hasEnchant("fortune");          // true/false
data.getEnchantLevel("fortune");     // 0 if not unlocked
data.getEnchants();                  // Map<String, Integer> — unmodifiable
```

---

## Tokens

```java
// Read balance
long tokens = api.getTokens(player);
long tokens = api.getTokens(uuid);

// Modify balance
api.giveTokens(player, 5000L);
api.setTokens(player, 10000L);

// Take tokens — returns false if the player doesn't have enough
boolean success = api.takeTokens(player, 1000L);
```

---

## Enchants

```java
// List all registered enchants
Collection<ApiEnchantDefinition> enchants = api.getEnchants();

// Get a specific enchant by internal name (lowercase)
ApiEnchantDefinition def = api.getEnchant("fortune");

if (def != null) {
    def.getName();              // "fortune"
    def.getDisplayName();       // Color-coded display name
    def.isEnabled();            // Whether it's globally active
    def.getMaxLevel();          // Max level cap
    def.getTriggerType();       // "MINE_BLOCK", "HELD", etc.
    def.getChance(50);          // Activation chance at level 50
    def.calculateCost(10, 5);   // Cost to go from level 10 to level 15
    def.isTogglable();          // Whether players can toggle it
    def.getMinPickaxeLevel();   // Required pickaxe level (0 = none)
}

// Read or write a player's enchant level
int level = api.getEnchantLevel(uuid, "fortune");
api.setEnchantLevel(uuid, "fortune", 25); // 0 removes the enchant
```

---

## Multipliers

```java
// Effective multiplier = personal × global
double effective = api.getEffectiveMultiplier(player);

// Individual values
double personal = api.getPersonalMultiplier(uuid);
double global   = api.getGlobalMultiplier();

// Set personal multiplier (expiryMs = System.currentTimeMillis() + duration, or 0 for permanent)
api.setPersonalMultiplier(uuid, 1.5, 0L);                               // permanent
api.setPersonalMultiplier(uuid, 2.0, System.currentTimeMillis() + 3600000L); // 1 hour

// Add to personal multiplier
api.addPersonalMultiplier(uuid, 0.5, System.currentTimeMillis() + 1800000L);

// Remove personal multiplier (resets to 1.0)
api.removePersonalMultiplier(uuid);

// Global multiplier
api.setGlobalMultiplier(1.5, System.currentTimeMillis() + 7200000L); // 2 hours
api.removeGlobalMultiplier();
```

---

## Events

All events are in `dev.imshadow.ryzenenchants.api.event`.

### PickaxeEnchantUpgradeEvent

Fired before a player upgrades an enchant. Cancelling prevents the upgrade and token deduction.

```java
@EventHandler
public void onUpgrade(PickaxeEnchantUpgradeEvent event) {
    Player player = event.getPlayer();
    String enchant = event.getEnchantName();
    int from  = event.getPreviousLevel();
    int to    = event.getNewLevel();
    long cost = event.getCost();

    // Prevent upgrading fortune above level 50
    if (enchant.equals("fortune") && to > 50) {
        event.setCancelled(true);
        player.sendMessage("Fortune cannot exceed level 50 on this server.");
    }
}
```

### PickaxeBlockSellEvent

Fired before AutoSell processes a block sale. Cancelling skips the sale. The payout can be modified.

```java
@EventHandler
public void onSell(PickaxeBlockSellEvent event) {
    // Double payout for VIP players
    if (event.getPlayer().hasPermission("myplugin.vip")) {
        event.setAmount(event.getAmount() * 2);
    }

    // Cancel sale for a specific block type
    if (event.getBlockType().equals("DIRT")) {
        event.setCancelled(true);
    }
}
```

### MultiplierChangeEvent

Fired when any personal or global multiplier is added, set, or removed.

```java
@EventHandler
public void onMultiplierChange(MultiplierChangeEvent event) {
    if (event.getScope() == MultiplierChangeEvent.Scope.GLOBAL) {
        Bukkit.broadcastMessage("Global multiplier changed: "
            + event.getOldValue() + "x → " + event.getNewValue() + "x");
    }
}
```

| Field | Description |
|---|---|
| `getPlayerUuid()` | UUID of the affected player, or `null` for global changes |
| `getScope()` | `PERSONAL` or `GLOBAL` |
| `getChangeType()` | `SET`, `ADD`, or `REMOVE` |
| `getOldValue()` | Multiplier before the change |
| `getNewValue()` | Multiplier after the change |

---

## Examples

**Reward a player with tokens when they deal damage**

```java
@EventHandler
public void onDamage(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player)) return;
    Player attacker = (Player) event.getDamager();

    if (!RyzenEnchantsProvider.isAvailable()) return;
    RyzenEnchantsProvider.get().giveTokens(attacker, 10L);
}
```

**Check if a player has a specific enchant before applying a bonus**

```java
public boolean hasFortune(Player player) {
    if (!RyzenEnchantsProvider.isAvailable()) return false;
    return RyzenEnchantsProvider.get().getEnchantLevel(player.getUniqueId(), "fortune") > 0;
}
```

**Display pickaxe stats in a scoreboard**

```java
public void updateScoreboard(Player player) {
    if (!RyzenEnchantsProvider.isAvailable()) return;
    RyzenEnchantsAPI api = RyzenEnchantsProvider.get();

    ApiPickaxeData data = api.getPickaxe(player);
    if (data == null) return;

    // Use data.getLevel(), data.getEnergy(), data.getTokens(), etc.
}
```

---

## License

RyzenEnchants is proprietary software. Each license is bound to a single server and validated online at startup.

Redistribution, resale, decompilation, or modification without explicit written permission from the author is strictly prohibited.

Purchase your license at **[discord.gg/RKu7vfwtRW](https://discord.gg/RKu7vfwtRW)**

---

<div align="center">

Made by **ImShadow**

</div>
