package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;
	
	private volatile boolean cancelled = false;

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> q) {
		handler = eh;
		queue = q;
	}

	public void run() {
		while (!cancelled) {
			final T value = handler.getHandle().read();
			final Event<T> event = new Event<T>(value, handler);
			try {
				queue.put(event);
			} catch (InterruptedException e) {
				System.err.println("Worker thread got interrupted. Terminating...");
				return;
			}
			if (value == null || interrupted())
				return;
		}
	}

	public void cancelThread() {
		cancelled = true;
	}
}