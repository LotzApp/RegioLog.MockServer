package org.lotzapp.util;

public final class TimeUtils {
    public static final String LONG_LOAD_KEY = "Very-Long-Loading-Duration";
    public static final String LOADING_TIMEOUT_KEY = "Loading-Timeout";

    private static final long DEFAULT_LOADING_LATENCY = 200;
    private static final int LONG_LOADING_LATENCY = 50;
    private static final long LOADING_TIMEOUT_LATENCY = Long.MAX_VALUE;

    public static void sleepForSeconds(long seconds) {
        sleepForMills(seconds * 1_000);
    }

    public static void sleepForMills(long millis) {
        long targetTime = System.currentTimeMillis() + millis;
        while(System.currentTimeMillis() < targetTime) {
            // do nothing
        }
    }

    public static void handleSpecialNames(String name) {
        switch (name) {
            case LONG_LOAD_KEY -> sleepForSeconds(LONG_LOADING_LATENCY);
            case LOADING_TIMEOUT_KEY -> sleepForSeconds(LOADING_TIMEOUT_LATENCY);
            default -> sleepForMills(DEFAULT_LOADING_LATENCY);
        }
    }
}
