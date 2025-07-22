package com.robot.nettywss;

import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.ResEntity;
import com.robot.nettywss.listener.IWssConnectListener;
import com.robot.nettywss.proto.IWssNettyTaskInte;
import com.robot.util.MyLog;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
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
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WssNettyClient {
    private final String TAG = getClass().getSimpleName();
    // 与服务端的连接通道
    private Channel channel;
    private IWssConnectListener mConnectListener;
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public WssNettyClient() {

    }

    public void setConnectListener(IWssConnectListener mListener) {
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
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc(), websocketURI.getHost(), MConfiger.env.getPort()));
                            pipeline.addLast(new HttpClientCodec()); // 添加一个http的编解码器
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64)); // 聚合器，使用websocket会用到
                            pipeline.addLast(new ChunkedWriteHandler()); // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new WebSocketClientProtocolHandler(handshaker, true, false));
                            pipeline.addLast(new IdleStateHandler(0, 50, 0, TimeUnit.SECONDS));// 添加心跳支持
                            pipeline.addLast(new HeartbeatHandler());
                            // 添加发送数据编码器
                            // MyLog.debug(TAG, "[startConnect]" + "添加发送数据编码器...");
                            // pipeline.addLast("encoder", new ClientEncoder());
                            // pipeline.addLast("decoder", new NettyMsgDecoder());
                            // 添加数据处理器
                            MyLog.debug(TAG, "[startConnect]" + "添加数据处理器...");
                            pipeline.addLast("handler", new WssClientHandler(handshaker, mConnectListener));
                        }
                    });
            // 连接到服务端
            if (mConnectListener != null) {
                mConnectListener.startConnect();
            }
            MyLog.debug(TAG, "start connect...port:" + MConfiger.env.getPort());
            channel = bootstrap.connect(websocketURI.getHost(), MConfiger.env.getPort()).sync().channel();
            succ = true;
            MyLog.debug(TAG, "start connect complete...");
            // channel.closeFuture().sync();
        } catch (InterruptedException e) {
//            MyLog.error(TAG, e);
            MyLog.debug(TAG, "startConnect: Error InterruptedException" + e);

            if (mConnectListener != null) {
                mConnectListener.connectError(e.getMessage());
            }
        } catch (Throwable ee) {
            // ee.printStackTrace();
//            MyLog.error(TAG, ee);
            MyLog.debug(TAG, "startConnect: Error Throwable" + ee);

            if (mConnectListener != null) {
                mConnectListener.connectError(ee.getMessage());
            }
        }
        return succ;
    }

    public Channel getChannel() {
        return channel;
    }

    public ResEntity sendNettyClient(IWssNettyTaskInte task) {
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
