package cloud.dispatcher.midware.health.dashboard.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import cloud.dispatcher.base.framework.entry.rest.RestDataResponse;
import cloud.dispatcher.base.framework.entry.rest.RestMetaResponse;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDataQuery;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDiscovery;
import cloud.dispatcher.midware.health.dashboard.ComponentMetricsTaxonomy;
import cloud.dispatcher.midware.health.dashboard.model.MetricsInstanceNodeResponse;

@RequestMapping("/general")
@RestController
public class GeneralController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralController.class);

    @Autowired private ComponentMetricsDataQuery componentMetricsDataQuery;

    @Autowired private ComponentMetricsDiscovery componentMetricsDiscovery;

    @RequestMapping("/application.html")
    public ModelAndView applicationView(@RequestParam(value = "serviceId", required = false) String serviceId) {
        ModelAndView modelAndView = new ModelAndView("application");
        List<String> serviceIdList = componentMetricsDiscovery.getServiceIdList();
        modelAndView.addObject("serviceIdList", serviceIdList);

        serviceId = StringUtils.isNotBlank(serviceId) && serviceIdList.contains(
                serviceId) ? serviceId : serviceIdList.get(0);

        List<MetricsInstanceNodeResponse> instanceNodeList = Lists.newArrayList();
        List<ServiceInstance> instances = componentMetricsDiscovery.getInstanceList(serviceId);
        if (!CollectionUtils.isEmpty(instances)) {
            instances.forEach(item -> instanceNodeList.add(new MetricsInstanceNodeResponse(
                    item.getServiceId(), item.getPort(), item.getHost())));
        }
        modelAndView.addObject("instanceNodeList", instanceNodeList);

        return modelAndView;
    }

    @RequestMapping(value = "/api/servicesList", method = RequestMethod.GET)
    public RestDataResponse servicesListAction() {
        return newRestDataResponse(componentMetricsDiscovery.getServiceIdList());
    }

    @RequestMapping(value = "/api/instanceList", method = RequestMethod.GET)
    public RestDataResponse instanceListAction(@RequestParam("service") String service) {
        List<ServiceInstance> instanceList = componentMetricsDiscovery.getInstanceList(service);
        List<MetricsInstanceNodeResponse> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(instanceList)) {
            instanceList.forEach(item -> result.add(new MetricsInstanceNodeResponse(
                    item.getServiceId(), item.getPort(), item.getHost())));
        }
        return newRestDataResponse(result);
    }

    @RequestMapping(value = "/api/health", method = RequestMethod.GET)
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
