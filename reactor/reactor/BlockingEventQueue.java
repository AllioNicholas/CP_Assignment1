package reactor;

import reactorapi.BlockingQueue;
import java.util.List;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	public BlockingEventQueue(int capacity) {
		// TODO: Implement BlockingEventQueue(int).
	}

	public int getSize() {
		throw new UnsupportedOperationException();
		// TODO: Implement BlockingEventQueue.getSize().
	}

	public int getCapacity() {
		throw new UnsupportedOperationException(); // Replace this.
		// TODO: Implement BlockingEventQueue.getCapacity().
	}

	public Event<? extends T> get() throws InterruptedException {
		throw new UnsupportedOperationException(); // Replace this.
		// TODO: Implement BlockingEventQueue.get().
	}

	public synchronized List<Event<? extends T>> getAll() {
		throw new UnsupportedOperationException(); // Replace this.
		// TODO: Implement BlockingEventQueue.getAll().
	}

	public void put(Event<? extends T> event) throws InterruptedException {
		// TODO: Implement BlockingEventQueue.put(Event).
	}

	// Add other methods and variables here as needed.
}