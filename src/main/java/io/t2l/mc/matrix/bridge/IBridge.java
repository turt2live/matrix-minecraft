package io.t2l.mc.matrix.bridge;

import io.t2l.mc.matrix.MatrixException;

import java.io.IOException;
import java.util.UUID;

public interface IBridge {
    void start() throws IOException;

    void stop() throws IOException;

    void sendMatrixMessage(String roomId, UUID sender, String message) throws MatrixException;

    // TODO: Support Minecraft-JSON
    void sendMinecraftMessage(String sourceRoomId, String sender, String plaintext);
}
