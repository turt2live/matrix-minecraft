package io.t2l.mc.matrix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatrixUserProfile {
    @JsonProperty("displayname")
    public String displayName;

    @JsonProperty("avatar_url")
    public String avatarMxc;
}
