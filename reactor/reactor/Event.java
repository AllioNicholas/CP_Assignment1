package reactor;

import reactorapi.EventHandler;

public class Event<T> {
	private final T event;
	private final EventHandler<T> handler;

	public Event(T e, EventHandler<T> eh) {
		event = e;
		handler = eh;
	}

	public T getEvent() {
		return event;
	}

	public EventHandler<T> getHandler() {
		return handler;
	}

	public void handle() {
		handler.handleEvent(event);
	}
}