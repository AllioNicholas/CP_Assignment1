package hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import reactorapi.Handle;

public class ServerSocketHandle implements Handle<Socket> {
	private final ServerSocket socket;
	
	public ServerSocketHandle(final int port) throws IOException {
		socket = new ServerSocket(port);
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
