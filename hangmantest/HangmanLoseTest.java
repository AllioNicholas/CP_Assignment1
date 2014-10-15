package hangmantest;

public class HangmanLoseTest extends HangmanTest {
	protected String[] getPlayers() {
		return new String[] { "Foo", "Bar", "Zoq" };
	}

	protected void executeGameSequence() throws HangmanFailureException {
		for (int i = 0; i < names.length; i++)
			transmit(i, names[i]);
		expect("------ 3");
		transmit(0, 'c');
		expect("c ------ 2 Foo");
		transmit(1, 'w');
		expect("w -w---- 2 Bar");
		transmit(2, 'z');
		expect("z -w---- 1 Zoq");
		transmit(2, 'l');
		expect("l -w---- 0 Zoq");
	}

	public static void main(String[] args) {
		new HangmanLoseTest().run(args);
	}
}
