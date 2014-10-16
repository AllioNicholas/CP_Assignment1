package reactor;

import reactorapi.BlockingQueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	private final Object PutLock = new Object();
	private final Object GetLock = new Object();
	private final Queue<Event<? extends T>> queue;
	private final int capacity;
	private volatile int size;
	
	public BlockingEventQueue(final int capacity) {
		assert(capacity > 0);
		queue = new ArrayDeque<Event<? extends T>>(capacity);
		this.capacity = capacity;
		this.size = 0;
	}

	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return capacity;
	}

	public Event<? extends T> get() throws InterruptedException {
		Event<? extends T> event;
		synchronized (GetLock) {
			while (queue.isEmpty())
				GetLock.wait();
			synchronized (this) {
				event = queue.poll();
				size = queue.size(); 
			}
		}
		synchronized (PutLock) {
			PutLock.notify();
		}
		return event;
	}

	public List<Event<? extends T>> getAll() {
		List<Event<? extends T>> eventList;
		synchronized (GetLock) {
			synchronized (this) {
				eventList = new ArrayList<Event<? extends T>>(queue);
				queue.clear();
				size = queue.size(); 
			}
		}
		synchronized (PutLock) {
			PutLock.notifyAll();
		}
		return eventList;
	}

	public void put(final Event<? extends T> event) throws InterruptedException {
		synchronized (PutLock) {
			while (queue.size() >= capacity)
				PutLock.wait();
			synchronized (this) {
				queue.add(event);
				size = queue.size(); 
			}
		}
		synchronized (GetLock) {
			GetLock.notify();
		}
	}
}