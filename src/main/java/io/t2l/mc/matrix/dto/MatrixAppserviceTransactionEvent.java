package io.t2l.mc.matrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatrixAppserviceTransactionEvent {
    public static final String STATE_KEY_DEFAULT = "____NO_STATE_KEY____";

    public Map<String, Object> content;

    public String type;

    @JsonProperty("event_id")
    public String eventId;

    @JsonProperty("room_id")
    public String roomId;

    @JsonProperty("sender")
    public String sender;

    @JsonProperty(value = "state_key", defaultValue = STATE_KEY_DEFAULT)
    public String stateKey;

    @JsonProperty("origin_server_ts")
    public long originServerTs;
}
