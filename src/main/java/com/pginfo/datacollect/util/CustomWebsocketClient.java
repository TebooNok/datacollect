package com.pginfo.datacollect.util;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class CustomWebsocketClient extends WebSocketClient {

    Logger logger = LoggerFactory.getLogger(CustomWebsocketClient.class);
    private String retMsg;

    public CustomWebsocketClient(URI serverUri) {
        super(serverUri, new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.debug("Open websocket to" + this.uri.toString());
    }

    @Override
    public void onMessage(String s) {
        this.retMsg = s;
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
