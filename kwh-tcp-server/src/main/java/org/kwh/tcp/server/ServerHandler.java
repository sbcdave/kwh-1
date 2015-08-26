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

import org.kwh.tcp.util.Parser;
import org.kwh.tcp.util.Record;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	public ServerHandler() {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		try {
			System.out.println("channelRead");
			String receivedContent = in
					.toString(io.netty.util.CharsetUtil.US_ASCII);
			new Consumer(receivedContent).start();
			// send back message to the datalogger to notify it the bytes were
			// correctly received
			ctx.writeAndFlush("@888");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		System.out.println("Exception caught in ServerHandler");
		cause.printStackTrace();
		ctx.close();
	}

	private class Consumer extends Thread {

		private final String receivedContent;

		public Consumer(String receivedContent) {
			this.receivedContent = receivedContent;
		}

		@Override
		public void run() {
			boolean sendToCarbon = Boolean.parseBoolean(System.getProperty(
					"org.kwh.send.carbon", "true"));
			try {
				if (!receivedContent.startsWith("@")) {
					// Parse packet
					Record newRecord = Parser.toRecord(receivedContent);

					// Send data to Carbon if property activated
					if (sendToCarbon) {
						List<String> channelData = newRecord.toGraphite();
						for (String chanelSample : channelData) {
							GraphiteClient.getClient().sendData(chanelSample);
						}
					} else {
						System.out
								.println("Warning: \"org.kwh.send.carbon\" is set false. "
										+ "Data won't be sent to the databaseÏ");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}