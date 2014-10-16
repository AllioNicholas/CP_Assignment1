package hangmantest;

import java.io.*;
import java.net.*;

public abstract class HangmanTest {
	PrintStream[] in;
	BufferedReader[] out;
	String[] names;
	Socket[] sockets;
	HangmanLog log;

	public static final int ITERATIONS = 50;

	public void run(HangmanLog l, int port) throws HangmanFailureException {
		log = l;
		names = getPlayers();

		sockets = new Socket[names.length];
		in = new PrintStream[names.length];
		out = new BufferedReader[names.length];

		for (int i = 0; i < names.length; i++) {
			try {
				sockets[i] = new Socket(InetAddress.getByName("localhost"),
						port);
				in[i] = new PrintStream(sockets[i].getOutputStream());
				out[i] = new BufferedReader(new InputStreamReader(
						sockets[i].getInputStream()));
			} catch (IOException ie) {
				fail("Error opening socket: " + ie.getMessage());
			}
		}

		log.output("TEST", "Connections opened.");
		try {
			executeGameSequence();
		} finally {
			for (int i = 0; i < names.length; i++) {
				try {
					sockets[i].close();
				} catch (IOException ie) {
					/* It doesn't get any more closed than this. */
				}
			}
		}
		log.output("TEST", "Connections closed.");
	}

	protected abstract void executeGameSequence()
			throws HangmanFailureException;

	protected abstract String[] getPlayers();

	protected void transmit(int p, char c) {
		in[p].println(c);
	}

	protected void transmit(int p, String c) {
		in[p].println(c);
	}

	protected void expect(String r) throws HangmanFailureException {
		log.output("TEST", "Expecting: " + r);
		for (int i = 0; i < names.length; i++) {
			String s;
			try {
				s = out[i].readLine();
			} catch (IOException ie) {
				fail("Exception on player connection: " + ie.getMessage());
				throw new Error("Failed to fail.");
			}
			if (s == null)
				fail("Server connection lost.");
			String[] ew = r.split(" ");
			String[] gw = s.split(" ");
			int gi = 0;
			for (int ei = 0; ei < ew.length; ei++) {
				while ((gi < gw.length) && (gw[gi].equals(""))) {
					gi++;
				}
				if (gi >= gw.length)
					fail("Missing field: expected <" + r + "> instead of <" + s
							+ ">");

				if (!ew[ei].equalsIgnoreCase(gw[gi]))
					fail("Incorrect field: expected <" + r + "> instead of <"
							+ s + ">");
				gi++;
			}
			/* Superfluous fields ignored on purpose. */
		}
		log.output("TEST", "Got: " + r);
	}

	public void fail(String s) throws HangmanFailureException {
		throw new HangmanFailureException(s);
	}

	public void run(String[] args) {
		int port;
		try {
			port = new Integer(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("Give server port number as first argument.");
			return;
		}
		try {
			run(new ConsoleLog(), port);
		} catch (HangmanFailureException e) {
			e.printStackTrace();
		}
	}
}
