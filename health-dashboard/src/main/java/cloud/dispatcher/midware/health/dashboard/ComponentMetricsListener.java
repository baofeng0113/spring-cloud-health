package cloud.dispatcher.midware.health.dashboard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cloud.dispatcher.base.framework.error.CheckedException;
import cloud.dispatcher.base.framework.error.DefaultExceptionMessage;
import cloud.dispatcher.base.framework.utils.CodecUtil;
import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;

@Component
public class ComponentMetricsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsListener.class);

    @Autowired private MongoTemplate mongoTemplate;

    @KafkaListener(topics = "spring-cloud-health")
    public void reciveMetricsPayload(String payload) {
        if (StringUtils.isBlank(payload) || !payload.startsWith("{") || !payload.endsWith("}")) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "payload", payload);
        }

        Map<String, Object> metrics = convertPayload(payload);
        if (!metrics.containsKey(GlobalConfigValue.METRICS_APPLICATION_NAME)) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "payload", payload);
        }

        mongoTemplate.insert(metrics, collectionName(metrics.get(GlobalConfigValue.METRICS_APPLICATION_NAME).toString()));

        LOGGER.info("Save metrics payload success, time: {}, host: {}, port: {}, application: {}",
                metrics.get(GlobalConfigValue.METRICS_CREATE_TIME),
                metrics.get(GlobalConfigValue.METRICS_APPLICATION_HOST),
                metrics.get(GlobalConfigValue.METRICS_APPLICATION_PORT),
                metrics.get(GlobalConfigValue.METRICS_APPLICATION_NAME)
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertPayload(String payload) {
        return CodecUtil.JSON.decode(payload, Map.class);
    }

    private String collectionName(String applicationName) {
        if (StringUtils.isBlank(applicationName)) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "applicationName", applicationName);
        }

        int startWith = applicationName.toLowerCase().charAt(0);
        if (startWith < 97 || startWith > 122) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "applicationName", applicationName);
        }

        return GlobalConfigValue.MONGO_COLLECTION_NAME_PREFIX + (char) startWith;
    }
}
