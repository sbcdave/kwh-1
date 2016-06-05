package org.kwh.tcp.server;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kwh.tcp.util.Parser;
import org.kwh.tcp.util.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	// private static ExecutorService es = Executors.newCachedThreadPool();
	private static ExecutorService es = Executors.newFixedThreadPool(4);
	static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	public ServerHandler() {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		try {
			logger.info("New packet received");
			String receivedContent = in
					.toString(io.netty.util.CharsetUtil.US_ASCII);
			// send back message to the datalogger to notify it the bytes were
			// correctly received
			byte[] response = "@888\n".getBytes();
			final ByteBuf buffer = ctx.alloc().buffer(response.length);
			buffer.writeBytes(response);
			ctx.writeAndFlush(buffer);
			Consumer cons = new Consumer(receivedContent);
			es.submit(cons);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.error("Exception caught in ServerHandler");
		cause.printStackTrace();
		ctx.close();
	}

	private class Consumer extends Thread {

		private final String receivedContent;

		public Consumer(String receivedContent) {
			logger.info("A new consumer is created to handle a new packet");
			this.receivedContent = receivedContent;
		}

		@Override
		public void run() {
			try {
				if (!receivedContent.startsWith("@")) {
					// Parse packet
					logger.info("A new packet is being parsed...");
					Record newRecord = Parser.toRecord(receivedContent);
					logger.info("A new packet has been parsed and split in several datapoints.");
					
					List<String> channelData = newRecord.toGraphite();
					for (String chanelSample : channelData) {
						logger.info("A new datapoint is being sent to Carbon: " + chanelSample);
						GraphiteClient.getClient().sendData(chanelSample);					
					}
					logger.info("All the datapoints have been sent to Carbon...");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}