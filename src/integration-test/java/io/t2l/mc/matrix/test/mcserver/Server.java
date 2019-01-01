package io.t2l.mc.matrix.test.mcserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Server {
    CRAFTBUKKIT("CraftBukkit", new MCVersion[]{
            MCVersion.V1_13_2,
            MCVersion.V1_13_1,
            MCVersion.V1_13,
            MCVersion.V1_12_2
    }),
    SPIGOT("Spigot", new MCVersion[]{
            MCVersion.V1_13_2,
            MCVersion.V1_13_1,
            MCVersion.V1_13,
            MCVersion.V1_12_2
    }),
    PAPER("Paperclip", new MCVersion[]{
            MCVersion.V1_13_2,
            MCVersion.V1_12_2
    }),
    GLOWSTONE("Glowstone", new MCVersion[]{
            MCVersion.V1_12_2
    }),
    ;

    public final String serverName;
    public final List<MCVersion> versions;

    Server(String serverName, MCVersion[] versions) {
        this.serverName = serverName;
        this.versions = Collections.unmodifiableList(Arrays.asList(versions));
    }
}
