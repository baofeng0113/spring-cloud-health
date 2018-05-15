package cloud.dispatcher.midware.health.dashboard.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cloud.dispatcher.midware.health.dashboard.ComponentMetricsDiscovery;

@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired private ComponentMetricsDiscovery componentMetricsDiscovery;

    @RequestMapping(value = {"/", "/index", "/index.action", "/index.html"})
    public ModelAndView indexAction(ModelAndView modelAndView) {
        List<String> serviceIdList = componentMetricsDiscovery.getServiceIdList();
        modelAndView.addObject("services", serviceIdList);
        modelAndView.addObject("selected", serviceIdList.isEmpty() ?
                StringUtils.EMPTY : serviceIdList.get(0));
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
