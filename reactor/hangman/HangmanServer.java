package hangman;

import hangmanrules.HangmanRules;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import reactor.Dispatcher;
import reactorapi.EventHandler;
import reactorapi.Handle;

public class HangmanServer {
	private final Dispatcher dispatcher;
	private final HangmanRules<PlayerHandle> rules;

	// TODO: nicer way of doing this?
	private final ServerSocketHandler serverSocketHandler;	
	private final LinkedList<PlayerHandler> playerHandlers;
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Call the server like: java hangman.HangmanServer <word to guess> <number of failed attempts before termination>");
			return;
		}
		String word_to_guess = args[0];
		Integer num_attempts = Integer.parseInt(args[1]);
		if (num_attempts < 0) {
			System.out.println("The number of failed attempts must be at least 0");
			return;
		}
		HangmanServer server;
		try {
			server = new HangmanServer(word_to_guess, num_attempts);
		} catch (IOException e) {
			System.out.println("Could not initialize the server");
			e.printStackTrace();
			return;
		}
		server.start();
	}
	
	public HangmanServer(final String word, final int attempts) throws IOException {
		this.dispatcher = new Dispatcher();
		this.rules = new HangmanRules<PlayerHandle>(word, attempts); 
		this.playerHandlers = new LinkedList<PlayerHandler>();
		this.serverSocketHandler = new ServerSocketHandler();
		dispatcher.addHandler(serverSocketHandler);
	}
	
	public void start() {
		try {
			dispatcher.handleEvents();
		} catch (InterruptedException e) {
			System.out.println("Server got interrupted");
		}
	}
	
	public class PlayerHandler implements EventHandler<String> {
		private final PlayerHandle handle;
		private boolean uninitialized = true;
		private HangmanRules<PlayerHandle>.Player player = null;
		
		public PlayerHandler(Socket s) throws IOException {
			handle = new PlayerHandle(s);
		}

		public Handle<String> getHandle() {
			return handle;
		}

		public void handleEvent(String s) {
			if (s == null) {
				if (player != null) {
					rules.removePlayer(player);
					player = null;
				}
				playerHandlers.remove(this);
				dispatcher.removeHandler(this);
				return;
			}
			
			if (uninitialized) {
				player = rules.addNewPlayer(handle, s);
				handle.write(rules.getStatus());
				uninitialized = false;
			}
			else {
				if (s.length() != 1)
					return; // TODO: do something else?
				rules.makeGuess(s.charAt(0));
				String result = player.getGuessString(s.charAt(0));
				for (HangmanRules<PlayerHandle>.Player player : rules.getPlayers()) {
					player.playerData.write(result);
				}
				
				if (rules.gameEnded()) {
					ServerSocketHandle serverSocketHandle = (ServerSocketHandle)serverSocketHandler.getHandle();
					serverSocketHandle.close();
					dispatcher.removeHandler(serverSocketHandler);
					for (PlayerHandler handler : playerHandlers) {
						PlayerHandle handle = (PlayerHandle)handler.getHandle();
						handle.close();
						dispatcher.removeHandler(handler);
					}
				}
			}
		}
		
	}

	public class ServerSocketHandler implements EventHandler<Socket> {
		private final ServerSocketHandle handle;
		
		public ServerSocketHandler() throws IOException {
			handle = new ServerSocketHandle();
		}

		public Handle<Socket> getHandle() {
			return handle;
		}

		public void handleEvent(Socket s) {
			PlayerHandler playerHandler;
			try {
				playerHandler = new PlayerHandler(s);
			} catch (IOException e) {
				System.out.println("Unsuccessful connection attempt");
				return;
			}
			playerHandlers.add(playerHandler);
			dispatcher.addHandler(playerHandler);
		}

	}

}
