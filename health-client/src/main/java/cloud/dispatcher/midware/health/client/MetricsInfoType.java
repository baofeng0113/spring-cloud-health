package cloud.dispatcher.midware.health.client;

import lombok.Getter;

public enum MetricsInfoType {

    METRICS("/metrics"), INFO("/info"), HEALTH("/health"), ENV("/env");

    MetricsInfoType(String path) {
        this.path = path;
    }

    @Getter private String path;
}
