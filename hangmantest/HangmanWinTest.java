package hangmantest;

public class HangmanWinTest extends HangmanTest {
	protected String[] getPlayers() {
		return new String[] { "Foo", "Bar", "Zoq", "Fot", "Pik" };
	}

	protected void executeGameSequence() throws HangmanFailureException {
		for (int i = 0; i < names.length; i++)
			transmit(i, names[i]);
		expect("----------- 10");
		transmit(0, 'c');
		expect("c c--c-----c- 10 Foo");
		transmit(1, 'y');
		expect("y c--c-----cy 10 Bar");
		transmit(4, 'z');
		expect("z c--c-----cy 9 Pik");
		transmit(4, 'y');
		expect("y c--c-----cy 9 Pik");
		transmit(3, 'e');
		expect("e c--c---e-cy 9 Fot");
		transmit(0, 'n');
		expect("n c-nc---ency 9 Foo");
		transmit(0, 'r');
		expect("r c-nc-rrency 9 Foo");
		transmit(0, 'o');
		expect("o conc-rrency 9 Foo");
		transmit(0, 'u');
		expect("u concurrency 9 Foo");
	}

	public static void main(String[] args) {
		new HangmanWinTest().run(args);
	}
}
