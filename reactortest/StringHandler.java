package reactortest;

import reactor.*;
import reactorapi.*;

/**
 * Event handler that receives characters from a (creator-supplied) StringHandle
 * and collects them in a String.
 */
class StringHandler implements EventHandler<Character> {
	StringHandle sh;
	StringBuffer sb = new StringBuffer();
	Dispatcher d;

	public StringHandler(Dispatcher d, StringHandle sh) {
		this.sh = sh;
		this.d = d;
	}

	public StringHandle getHandle() {
		return sh;
	}

	public void handleEvent(Character s) {
		if (s == null)
			d.removeHandler(this);
		else
			sb.append(s);
	}

	public String toString() {
		return sb.toString();
	}
}
