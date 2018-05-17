package cloud.dispatcher.midware.health.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;

@Component
public class ComponentMetricsDataQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsDataQuery.class);

    @Autowired private MongoTemplate mongoTemplate;

    public List<JsonNode> getMetricsDataList(String name, String host, int port,
            long beginTimeMillis, long closeTimeMillis) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where(GlobalConfigValue.METRICS_APPLICATION_NAME_CONVERTED).is(name),
                Criteria.where(GlobalConfigValue.METRICS_APPLICATION_HOST_CONVERTED).is(host),
                Criteria.where(GlobalConfigValue.METRICS_APPLICATION_PORT_CONVERTED).is(port),
                Criteria.where(GlobalConfigValue.METRICS_CREATE_TIME).gte(beginTimeMillis),
                Criteria.where(GlobalConfigValue.METRICS_CREATE_TIME).lte(closeTimeMillis)
        );

        Query query = new Query(criteria).with(new Sort(Sort.Direction.ASC,
                GlobalConfigValue.METRICS_CREATE_TIME));

        return mongoTemplate.find(query, JsonNode.class, Application.getCollectionName(name));
    }
}
