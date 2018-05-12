package cloud.dispatcher.midware.health.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import cloud.dispatcher.base.framework.utils.CodecUtil;

@Component
public class ComponentMetricsInfoPublish {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsInfoPublish.class);

    @Value("${spring.cloud.health.kafka.topic}") private String kafkaTopic;

    @SuppressWarnings("unchecked")
    public void publishMetricsInfo(String serviceId, Map<String, Object> metrics) {
        kafkaTemplate.send(kafkaTopic, CodecUtil.JSON.encode(metrics)).addCallback(
                s -> LOGGER.info("Publish metrics info success, serviceId: {}", serviceId),
                f -> LOGGER.warn("Publish metrics info failure, serviceId: {}", serviceId)
        );
    }

    @Autowired private KafkaTemplate kafkaTemplate;
}
