package cloud.dispatcher.midware.health.dashboard.config;

public final class GlobalConfigValue {

    private GlobalConfigValue() {}

    public static final String MONGO_COLLECTION_NAME_PREFIX = "spring_cloud_health_";

    public static final String METRICS_CREATE_TIME = "time";

    public static final String METRICS_APPLICATION_NAME = "application.name";

    public static final String METRICS_APPLICATION_HOST = "application.host";

    public static final String METRICS_APPLICATION_PORT = "application.port";
}
