package io.t2l.mc.matrix.appservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import io.t2l.mc.matrix.dto.MatrixAppserviceTransaction;
import io.t2l.mc.matrix.events.AppserviceEvent;
import io.t2l.mc.matrix.events.PluginEventBus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class AppserviceTransactionHandler extends RouterNanoHTTPD.DefaultHandler {

    private static final int MAX_TRANSACTION_HISTORY = 50;
    private static LinkedHashMap<String, NanoHTTPD.Response> transactions = new LinkedHashMap<>();
    private static ObjectMapper json = new ObjectMapper();
    static String hsToken;

    public AppserviceTransactionHandler() {
        super();
    }

    private void pruneMap() {
        while (transactions.size() > MAX_TRANSACTION_HISTORY) {
            transactions.remove(transactions.keySet().toArray()[0].toString());
        }
    }

    private boolean isAuthorized(NanoHTTPD.IHTTPSession session) {
        List<String> accessTokens = session.getParameters().get("access_token");
        if (accessTokens != null && accessTokens.size() > 0) {
            return accessTokens.stream().findFirst().get().equals(hsToken);
        }
        return false;
    }

    @Override
    public String getText() {
        return "{}";
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.NOT_FOUND;
    }

    @Override
    public NanoHTTPD.Response put(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        if (!isAuthorized(session)) {
            return newFixedLengthResponse(NanoHTTPD.Response.Status.FORBIDDEN, "application/json", "{\"error\":\"Not authorized\",\"errcode\":\"M_FORBIDDEN\"}");
        }

        String transactionId = urlParams.get("txnId");
        if (transactions.containsKey(transactionId)) {
            if (transactions.get(transactionId) != null) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", "{}");
            } else {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, "application/json", "{\"error\":\"Duplicate request in progress\",\"errcode\":\"M_UNKNOWN\"}");
            }
        }
        transactions.put(transactionId, null);

        try {
            Map<String, String> body = new HashMap<>();
            session.parseBody(body);

            if (!body.containsKey("content")) {
                return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", "{\"error\":\"Missing post data\",\"errcode\":\"M_UNKNOWN\"}");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(body.get("content"))));
            StringBuilder rawJson = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) rawJson.append(line);
            reader.close();

            MatrixAppserviceTransaction txn = json.readValue(rawJson.toString(), MatrixAppserviceTransaction.class);
            txn.events.stream().map(AppserviceEvent::new).forEach(e -> PluginEventBus.getInstance().publish(e));

            NanoHTTPD.Response response = newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", "{}");
            transactions.put(transactionId, response);
            pruneMap();
            return response;
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
            return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, "application/json", "{\"error\":\"Internal server error\",\"errcode\":\"M_UNKNOWN\"}");
        }
    }
}
