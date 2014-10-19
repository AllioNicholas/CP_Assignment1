package reactor;

import java.util.*;
import reactorapi.*;

public class Dispatcher {

	private List<EventHandler<?>> eventHandlerList;
	private BlockingEventQueue<Object> blockingQueue;
	private List<WorkerThread<?>> workerThreadList;

	public Dispatcher() {
		this.blockingQueue = new BlockingEventQueue<>(10);
		this.eventHandlerList = new LinkedList<EventHandler<?>>();
		this.workerThreadList = new LinkedList<WorkerThread<?>>();
	}

	public Dispatcher(int capacity) {
		this.blockingQueue = new BlockingEventQueue<>(capacity);
		this.eventHandlerList = new LinkedList<EventHandler<?>>();
		this.workerThreadList = new LinkedList<WorkerThread<?>>();
	}

	public void handleEvents() throws InterruptedException {
		while (eventHandlerList.size() > 0) {
			Event<?> e = select();
			if (eventHandlerList.contains(e.getHandler())) {
				e.handle();
			}
		}
	}

	public Event<?> select() throws InterruptedException {
		Event<?> e = this.blockingQueue.get();
		return e;
	}

	public void addHandler(EventHandler<?> h) {
		WorkerThread<?> wThread = new WorkerThread<>(h, this.blockingQueue);
		eventHandlerList.add(h);
		workerThreadList.add(wThread);
		wThread.start();
	}

	public void removeHandler(EventHandler<?> h) {
		int workerIndex = eventHandlerList.indexOf(h);
		workerThreadList.get(workerIndex).cancelThread();
		eventHandlerList.remove(h);
	}
}
