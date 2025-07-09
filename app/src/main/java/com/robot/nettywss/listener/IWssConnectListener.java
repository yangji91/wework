package com.robot.nettywss.listener;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface IWssConnectListener {
    void startConnect();

    void onConnectSucc();

    void connectError(String err);

    void onRecv(WebSocketFrame transmitData);
}
