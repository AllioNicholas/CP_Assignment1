package reactor;

import java.util.List;

import reactorapi.*;

public class Dispatcher {

	private List<EventHandler<?>> eventHandlerList;
	private BlockingEventQueue<Object> blockingQueue;
	private List<WorkerThread<?>> workerThreadList;

	public Dispatcher() {
		this.blockingQueue = new BlockingEventQueue<>(10);
	}

	public Dispatcher(int capacity) {
		this.blockingQueue = new BlockingEventQueue<>(capacity);
	}

	public void handleEvents() throws InterruptedException {
		while (true) {
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
		wThread.start();
		workerThreadList.add(eventHandlerList.indexOf(h), wThread);
	}

	public void removeHandler(EventHandler<?> h) {
		int workerIndex = workerThreadList.lastIndexOf(h);
		workerThreadList.get(workerIndex).cancelThread();
		eventHandlerList.remove(h);
	}
}
