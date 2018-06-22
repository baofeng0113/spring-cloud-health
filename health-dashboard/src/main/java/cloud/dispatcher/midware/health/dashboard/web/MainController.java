package cloud.dispatcher.midware.health.dashboard.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDiscovery;
import cloud.dispatcher.midware.health.dashboard.model.MetricsInstanceNodeResponse;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired private ComponentMetricsDiscovery componentMetricsDiscovery;

    @RequestMapping({"/", "/index.html", "/application.html", "/general/application.html"})
    public ModelAndView indexView(@RequestParam(value = "serviceId", required = false) String serviceId) {
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
}
