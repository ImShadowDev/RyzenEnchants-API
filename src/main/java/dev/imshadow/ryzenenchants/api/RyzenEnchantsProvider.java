package dev.imshadow.ryzenenchants.api;

public final class RyzenEnchantsProvider {

    private static RyzenEnchantsAPI instance;

    private RyzenEnchantsProvider() {}

    public static RyzenEnchantsAPI get() {
        if (instance == null) {
            throw new IllegalStateException("RyzenEnchants API is not available. Is the plugin enabled?");
        }
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null;
    }

    public static void register(RyzenEnchantsAPI api) {
        instance = api;
    }

    public static void unregister() {
        instance = null;
    }
}
