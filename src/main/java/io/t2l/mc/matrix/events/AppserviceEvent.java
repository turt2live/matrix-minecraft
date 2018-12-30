package io.t2l.mc.matrix.events;

import com.github.jneat.minibus.EventBusEvent;
import io.t2l.mc.matrix.dto.MatrixAppserviceTransactionEvent;

public class AppserviceEvent implements EventBusEvent {
    public final MatrixAppserviceTransactionEvent event;

    public AppserviceEvent(MatrixAppserviceTransactionEvent event) {
        this.event = event;
    }
}
