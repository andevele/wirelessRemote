package cn.feeyan.www.wirelessremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SendUdpDataPacket {

	// 这里的参数为字节数组，里面保存的是16进制，以16进制输出
	public boolean send(byte[] msg) {

		DatagramSocket dSocket = null;

		InetAddress local = null;
		try {

			local = InetAddress.getByName(Constants.HOST_ADDR); // 本机测试
			// local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

		try {

			dSocket = new DatagramSocket();

		} catch (SocketException e) {

			e.printStackTrace();

		}

		int msg_len = msg == null ? 0 : msg.length;
		DatagramPacket dPacket = new DatagramPacket(msg, msg_len, local,
				Constants.SERVER_PORT);

		try {

			dSocket.send(dPacket);

		} catch (IOException e) {
			return false;
		} finally {
			dSocket.close();
		}

		return true;
	}
}
