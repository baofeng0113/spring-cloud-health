package cloud.dispatcher.midware.health.dashboard.config;

public final class GlobalConfigValue {

    private GlobalConfigValue() {}

    public static final String REST_CONTROLLER_BASE_PACKAGE = "cloud.dispatcher.midware.health.dashboard.web.api";

    public static final String METRICS_APP_NAME = "application.name";

    public static final String METRICS_APP_HOST = "application.host";

    public static final String METRICS_APP_PORT = "application.port";

    public static final String MONGO_COLLECTION_NAME_PREFIX = "spring_cloud_health_";

    public static final String METRICS_APP_NAME_CONVERTED = "application_name";

    public static final String METRICS_APP_HOST_CONVERTED = "application_host";

    public static final String METRICS_APP_PORT_CONVERTED = "application_port";

    public static final String METRICS_CREATE_TIME = "time";
}
