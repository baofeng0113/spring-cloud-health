package cloud.dispatcher.midware.health.dashboard;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.*;

@Component
public class ComponentMetricsTaxonomy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsTaxonomy.class);

    public MetricsTaxonomyModel convert(List<JsonNode> original) {
        if (CollectionUtils.isEmpty(original)) { return null; }

        MetricsTaxonomyModel result = new MetricsTaxonomyModel();

        return result;
    }

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @ToString
    public static class MetricsTaxonomyModel implements Serializable {

        public MetricsTaxonomyModel(String applicationName, String applicationHost, int applicationPort) {
            this.applicationName = applicationName;
            this.applicationHost = applicationHost;
            this.applicationPort = applicationPort;
        }

        @Getter @Setter private String applicationName;

        @Getter @Setter private String applicationHost;

        @Getter @Setter private int applicationPort;

        @Getter @Setter private List<String> timeline;
    }
}
