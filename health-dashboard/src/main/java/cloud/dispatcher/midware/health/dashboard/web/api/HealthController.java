package cloud.dispatcher.midware.health.dashboard.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cloud.dispatcher.base.framework.entry.rest.RestDataResponse;
import cloud.dispatcher.base.framework.entry.rest.RestMetaResponse;

@RequestMapping("/api")
@RestController
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    private RestDataResponse newRestDataResponse(Object result) {
        return new RestDataResponse(result, new RestMetaResponse(null, 200, ""));
    }
}
