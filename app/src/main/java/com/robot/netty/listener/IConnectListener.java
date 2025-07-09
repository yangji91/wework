package com.robot.netty.listener;

import com.robot.netty.entity.TransmitData;

public interface IConnectListener {
    void startConnect();

    void onConnectSucc();

    void connectError(String err);

    void onRecv(TransmitData transmitData);
}
