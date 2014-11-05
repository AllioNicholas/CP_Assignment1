package hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import reactorapi.Handle;

public class ServerSocketHandle implements Handle<Socket> {
	private final ServerSocket socket;
	
	public ServerSocketHandle() throws IOException {
		socket = new ServerSocket(0);
		System.out.println(socket.getLocalPort());
	}

	public Socket read() {
		try {
			return socket.accept();
		} catch (IOException e) {
			return null;
		}
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
	
}
