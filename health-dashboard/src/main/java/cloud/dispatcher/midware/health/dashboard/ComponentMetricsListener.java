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
        if (!metrics.containsKey(GlobalConfigValue.METRICS_APP_NAME)) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "payload", payload);
        }

        mongoTemplate.insert(metrics, Application.getCollectionName(metrics.get(
                GlobalConfigValue.METRICS_APP_NAME).toString()));

        LOGGER.info("Save metrics payload success, time: {}, host: {}, port: {}, application: {}",
                metrics.get(GlobalConfigValue.METRICS_CREATE_TIME),
                metrics.get(GlobalConfigValue.METRICS_APP_HOST),
                metrics.get(GlobalConfigValue.METRICS_APP_PORT),
                metrics.get(GlobalConfigValue.METRICS_APP_NAME)
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertPayload(String payload) {
        return CodecUtil.JSON.decode(payload, Map.class);
    }
}
