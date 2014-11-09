package reactor;

import reactorapi.BlockingQueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {
	private final Object GetLock = new Object();
	private final Object PutLock = new Object();
	private final Queue<Event<? extends T>> queue;
	private final int capacity;
	private volatile int size = 0;

	public BlockingEventQueue(final int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Capacity must be bigger than 0");
		this.capacity = capacity;
		this.queue = new ArrayDeque<Event<? extends T>>(capacity);
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
				assert (event != null);
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
				size = 0;
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