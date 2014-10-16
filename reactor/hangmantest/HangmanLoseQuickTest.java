package hangmantest;

public class HangmanLoseQuickTest extends HangmanLoseTest {
	protected void executeGameSequence() throws HangmanFailureException {
		for (int i = 0; i < names.length; i++)
			transmit(i, names[i]);
		expect("------ 3");
		transmit(0, 'c');
		expect("c ------ 2 Foo");
		transmit(2, 'z');
		expect("z ------ 1 Zoq");
		transmit(2, 'l');
		expect("l ------ 0 Zoq");
	}

	public static void main(String[] args) {
		new HangmanLoseQuickTest().run(args);
	}
}
