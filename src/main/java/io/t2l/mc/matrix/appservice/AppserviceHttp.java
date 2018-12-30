package io.t2l.mc.matrix.appservice;

import fi.iki.elonen.router.RouterNanoHTTPD;

public class AppserviceHttp extends RouterNanoHTTPD {

    AppserviceHttp(int port, String hsToken) {
        super(port);
        AppserviceTransactionHandler.hsToken = hsToken;
        addMappings();
    }

    @Override
    public void addMappings() {
        super.addMappings();
        addRoute("/transactions/:txnId", AppserviceTransactionHandler.class);
        addRoute("/_matrix/app/v1/transactions/:txnId", AppserviceTransactionHandler.class);
    }
}
