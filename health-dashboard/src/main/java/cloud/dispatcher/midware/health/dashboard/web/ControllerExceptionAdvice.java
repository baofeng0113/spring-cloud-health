package cloud.dispatcher.midware.health.dashboard.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cloud.dispatcher.base.framework.entry.rest.RestDataResponse;
import cloud.dispatcher.base.framework.entry.rest.RestMetaResponse;
import cloud.dispatcher.base.framework.error.CheckedException;
import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;

@ControllerAdvice(GlobalConfigValue.REST_CONTROLLER_BASE_PACKAGE)
public class ControllerExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(value = CheckedException.class)
    @ResponseBody
    public RestDataResponse checkedExceptionHandle(CheckedException exception) {
        String text = exception.getExceptionMessage().template;
        int code = exception.getExceptionMessage().code;
        if (exception.getParameter() != null && exception.getParameter().length > 0) {
            text = MessageFormatter.arrayFormat(exception.getExceptionMessage().template,
                    exception.getParameter()).getMessage();
        }
        return new RestDataResponse(null, new RestMetaResponse(null, code, text));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestDataResponse exceptionHandle(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        if (exception instanceof IllegalArgumentException || exception instanceof MissingServletRequestParameterException) {
            return new RestDataResponse(null, new RestMetaResponse(null, 400, exception.getMessage()));
        } else {
            return new RestDataResponse(null, new RestMetaResponse(null, 500, "Internal error"));
        }
    }
}
