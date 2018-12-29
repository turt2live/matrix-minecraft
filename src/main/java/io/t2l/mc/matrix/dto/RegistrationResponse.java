package io.t2l.mc.matrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties("home_server")
public class RegistrationResponse {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("user_id")
    public String userId;

    @JsonProperty("device_id")
    public String deviceId;
}
