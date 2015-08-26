package org.kwh.tcp.client;
// Client Side
import java.io.*;
import java.net.*;

public class Client {
  public void run() {
	try {
		int serverPort = 6001;
		InetAddress host = InetAddress.getByName("localhost"); 
		System.out.println("Connecting to server on port " + serverPort); 

		Socket socket = new Socket(host,serverPort); 
		//Socket socket = new Socket("127.0.0.1", serverPort);
		System.out.println("Just connected to " + socket.getRemoteSocketAddress()); 
		PrintWriter toServer = 
			new PrintWriter(socket.getOutputStream(),true);
		BufferedReader fromServer = 
			new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
		//toServer.println("Packet from " + socket.getLocalSocketAddress()); 
		// String packet = "STH:000000,511;L:232;TM:141215014325;D:0;T:01;C:47;A00:52.67;A01:00000;A02:0.445;A03:234.6;A04:1.240;A05:00000;A06:00000;A07:00000;A08:00000;A09:0.200;A10:00000;A11:0.689;A12:50.02;A13:24.43;A14:27.43;K01:10000000000000000;O01:0000;CA";
		String packet = "STA:00001;TM:08/10/2011,13:19:09;C:53;V:7.59;AD01:0.001;AD02:0.002;AD03:0.003;AD04:0.004;AD05:0.005;AD06:0.006;AD07:0.007;AD08:0.008;AD09:0.009;AD10:0.010;AD11:0.011;AD12:0.012;PU01:00000000;PU02:00000005;PU04:00000009;PU05:00000003;DI:330330;DO:000";
		toServer.println(packet);
		String line = fromServer.readLine();
		System.out.println("Client received: " + line + " from Server");
		toServer.close();
		fromServer.close();
		socket.close();
	}
	catch(UnknownHostException ex) {
		ex.printStackTrace();
	}
	catch(IOException e){
		e.printStackTrace();
	}
  }
	
  public static void main(String[] args) {
		Client client = new Client();
		client.run();
  }
}