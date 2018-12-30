package io.t2l.mc.matrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class MatrixAppserviceTransaction {
    public List<MatrixAppserviceTransactionEvent> events;
}

