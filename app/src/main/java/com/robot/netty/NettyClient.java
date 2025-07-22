package com.robot.netty;

import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.ResEntity;
import com.robot.netty.listener.IConnectListener;
import com.robot.netty.proto.INettyTaskInte;
import com.robot.util.MyLog;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {
    private final String TAG = getClass().getSimpleName();
    // 与服务端的连接通道
    private Channel channel;
    private IConnectListener mConnectListener;
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public NettyClient() {

    }

    public void setConnectListener(IConnectListener mListener) {
        this.mConnectListener = mListener;
    }

    public boolean startConnect() {
        boolean succ = false;
        // 获取连接通道
        try {
            URI websocketURI = new URI(MConfiger.env.getSocketIp(Global.getDeviceSn(), String.valueOf(Global.getRemoteId())));
            MyLog.debug(TAG, "[websocketURI]" + websocketURI);
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                    websocketURI,
                    WebSocketVersion.V13,
                    null,
                    true,
                    new DefaultHttpHeaders());
            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            group = new NioEventLoopGroup();
            bootstrap = new Bootstrap()
                    // 指定channel类型
                    .channel(NioSocketChannel.class)
                    // 指定EventLoopGroup
                    .group(group)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 1024 * 1024 * 10)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 指定Handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addFirst(sslCtx.newHandler(socketChannel.alloc(), websocketURI.getHost(), websocketURI.getPort()));
                            pipeline.addLast(new HttpClientCodec()); // 添加一个http的编解码器
                            pipeline.addLast(new ChunkedWriteHandler()); // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64)); // 聚合器，使用websocket会用到
                            pipeline.addLast(new IdleStateHandler(3, 3, 3, TimeUnit.SECONDS));// 添加心跳支持

                            // 添加发送数据编码器
                            MyLog.debug(TAG, "[startConnect]" + "添加发送数据编码器...");
//                            pipeline.addLast("encoder", new ClientEncoder());
//                            pipeline.addLast("decoder", new NettyMsgDecoder());
                            // 添加数据处理器
                            MyLog.debug(TAG, "[startConnect]" + "添加数据处理器...");
                            pipeline.addLast("handler", new ClientHandler(mConnectListener));
                        }
                    });
            // 连接到服务端
            if (mConnectListener != null) {
                mConnectListener.startConnect();
            }
            MyLog.debug(TAG, "start connect...");
            ChannelFuture channelFuture = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort());
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    MyLog.debug(TAG, "[operationComplete]" + " channelFuture..." + "连接成功");
                    handshaker.handshake(future.channel());
                } else {
                    MyLog.debug(TAG, "[operationComplete]" + " channelFuture..." + "连接失败");
                }
            });
            channel = channelFuture.sync().channel();
            succ = true;
            MyLog.debug(TAG, "start connect complete...");
        } catch (InterruptedException e) {
            MyLog.debug(TAG, "startConnect: Error" + e);
            if (mConnectListener != null) {
                mConnectListener.connectError(e.getMessage());
            }
        } catch (Throwable ee) {
            // ee.printStackTrace();
            MyLog.debug(TAG, "startConnect: Error Throwable" + ee);
            if (mConnectListener != null) {
                mConnectListener.connectError(ee.getMessage());
            }
        }
        return succ;
    }

    public ResEntity sendNettyClient(INettyTaskInte task) {
        ResEntity<?> resEntity = task.sendMsg(channel);
        MyLog.debug(TAG, "[sendPushMessage]" + "消息已发送...task");
        if (resEntity != null)
            return resEntity;
        return ResEntity.genErr(null, "实现类未处理...");
    }

    public boolean isNettyConncted() {
        if (channel != null) {
            return channel.isActive();
        }
        return false;
    }

    public void closeConnected() {
        if (channel != null) {
            // 1. 要获取一个 CloseFuture对象，同样它也有同步处理关闭和异步处理关闭两种方式
            ChannelFuture closeFuture = channel.closeFuture();
            // 2. 异步处理，上面真正执行关闭channel的EventLoop线程 成功关闭channel后就会调用这里面的方法
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (group != null) {
                        MyLog.debug(TAG, "[operationComplete]" + "shutdownGracefully");
                        group.shutdownGracefully();
                    }
                }
            });
            channel.disconnect();
            channel.close();
            channel = null;
        }
    }
}
