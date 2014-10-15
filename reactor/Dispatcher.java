package reactor;

import java.util.List;

import reactorapi.*;

public class Dispatcher {
	
	private List<EventHandler> eventHandlerList;
	private BlockingEventQueue blockingQueue;
	
	public Dispatcher() {
		this.blockingQueue = new BlockingEventQueue<>(10);
	}

	public Dispatcher(int capacity) {
		this.blockingQueue = new BlockingEventQueue<>(capacity);
	}

	public void handleEvents() throws InterruptedException {
//		while (true) {															
			Event<?> e = select();
			
//		}	
	}

	public Event<?> select() throws InterruptedException {
			Event<?> e = this.blockingQueue.get();
			for(int i=0; i<eventHandlerList.size(); i++) {
				EventHandler<?> eH = eventHandlerList.get(i);
				if(eH == e.getHandler()) {
					return e;
				}
			}
			return null;											//null returned when no handler is inside the List
	}

	public void addHandler(EventHandler<?> h) {
			eventHandlerList.add(h);
	}

	public void removeHandler(EventHandler<?> h) {
		eventHandlerList.remove(h);
	}

	// Add methods and fields as needed.
}
