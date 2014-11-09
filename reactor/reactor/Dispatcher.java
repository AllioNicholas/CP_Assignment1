package reactor;

import java.util.*;
import reactorapi.*;

public class Dispatcher {

	private final List<EventHandler<?>> eventHandlerList;
	private final BlockingEventQueue<Object> blockingQueue;
	private final List<WorkerThread<?>> workerThreadList;

	public Dispatcher() {
		this(10);
	}

	public Dispatcher(int capacity) {
		blockingQueue = new BlockingEventQueue<Object>(capacity);
		eventHandlerList = new LinkedList<EventHandler<?>>();
		workerThreadList = new LinkedList<WorkerThread<?>>();
	}

	public void handleEvents() throws InterruptedException {
		while (eventHandlerList.size() > 0) {
			final Event<?> e = select();
			if (eventHandlerList.contains(e.getHandler())) {
				e.handle();
			}
		}
	}

	public Event<?> select() throws InterruptedException {
		return blockingQueue.get();
	}

	public <T> void addHandler(EventHandler<T> h) {
		if (eventHandlerList.contains(h))
			return;
		final WorkerThread<T> wThread = new WorkerThread<T>(h, blockingQueue);
		eventHandlerList.add(h);
		workerThreadList.add(wThread);
		wThread.start();
	}

	public <T> void removeHandler(EventHandler<T> h) {
		final int index = eventHandlerList.indexOf(h);
		if (index == -1)
			return;
		workerThreadList.get(index).cancelThread();
		workerThreadList.remove(index);
		eventHandlerList.remove(index);
	}
}
