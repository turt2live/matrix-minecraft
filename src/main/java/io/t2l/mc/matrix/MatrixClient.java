package io.t2l.mc.matrix;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.t2l.mc.matrix.dto.EventIdResponse;
import io.t2l.mc.matrix.dto.MatrixError;
import io.t2l.mc.matrix.dto.MatrixMessage;
import io.t2l.mc.matrix.dto.RoomIdResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MatrixClient {

    private static ObjectMapper json = new ObjectMapper();

    private String hsUrl;
    private String accessToken;
    private String impersonateUserId;

    public MatrixClient(String hsUrl, String accessToken) {
        this.hsUrl = hsUrl;
        this.accessToken = accessToken;

        if (this.hsUrl.endsWith("/")) {
            this.hsUrl = this.hsUrl.substring(0, this.hsUrl.length() - 1);
        }
    }

    public MatrixClient(String hsUrl, String accessToken, String impersonateUserId) {
        this(hsUrl, accessToken);
        this.impersonateUserId = impersonateUserId;
    }

    public String joinRoom(String roomIdOrAlias) throws MatrixException {
        RoomIdResponse r = this.doRequest("POST", "/_matrix/client/r0/rooms/" + roomIdOrAlias + "/join", null, null, RoomIdResponse.class);
        return r.roomId;
    }

    public String sendMessage(String roomId, MatrixMessage message) throws MatrixException {
        EventIdResponse r = this.doRequest("PUT", "/_matrix/client/r0/rooms/" + roomId + "/send/m.room.message/" + (new Date().getTime()), null, message, EventIdResponse.class);
        return r.eventId;
    }

    public <T> T doRequest(String method, String endpoint, Map<String, Object> queryString, Object body, Class<T> clazz) throws MatrixException {
        try {
            if (!endpoint.startsWith("/")) endpoint = "/" + endpoint;
            if (queryString == null) queryString = new HashMap<>();

            StringBuilder qs = new StringBuilder();
            if (this.impersonateUserId != null) queryString.put("user_id", this.impersonateUserId);
            for (Map.Entry<String, Object> param : queryString.entrySet()) {
                if (qs.length() > 0) qs.append("&");
                qs.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
            }

            URL url = new URL(this.hsUrl + endpoint + "?" + qs.toString());

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "Bearer " + this.accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Matrix-Minecraft Bukkit Plugin");

            String bodyJson = "";
            if (body != null) bodyJson = json.writeValueAsString(body);

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(bodyJson);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();

            InputStream stream = connection.getErrorStream();
            if (stream == null) stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder rawJson = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) rawJson.append(line);
            reader.close();

            if (responseCode != 200) {
                MatrixError err = json.readValue(rawJson.toString(), MatrixError.class);
                throw new MatrixException(err.error, err.errcode, responseCode);
            }

            return json.readValue(rawJson.toString(), clazz);
        } catch (IOException e) {
            throw new MatrixException("Error making request", "M_UNKNOWN", 500, e);
        }
    }
}
