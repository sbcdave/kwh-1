package org.kwh.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.WriteTimeoutHandler;

public class GraphiteClient {

	private static final EventLoopGroup group = new NioEventLoopGroup();
    private static final StringEncoder ENCODER = new StringEncoder();
    private static final WriteTimeoutHandler TIMEOUT_HANDLER = new WriteTimeoutHandler(120);
    private static final Bootstrap bootstrap = new Bootstrap();
    private boolean started;
    private static String graphiteHost = System.getProperty("org.kwh.graphite.server.host","localhost");
    private static Integer graphitePort = Integer.parseInt(System.getProperty("org.kwh.graphite.server.port","2003"));
    private static final GraphiteClient graphiteClient = new GraphiteClient();
    static Logger logger = LoggerFactory.getLogger(GraphiteClient.class);

    private Channel connection;

    private GraphiteClient() {
        this.started = false;
    }

    public static GraphiteClient getClient() {
    	return graphiteClient;
    }
    
    public void startUp() {
        try {
            if(bootstrap.group() == null) {
                bootstrap.group(group);
            }
            bootstrap.channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ChannelInitializer<SocketChannel>() {
            	@Override
            	public void initChannel(SocketChannel ch) throws Exception {
            		ch.pipeline().addLast(ENCODER);
                    ch.pipeline().addLast(TIMEOUT_HANDLER);
                }
            });
            ChannelFuture f = bootstrap.connect(graphiteHost, graphitePort).sync();
            this.connection = f.channel();
            this.started = true;
            f.channel().closeFuture().sync();        
        } catch (Exception ex) {
            ex.printStackTrace();
            group.shutdownGracefully();
        } 	        	
    }

    public void sendData(String data) {
    	if (this.started) {
    		logger.info("Sending datapoint to Carbon...");
    		this.connection.writeAndFlush(data);
    		logger.info("Datapoint has been sent to Carbon");
    	} else {
    		logger.warn("Connection to Graphite Server has failed or has not been started");
    		logger.warn("Shutting down gracefully");
    		shutdown();
    	}
        
    }

    public void shutdown() {
        if (connection != null) {
            connection.close().awaitUninterruptibly();
        }
        logger.info("--- GRAPHITE CLIENT - Stopped.");
    }

}