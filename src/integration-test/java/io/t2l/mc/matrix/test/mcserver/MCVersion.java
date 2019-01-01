package io.t2l.mc.matrix.test.mcserver;

public enum MCVersion {
    V1_12_2("1.12.2"),
    V1_13("1.13"),
    V1_13_1("1.13.1"),
    V1_13_2("1.13.2"),
    ;

    public final String version;

    MCVersion(String version) {
        this.version = version;
    }
}
