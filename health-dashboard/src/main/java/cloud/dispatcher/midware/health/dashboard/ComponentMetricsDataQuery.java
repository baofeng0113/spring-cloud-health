package cloud.dispatcher.midware.health.dashboard;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import cloud.dispatcher.base.framework.utils.CodecUtil;
import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;

@Component
public class ComponentMetricsDataQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsDataQuery.class);

    private static final long INTERVAL_TIME_MILLIS_MAX = 1800000;

    @Autowired private MongoTemplate mongoTemplate;

    public List<JsonNode> getMetricsDataList(String name, String host, int port,
            long beginTimeMillis, long closeTimeMillis) {
        if ((closeTimeMillis - beginTimeMillis) > INTERVAL_TIME_MILLIS_MAX) {
            throw new IllegalArgumentException("CloseTimeMillis - beginTimeMillis must less than "
                    + INTERVAL_TIME_MILLIS_MAX);
        }

        Criteria criteria = new Criteria().andOperator(
                Criteria.where(GlobalConfigValue.METRICS_APP_NAME_CONVERTED).regex(name, "i"),
                Criteria.where(GlobalConfigValue.METRICS_APP_HOST_CONVERTED).is(host),
                Criteria.where(GlobalConfigValue.METRICS_APP_PORT_CONVERTED).is(port),
                Criteria.where(GlobalConfigValue.METRICS_CREATE_TIME).gte(beginTimeMillis),
                Criteria.where(GlobalConfigValue.METRICS_CREATE_TIME).lte(closeTimeMillis)
        );

        Query query = new Query(criteria).with(new Sort(Sort.Direction.ASC,
                GlobalConfigValue.METRICS_CREATE_TIME));

        List<String> jsonList = mongoTemplate.find(query, String.class, Application.getCollectionName(name));
        if (CollectionUtils.isEmpty(jsonList)) { return Collections.emptyList(); }

        List<JsonNode> result = Lists.newArrayList();
        jsonList.forEach(json -> result.add(CodecUtil.JSON.readTree(json)));

        return result;
    }
}
