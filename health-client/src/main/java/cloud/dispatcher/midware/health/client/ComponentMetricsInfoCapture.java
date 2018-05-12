package cloud.dispatcher.midware.health.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import cloud.dispatcher.base.framework.utils.CodecUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class ComponentMetricsInfoCapture implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsInfoCapture.class);

    private static final String APPLICATION_NAME = "application.name";

    private static final String APPLICATION_HOST = "application.host";

    private static final String APPLICATION_PORT = "application.port";

    private static final String LOCALHOST_DOMAIN = "localhost.localdomain";

    private OkHttpClient okHttpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).readTimeout(3, TimeUnit.SECONDS).build();
    }

    public Map<String, Object> captureMetricsInfo(ServiceInstance instance) {
        EurekaDiscoveryClient.EurekaServiceInstance eurekaInstance = (EurekaDiscoveryClient.EurekaServiceInstance) instance;
        String scheme = instance.isSecure() ? "https" : "http";
        String uri = String.format("%s://%s:%s", scheme, LOCALHOST_DOMAIN, instance.getPort());
        Map<String, Object> metricsMap = buildMetricsInfoMap(eurekaInstance);
        for (MetricsInfoType type : MetricsInfoType.values()) {
            putMetricsInfoIntoMap(type, uri, metricsMap);
        }
        return metricsMap;
    }

    private Map<String, Object> buildMetricsInfoMap(EurekaDiscoveryClient.EurekaServiceInstance instance) {
        Map<String, Object> metricsInfoMap = new HashMap<>();
        metricsInfoMap.put(APPLICATION_NAME, instance.getInstanceInfo().getAppName());
        metricsInfoMap.put(APPLICATION_HOST, instance.getHost());
        metricsInfoMap.put(APPLICATION_PORT, instance.getPort());
        return metricsInfoMap;
    }

    private void putMetricsInfoIntoMap(MetricsInfoType type, String uri, Map<String, Object> metricsMap) {
        try {
            Response response = okHttpClient.newCall(new Request.Builder().get().url(uri + type.getPath()).build()).execute();
            if (response.code() != HttpStatus.OK.value()) {
                LOGGER.warn("Read metrics info on '{}' failure, code: {}", uri + type.getPath(), response.code());
                return;
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (StringUtils.isBlank(responseBody) || !responseBody.startsWith("{")) {
                LOGGER.warn("Read metrics info on '{}' failure, body: {}", uri + type.getPath(), responseBody);
                return;
            }

            metricsMap.put(type.name().toLowerCase(), CodecUtil.JSON.decode(responseBody, Map.class));
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
