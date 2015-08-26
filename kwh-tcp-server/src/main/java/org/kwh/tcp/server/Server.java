package org.kwh.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server { 
	
	private final int port;

    public Server(int port) {
        this.port = port;
    }
	
	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        	ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     // Decoders
                     //ch.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(80));
                     //ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                	 ch.pipeline().addLast(new ServerHandler());
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception caught in Server");
        } finally {
        	System.out.println("shutdownGracefully Server");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
		
	public static void main(String[] args) throws Exception {
		int port;
        // Retrieve port specified in System properties. Used port 6001 as default
        port = Integer.parseInt(System.getProperty("org.kwh.tcp.server.port","6001"));
        new Server(port).run();
	}
}