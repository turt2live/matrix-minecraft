package io.t2l.mc.matrix.bridge;

import io.t2l.mc.matrix.MatrixException;

import java.util.UUID;

public interface IBridge {
    void sendMessage(String roomId, UUID sender, String message) throws MatrixException;
}
