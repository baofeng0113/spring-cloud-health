package cloud.dispatcher.midware.health.dashboard;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;
import cloud.dispatcher.midware.health.dashboard.model.MetricsTaxonomyResponse;

@Component
public class ComponentMetricsTaxonomy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsTaxonomy.class);

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public MetricsTaxonomyResponse convert(List<JsonNode> original) {
        if (CollectionUtils.isEmpty(original)) { return null; }

        MetricsTaxonomyResponse response = null;

        try {
            response = MetricsTaxonomyResponse.newInstance();
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

        setFixedDataNode(original.get(original.size() - 1), response);

        Iterator<JsonNode> iterator = original.iterator();
        while (iterator.hasNext()) {
            setArrayDataNode(iterator.next(), response);
            iterator.remove();
        }

        return response;
    }

    private void setFixedDataNode(final JsonNode lastNode, final MetricsTaxonomyResponse response) {
        response.setApplicationName(lastNode.get(GlobalConfigValue.METRICS_APP_NAME_CONVERTED).asText(""));
        response.setApplicationHost(lastNode.get(GlobalConfigValue.METRICS_APP_HOST_CONVERTED).asText(""));
        response.setApplicationPort(lastNode.get(GlobalConfigValue.METRICS_APP_PORT_CONVERTED).asInt(0));

        response.setJrtName(lastNode.get("env").get("systemProperties").get("java_runtime_name").textValue());
        response.setJrtVersion(lastNode.get("env").get("systemProperties").get("java_runtime_version").textValue());
        response.setJvmName(lastNode.get("env").get("systemProperties").get("java_vm_name").textValue());
        response.setJvmVersion(lastNode.get("env").get("systemProperties").get("java_vm_version").textValue());
        response.setJvmVendor(lastNode.get("env").get("systemProperties").get("java_vm_vendor").textValue());
        response.setPid(lastNode.get("env").get("systemProperties").get("PID").textValue());
        response.setProcessorNumber(lastNode.get("metrics").get("processors").intValue());
        response.setInstanceUptime(lastNode.get("metrics").get("instance_uptime").longValue());
        response.setUptime(lastNode.get("metrics").get("uptime").longValue());
    }

    private void setArrayDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        response.getLoadAverage().add(jsonNode.get("metrics").get("systemload_average").doubleValue());

        response.getMemoryAllotted().add(jsonNode.get("metrics").get("mem").longValue());
        response.getMemoryUntapped().add(jsonNode.get("metrics").get("mem_free").longValue());

        response.getDiskThreshold().add(jsonNode.get("health").get("diskSpace").get("threshold").longValue());
        response.getDiskCapacity().add(jsonNode.get("health").get("diskSpace").get("total").longValue());
        response.getDiskUntapped().add(jsonNode.get("health").get("diskSpace").get("free").longValue());

        response.getHeapCommitted().add(jsonNode.get("metrics").get("heap_committed").longValue());
        response.getHeapInit().add(jsonNode.get("metrics").get("heap_init").longValue());
        response.getHeapUsed().add(jsonNode.get("metrics").get("heap_used").longValue());
        response.getHeapAllotted().add(jsonNode.get("metrics").get("heap").longValue());

        response.getNonHeapCommitted().add(jsonNode.get("metrics").get("nonheap_committed").longValue());
        response.getNonHeapInit().add(jsonNode.get("metrics").get("nonheap_init").longValue());
        response.getNonHeapUsed().add(jsonNode.get("metrics").get("nonheap_used").longValue());
        response.getNonHeapAllotted().add(jsonNode.get("metrics").get("nonheap").longValue());

        response.getThreadHistoryTotal().add(jsonNode.get("metrics").get("threads_totalStarted").longValue());
        response.getThreadCurrentPeak().add(jsonNode.get("metrics").get("threads_peak").longValue());
        response.getThreadCurrentDaemon().add(jsonNode.get("metrics").get("threads_daemon").longValue());
        response.getThreadCurrentTotal().add(jsonNode.get("metrics").get("threads").longValue());

        response.getClassesUnloaded().add(jsonNode.get("metrics").get("classes_unloaded").longValue());
        response.getClassesTotal().add(jsonNode.get("metrics").get("classes").longValue());
        response.getClassesLoaded().add(jsonNode.get("metrics").get("classes_loaded").longValue());

        response.getGcPsScavengeCount().add(jsonNode.get("metrics").get("gc_ps_scavenge_count").longValue());
        response.getGcPsScavengeTime().add(jsonNode.get("metrics").get("gc_ps_scavenge_time").longValue());
        response.getGcPsMarkSweepCount().add(jsonNode.get("metrics").get("gc_ps_marksweep_count").longValue());
        response.getGcPsMarkSweepTime().add(jsonNode.get("metrics").get("gc_ps_marksweep_time").longValue());

        response.getHttpSessionsActive().add(jsonNode.get("metrics").get("httpsessions_active").longValue());
        response.getHttpSessionsMax().add(jsonNode.get("metrics").get("httpsessions_max").longValue());

        response.getTimeline().add(FastDateFormat.getInstance(DATETIME_FORMAT).format(jsonNode
                .get(GlobalConfigValue.METRICS_CREATE_TIME).longValue()));
    }
}
