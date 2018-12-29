package io.t2l.mc.matrix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatrixMessage {

    public String body;
    public String msgtype;
    public String format;

    @JsonProperty("formatted_body")
    public String formattedBody;

}
