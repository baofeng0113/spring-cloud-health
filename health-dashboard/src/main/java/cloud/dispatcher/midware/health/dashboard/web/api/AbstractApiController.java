package cloud.dispatcher.midware.health.dashboard.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cloud.dispatcher.base.framework.entry.rest.RestDataResponse;
import cloud.dispatcher.base.framework.entry.rest.RestMetaResponse;

public abstract class AbstractApiController {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected RestDataResponse newRestDataResponse(Object result) {
        return new RestDataResponse(result, new RestMetaResponse(null, 200, ""));
    }
}
