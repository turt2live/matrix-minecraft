package io.t2l.mc.matrix.appservice;

import io.t2l.mc.matrix.MatrixClient;
import io.t2l.mc.matrix.MatrixException;
import io.t2l.mc.matrix.dto.RegistrationResponse;

import java.util.HashMap;
import java.util.Map;

public class AppService {

    private String hsToken;
    private String asToken;
    private String userPrefix;
    private String domainName;
    private String csUrl;

    public AppService(String hsToken, String asToken, String userPrefix, String domainName, String csUrl) {
        this.hsToken = hsToken;
        this.asToken = asToken;
        this.userPrefix = userPrefix;
        this.domainName = domainName;
        this.csUrl = csUrl;
    }

    public MatrixClient getClient(String suffix) throws MatrixException {
        String localpart = this.userPrefix + suffix;
        this.registerUser(localpart);
        return new MatrixClient(this.csUrl, this.asToken, "@" + localpart + ":" + this.domainName);
    }

    private void registerUser(String localpart) throws MatrixException {
        try {
            MatrixClient asBot = new MatrixClient(this.csUrl, this.asToken);

            Map<String, String> body = new HashMap<>();
            body.put("type", "m.login.application_service");
            body.put("username", localpart);

            asBot.doRequest("POST", "/_matrix/client/r0/register", null, body, RegistrationResponse.class);
        } catch (MatrixException e) {
            if (!e.errcode.equals("M_USER_IN_USE")) {
                throw e;
            }
        }
    }
}
