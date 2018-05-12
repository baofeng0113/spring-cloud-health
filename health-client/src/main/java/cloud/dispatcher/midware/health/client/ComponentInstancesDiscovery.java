package cloud.dispatcher.midware.health.client;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cloud.dispatcher.base.framework.utils.LocalizeEnvUtil;

@Component
public class ComponentInstancesDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentInstancesDiscovery.class);

    @Autowired private DiscoveryClient eurekaDiscoveryClient;

    public List<ServiceInstance> getLocalizeInstanceList() {
        List<String> serviceIdListOnDiscovery = eurekaDiscoveryClient.getServices();
        if (serviceIdListOnDiscovery.isEmpty()) {
            return Collections.emptyList();
        }

        List<ServiceInstance> localizeInstanceList = Lists.newArrayList();

        for (String serviceId : serviceIdListOnDiscovery) {
            List<ServiceInstance> serviceInstanceList = eurekaDiscoveryClient.getInstances(serviceId);
            if (!serviceInstanceList.isEmpty()) {
                serviceInstanceList.forEach(serviceInstance -> {
                    if (serviceInstance.getHost().equals(LocalizeEnvUtil.getMachineIpv4Address())) {
                        localizeInstanceList.add(serviceInstance);
                    }
                });
            }
        }

        LOGGER.debug("Fetch local instances success, instances: {}", localizeInstanceList);

        return localizeInstanceList;
    }
}
