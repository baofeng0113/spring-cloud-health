package cloud.dispatcher.midware.health.dashboard.model;

import java.io.Serializable;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class MetricsInstanceNodeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String serviceId;

    @Getter @Setter private int port;

    @Getter @Setter private String host;
}
