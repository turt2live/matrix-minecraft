package io.t2l.mc.matrix.events;

import com.github.jneat.minibus.EventBus;
import com.github.jneat.minibus.EventBusAsync;
import com.github.jneat.minibus.EventBusEvent;
import com.github.jneat.minibus.EventBusHandler;

import java.util.ArrayList;
import java.util.List;

public class PluginEventBus {

    private static EventBus<EventBusEvent, EventBusHandler<? extends EventBusEvent>> instance;
    private static List<EventBusHandler<? extends EventBusEvent>> handlers = new ArrayList<>();

    private PluginEventBus() {
    }

    public static void register(EventBusHandler<? extends EventBusEvent> handler) {
        handlers.add(handler);
        getInstance().subscribe(handler);
    }

    public static EventBus<EventBusEvent, EventBusHandler<? extends EventBusEvent>> getInstance() {
        if (instance == null) {
            instance = new EventBusAsync<>();
        }
        return instance;
    }
}
