package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;

	// Additional fields are allowed.

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> q) {
		handler = eh;
		queue = q;
	}

	public void run() {
		// TODO: Implement WorkerThread.run().
	}

	public void cancelThread() {
		// TODO: Implement WorkerThread.cancelThread().
	}
}