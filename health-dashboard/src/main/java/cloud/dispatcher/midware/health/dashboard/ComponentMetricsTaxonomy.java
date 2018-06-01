package cloud.dispatcher.midware.health.dashboard;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;
import cloud.dispatcher.midware.health.dashboard.config.MetricsPathConfig;
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

    private JsonNode getJsonNodeByPath(JsonNode json, String[] path) {
        JsonNode result = json;
        if (Objects.isNull(json) || Objects.isNull(path) || path.length == 0) { return null; }

        for (String node : path) {
            result = result.has(node) ? result.get(node) : null;
            if (Objects.isNull(result)) { return null; }
        }

        return result;
    }

    private void setFixedDataNode(final JsonNode lastNode, final MetricsTaxonomyResponse response) {
        response.setApplicationName(lastNode.get(GlobalConfigValue.METRICS_APP_NAME_CONVERTED).asText(StringUtils.EMPTY));
        response.setApplicationHost(lastNode.get(GlobalConfigValue.METRICS_APP_HOST_CONVERTED).asText(StringUtils.EMPTY));
        response.setApplicationPort(lastNode.get(GlobalConfigValue.METRICS_APP_PORT_CONVERTED).asInt(-1));

        JsonNode instanceUptime = getJsonNodeByPath(lastNode, MetricsPathConfig.INSTANCE_UPTIME);
        JsonNode uptime = getJsonNodeByPath(lastNode, MetricsPathConfig.UPTIME);
        JsonNode pid = getJsonNodeByPath(lastNode, MetricsPathConfig.PID);
        JsonNode processorNumber = getJsonNodeByPath(lastNode, MetricsPathConfig.PROCESSOR_NUMBER);
        JsonNode status = getJsonNodeByPath(lastNode, MetricsPathConfig.STATUS);
        JsonNode jvmVersion = getJsonNodeByPath(lastNode, MetricsPathConfig.JVM_VERSION);
        JsonNode jrtVersion = getJsonNodeByPath(lastNode, MetricsPathConfig.JRT_VERSION);
        JsonNode jvmName = getJsonNodeByPath(lastNode, MetricsPathConfig.JVM_NAME);
        JsonNode jrtName = getJsonNodeByPath(lastNode, MetricsPathConfig.JRT_NAME);
        JsonNode jvmVendor = getJsonNodeByPath(lastNode, MetricsPathConfig.JVM_VENDOR);

        response.setJvmVersion(Objects.isNull(jvmVersion) ? StringUtils.EMPTY : jvmVersion.textValue());
        response.setJrtVersion(Objects.isNull(jrtVersion) ? StringUtils.EMPTY : jrtVersion.textValue());
        response.setJvmName(Objects.isNull(jvmName) ? StringUtils.EMPTY : jvmName.textValue());
        response.setJrtName(Objects.isNull(jrtName) ? StringUtils.EMPTY : jrtName.textValue());
        response.setJvmVendor(Objects.isNull(jvmVendor) ? StringUtils.EMPTY : jvmVendor.textValue());
        response.setInstanceUptime(Objects.isNull(instanceUptime) ? -1 : instanceUptime.longValue());
        response.setUptime(Objects.isNull(uptime) ? -1 : uptime.longValue());
        response.setProcessorNumber(Objects.isNull(processorNumber) ? -1 : processorNumber.intValue());
        response.setPid(Objects.isNull(pid) ? StringUtils.EMPTY : pid.textValue());
        response.setStatus(Objects.isNull(status) ? StringUtils.EMPTY : status.textValue());
    }

    private void setArrayDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode loadAverage = getJsonNodeByPath(jsonNode, MetricsPathConfig.LOAD_AVERAGE);
        response.getLoadAverage().add(Objects.isNull(loadAverage) ? -0.01 : loadAverage.doubleValue());

        appendHttpSessionsDataNode(jsonNode, response);
        appendMemoryDataNode(jsonNode, response);
        appendDiskDataNode(jsonNode, response);
        appendNonHeapDataNode(jsonNode, response);
        appendHeapDataNode(jsonNode, response);
        appendThreadDataNode(jsonNode, response);
        appendClassesDataNode(jsonNode, response);
        appendGcPsDataNode(jsonNode, response);

        response.getTimeline().add(FastDateFormat.getInstance(DATETIME_FORMAT).format(jsonNode
                .get(GlobalConfigValue.METRICS_CREATE_TIME).longValue()));
    }

    private void appendMemoryDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode memoryAllotted = getJsonNodeByPath(jsonNode, MetricsPathConfig.MEMORY_ALLOTTED);
        JsonNode memoryUntapped = getJsonNodeByPath(jsonNode, MetricsPathConfig.MEMORY_UNTAPPED);
        response.getMemoryAllotted().add(Objects.isNull(memoryAllotted) ? -1L : memoryAllotted.longValue());
        response.getMemoryUntapped().add(Objects.isNull(memoryUntapped) ? -1L : memoryUntapped.longValue());
    }

    private void appendDiskDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode diskThreshold = getJsonNodeByPath(jsonNode, MetricsPathConfig.DISK_THRESHOLD);
        JsonNode diskCapacity = getJsonNodeByPath(jsonNode, MetricsPathConfig.DISK_CAPACITY);
        JsonNode diskUntapped = getJsonNodeByPath(jsonNode, MetricsPathConfig.DISK_UNTAPPED);
        response.getDiskThreshold().add(Objects.isNull(diskThreshold) ? -1L : diskThreshold.longValue());
        response.getDiskCapacity().add(Objects.isNull(diskCapacity) ? -1L : diskCapacity.longValue());
        response.getDiskUntapped().add(Objects.isNull(diskUntapped) ? -1L : diskUntapped.longValue());
    }

    private void appendNonHeapDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode nonHeapCommitted = getJsonNodeByPath(jsonNode, MetricsPathConfig.NON_HEAP_COMMITTED);
        JsonNode nonHeapInit = getJsonNodeByPath(jsonNode, MetricsPathConfig.NON_HEAP_INIT);
        JsonNode nonHeapUsed = getJsonNodeByPath(jsonNode, MetricsPathConfig.NON_HEAP_USED);
        JsonNode nonHeapAllotted = getJsonNodeByPath(jsonNode, MetricsPathConfig.NON_HEAP_ALLOTTED);
        response.getNonHeapCommitted().add(Objects.isNull(nonHeapCommitted) ? -1L : nonHeapCommitted.longValue());
        response.getNonHeapInit().add(Objects.isNull(nonHeapInit) ? -1L : nonHeapInit.longValue());
        response.getNonHeapUsed().add(Objects.isNull(nonHeapUsed) ? -1L : nonHeapUsed.longValue());
        response.getNonHeapAllotted().add(Objects.isNull(nonHeapAllotted) ? -1L : nonHeapAllotted.longValue());
    }

    private void appendHeapDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode heapCommitted = getJsonNodeByPath(jsonNode, MetricsPathConfig.HEAP_COMMITTED);
        JsonNode heapInit = getJsonNodeByPath(jsonNode, MetricsPathConfig.HEAP_INIT);
        JsonNode heapUsed = getJsonNodeByPath(jsonNode, MetricsPathConfig.HEAP_USED);
        JsonNode heapAllotted = getJsonNodeByPath(jsonNode, MetricsPathConfig.HEAP_ALLOTTED);
        response.getHeapCommitted().add(Objects.isNull(heapCommitted) ? -1L : heapCommitted.longValue());
        response.getHeapInit().add(Objects.isNull(heapInit) ? -1L : heapInit.longValue());
        response.getHeapUsed().add(Objects.isNull(heapUsed) ? -1L : heapUsed.longValue());
        response.getHeapAllotted().add(Objects.isNull(heapAllotted) ? -1L : heapAllotted.longValue());
    }

    private void appendThreadDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode threadHistoryTotal = getJsonNodeByPath(jsonNode, MetricsPathConfig.THREAD_HISTORY_TOTAL);
        JsonNode threadCurrentPeak = getJsonNodeByPath(jsonNode, MetricsPathConfig.THREAD_CURRENT_PEAK);
        JsonNode threadCurrentDaemon = getJsonNodeByPath(jsonNode, MetricsPathConfig.THREAD_CURRENT_DAEMON);
        JsonNode threadCurrentTotal = getJsonNodeByPath(jsonNode, MetricsPathConfig.THREAD_CURRENT_TOTAL);
        response.getThreadHistoryTotal().add(Objects.isNull(threadHistoryTotal) ? -1L : threadHistoryTotal.longValue());
        response.getThreadCurrentPeak().add(Objects.isNull(threadCurrentPeak) ? -1L : threadCurrentPeak.longValue());
        response.getThreadCurrentDaemon().add(Objects.isNull(threadCurrentDaemon) ? -1L : threadCurrentDaemon.longValue());
        response.getThreadCurrentTotal().add(Objects.isNull(threadCurrentTotal) ? -1L : threadCurrentTotal.longValue());
    }

    private void appendClassesDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode classesUnloaded = getJsonNodeByPath(jsonNode, MetricsPathConfig.CLASSES_UNLOADED);
        JsonNode classesTotal = getJsonNodeByPath(jsonNode, MetricsPathConfig.CLASSES_TOTAL);
        JsonNode classesLoaded = getJsonNodeByPath(jsonNode, MetricsPathConfig.CLASSES_LOADED);
        response.getClassesUnloaded().add(Objects.isNull(classesUnloaded) ? -1L : classesUnloaded.longValue());
        response.getClassesTotal().add(Objects.isNull(classesTotal) ? -1L : classesTotal.longValue());
        response.getClassesLoaded().add(Objects.isNull(classesLoaded) ? -1L : classesLoaded.longValue());
    }

    private void appendGcPsDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode gcPsScavengeCount = getJsonNodeByPath(jsonNode, MetricsPathConfig.GC_PS_SCAVENGE_COUNT);
        JsonNode gcPsScavengeTime = getJsonNodeByPath(jsonNode, MetricsPathConfig.GC_PS_SCAVENGE_TIME);
        JsonNode gcPsMarkSweepCount = getJsonNodeByPath(jsonNode, MetricsPathConfig.GC_PS_MARK_SWEEP_COUNT);
        JsonNode gcPsMarkSweepTime = getJsonNodeByPath(jsonNode, MetricsPathConfig.GC_PS_MARK_SWEEP_TIME);
        response.getGcPsScavengeCount().add(Objects.isNull(gcPsScavengeCount) ? -1L : gcPsScavengeCount.longValue());
        response.getGcPsScavengeTime().add(Objects.isNull(gcPsScavengeTime) ? -1L : gcPsScavengeTime.longValue());
        response.getGcPsMarkSweepCount().add(Objects.isNull(gcPsMarkSweepCount) ? -1L : gcPsMarkSweepCount.longValue());
        response.getGcPsMarkSweepTime().add(Objects.isNull(gcPsMarkSweepTime) ? -1L : gcPsMarkSweepTime.longValue());
    }

    private void appendHttpSessionsDataNode(final JsonNode jsonNode, final MetricsTaxonomyResponse response) {
        JsonNode httpSessionsActive = getJsonNodeByPath(jsonNode, MetricsPathConfig.HTTP_SESSIONS_ACTIVE);
        JsonNode httpSessionsMax = getJsonNodeByPath(jsonNode, MetricsPathConfig.HTTP_SESSIONS_MAX);
        response.getHttpSessionsActive().add(Objects.isNull(httpSessionsActive) ? -1L : httpSessionsActive.longValue());
        response.getHttpSessionsMax().add(Objects.isNull(httpSessionsMax) ? -1L : httpSessionsMax.longValue());
    }
}
