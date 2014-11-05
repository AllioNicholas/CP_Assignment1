package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import reactorapi.Handle;

public class PlayerHandle implements Handle<String> {
	private final Socket socket;
	private final BufferedReader reader;
	private final PrintWriter writer;
	
	public PlayerHandle(final Socket socket) throws IOException {
		this.socket = socket;
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(socket.getOutputStream(), true);
	}

	public String read() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	
	public void write(final String s) {
		writer.println(s);
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
}
