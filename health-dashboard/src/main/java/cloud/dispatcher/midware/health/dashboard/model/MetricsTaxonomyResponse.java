package cloud.dispatcher.midware.health.dashboard.model;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class MetricsTaxonomyResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private List<String> timeline;

    @Getter @Setter long pid;

    @Getter @Setter private String applicationName;

    @Getter @Setter private String applicationHost;

    @Getter @Setter private int applicationPort;

    @Getter @Setter private int processorNumber;

    @Getter @Setter private long instanceUptime;

    @Getter @Setter private long uptime;

    @Getter @Setter private String envProfile;

    @Getter @Setter private String jrtName;

    @Getter @Setter private String jrtVersion;

    @Getter @Setter private String jvmName;

    @Getter @Setter private String jvmVersion;

    @Getter @Setter private String jvmVendor;

    @Getter @Setter private List<Double> loadAverage;

    @Getter @Setter private List<Long> memoryAllotted;

    @Getter @Setter private List<Long> memoryUntapped;

    @Getter @Setter private List<Long> diskThreshold;

    @Getter @Setter private List<Long> diskCapacity;

    @Getter @Setter private List<Long> diskUntapped;

    @Getter @Setter private List<Long> heapAllotted;

    @Getter @Setter private List<Long> heapCommitted;

    @Getter @Setter private List<Long> heapInit;

    @Getter @Setter private List<Long> heapUsed;

    @Getter @Setter private List<Long> noHeapAllotted;

    @Getter @Setter private List<Long> noHeapCommitted;

    @Getter @Setter private List<Long> noHeapInit;

    @Getter @Setter private List<Long> noHeapUsed;

    @Getter @Setter private List<Long> threadHistoryTotal;

    @Getter @Setter private List<Long> threadCurrentTotal;

    @Getter @Setter private List<Long> threadCurrentPeak;

    @Getter @Setter private List<Long> threadCurrentDaemon;

    @Getter @Setter private List<Long> classesTotal;

    @Getter @Setter private List<Long> classesUnloaded;

    @Getter @Setter private List<Long> classesLoaded;

    @Getter @Setter private List<Long> gcPsScavengeCount;

    @Getter @Setter private List<Long> gcPsScavengeTime;

    @Getter @Setter private List<Long> gcPsMarkSweepCount;

    @Getter @Setter private List<Long> gcPsMarkSweepTime;

    @Getter @Setter private List<Long> httpSessionsMax;

    @Getter @Setter private List<Long> httpSessionsActive;
}
