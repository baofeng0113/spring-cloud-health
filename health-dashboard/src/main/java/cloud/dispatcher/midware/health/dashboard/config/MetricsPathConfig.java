package cloud.dispatcher.midware.health.dashboard.config;

public final class MetricsPathConfig {

    private MetricsPathConfig() {}

    public static final String[] INSTANCE_UPTIME = new String[]{"metrics", "instance_uptime"};
    public static final String[] UPTIME = new String[]{"metrics", "uptime"};
    public static final String[] PROCESSOR_NUMBER = new String[]{"metrics", "processors"};
    public static final String[] PID = new String[]{"env", "systemProperties", "PID"};

    public static final String[] JVM_VERSION = new String[]{"env", "systemProperties", "java_vm_version"};
    public static final String[] JVM_NAME = new String[]{"env", "systemProperties", "java_vm_name"};
    public static final String[] JVM_VENDOR = new String[]{"env", "systemProperties", "java_vm_vendor"};

    public static final String[] JRT_VERSION = new String[]{"env", "systemProperties", "java_runtime_version"};
    public static final String[] JRT_NAME = new String[]{"env", "systemProperties", "java_runtime_name"};

    public static final String[] LOAD_AVERAGE = new String[]{"metrics", "systemload_average"};

    public static final String[] MEMORY_ALLOTTED = new String[]{"metrics", "mem"};
    public static final String[] MEMORY_UNTAPPED = new String[]{"metrics", "mem_free"};

    public static final String[] DISK_THRESHOLD = new String[]{"health", "diskSpace", "threshold"};
    public static final String[] DISK_CAPACITY = new String[]{"health", "diskSpace", "total"};
    public static final String[] DISK_UNTAPPED = new String[]{"health", "diskSpace", "free"};

    public static final String[] NON_HEAP_ALLOTTED = new String[]{"metrics", "nonheap"};
    public static final String[] NON_HEAP_COMMITTED = new String[]{"metrics", "nonheap_committed"};
    public static final String[] NON_HEAP_INIT = new String[]{"metrics", "nonheap_init"};
    public static final String[] NON_HEAP_USED = new String[]{"metrics", "nonheap_used"};

    public static final String[] HEAP_ALLOTTED = new String[]{"metrics", "heap"};
    public static final String[] HEAP_COMMITTED = new String[]{"metrics", "heap_committed"};
    public static final String[] HEAP_INIT = new String[]{"metrics", "heap_init"};
    public static final String[] HEAP_USED = new String[]{"metrics", "heap_used"};

    public static final String[] THREAD_HISTORY_TOTAL = new String[]{"metrics", "threads_totalStarted"};
    public static final String[] THREAD_CURRENT_PEAK = new String[]{"metrics", "threads_peak"};
    public static final String[] THREAD_CURRENT_DAEMON = new String[]{"metrics", "threads_daemon"};
    public static final String[] THREAD_CURRENT_TOTAL = new String[]{"metrics", "threads"};

    public static final String[] CLASSES_UNLOADED = new String[]{"metrics", "classes_unloaded"};
    public static final String[] CLASSES_TOTAL = new String[]{"metrics", "classes"};
    public static final String[] CLASSES_LOADED = new String[]{"metrics", "classes_loaded"};

    public static final String[] GC_PS_SCAVENGE_COUNT = new String[]{"metrics", "gc_ps_scavenge_count"};
    public static final String[] GC_PS_SCAVENGE_TIME = new String[]{"metrics", "gc_ps_scavenge_time"};
    public static final String[] GC_PS_MARK_SWEEP_COUNT = new String[]{"metrics", "gc_ps_marksweep_count"};
    public static final String[] GC_PS_MARK_SWEEP_TIME = new String[]{"metrics", "gc_ps_marksweep_time"};

    public static final String[] HTTP_SESSIONS_ACTIVE = new String[]{"metrics", "httpsessions_active"};
    public static final String[] HTTP_SESSIONS_MAX = new String[]{"metrics", "httpsessions_max"};
}
