package org.kwh.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server { 
	
	private final int port;
	private boolean sendToCarbon = false;
	private static ExecutorService es = Executors.newSingleThreadExecutor();

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

            this.sendToCarbon = Boolean.parseBoolean(System.getProperty(
					"org.kwh.send.carbon", "false"));
            if (sendToCarbon) {
            	// Bind the server to Graphite if sendToCarbon is true
            	BindToGraphite cons = new BindToGraphite();
            	es.submit(cons);
            } else {
            	System.out
				.println("Warning: \"org.kwh.send.carbon\" is set false. "
						+ "Data won't be sent to the databaseÏ");
            }
            
            // Bind and start to accept incoming connections from dataloggers.
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
	
	private class BindToGraphite extends Thread {

		public BindToGraphite() {

		}

		@Override
		public void run() {
			try {
				// bind the server to Graphite
				GraphiteClient.getClient().startUp();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	public static void main(String[] args) throws Exception {
		int port;
        // Retrieve port specified in System properties. Used port 6001 as default
        port = Integer.parseInt(System.getProperty("org.kwh.tcp.server.port","6001"));
        new Server(port).run();
	}
}