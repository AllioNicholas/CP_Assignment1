package reactor;

import reactorapi.*;

public class Dispatcher {
	public Dispatcher() {
		this(10);
	}

	public Dispatcher(int capacity) {
		// TODO: Implement Dispatcher(int).
	}

	public void handleEvents() throws InterruptedException {
		// TODO: Implement Dispatcher.handleEvents().
	}

	public Event<?> select() throws InterruptedException {
		throw new UnsupportedOperationException();
		// TODO: Implement Dispatcher.select().
	}

	public void addHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.addHandler(EventHandler).
		//hello
	}

	public void removeHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.removeHandler(EventHandler).
	}

	// Add methods and fields as needed.
}
