package reactor;

import java.util.*;
import reactorapi.*;

public class Dispatcher {

	private List<EventHandler<?>> eventHandlerList;
	private BlockingEventQueue<Object> blockingQueue;
	private List<WorkerThread<?>> workerThreadList;

	public Dispatcher() {
		this.blockingQueue = new BlockingEventQueue<Object>(10);
		this.eventHandlerList = new LinkedList<EventHandler<?>>();
		this.workerThreadList = new LinkedList<WorkerThread<?>>();
	}

	public Dispatcher(int capacity) {
		this.blockingQueue = new BlockingEventQueue<Object>(capacity);
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

	public <T> void addHandler(EventHandler<T> h) {
		WorkerThread<T> wThread = new WorkerThread<T>(h, this.blockingQueue);
		eventHandlerList.add(h);
		workerThreadList.add(wThread);
		wThread.start();
	}

	public <T> void removeHandler(EventHandler<T> h) {
		int index = eventHandlerList.indexOf(h);
		workerThreadList.get(index).cancelThread();
		workerThreadList.remove(index);
		eventHandlerList.remove(index);
	}
}
