package cloud.dispatcher.midware.health.dashboard.web.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import cloud.dispatcher.base.framework.entry.rest.RestDataResponse;
import cloud.dispatcher.base.framework.entry.rest.RestMetaResponse;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDataQuery;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDiscovery;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsTaxonomy;

@RequestMapping("/api")
@RestController
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @Autowired private ComponentMetricsDataQuery componentMetricsDataQuery;

    @Autowired private ComponentMetricsDiscovery componentMetricsDiscovery;

    @RequestMapping(value = "/servicesList", method = RequestMethod.GET)
    public RestDataResponse servicesListAction() {
        return newRestDataResponse(componentMetricsDiscovery.getServiceIdList());
    }

    @RequestMapping(value = "/instanceList", method = RequestMethod.GET)
    public RestDataResponse instanceListAction(@RequestParam("service") String service) {
        return newRestDataResponse(componentMetricsDiscovery.getInstanceList(service));
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public RestDataResponse healthAction(@RequestParam("name") String name,
            @RequestParam("host") String host, @RequestParam("port") int port,
            @RequestParam("beginTimeMillis") long beginTimeMillis,
            @RequestParam("closeTimeMillis") long closeTimeMillis) {
        List<JsonNode> originalMetricsInfo = componentMetricsDataQuery.getMetricsDataList(
                name, host, port, beginTimeMillis, closeTimeMillis);

        return CollectionUtils.isEmpty(originalMetricsInfo) ? newRestDataResponse(null) :
                newRestDataResponse(componentMetricsTaxonomy.convert(originalMetricsInfo));
    }

    private RestDataResponse newRestDataResponse(Object result) {
        return new RestDataResponse(result, new RestMetaResponse(null, 200, ""));
    }

    @Autowired private ComponentMetricsTaxonomy componentMetricsTaxonomy;
}
