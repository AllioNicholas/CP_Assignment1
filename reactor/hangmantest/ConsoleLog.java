package hangmantest;

public class ConsoleLog implements HangmanLog {
	public void output(String source, String text) {
		System.err.println(source + ": " + text);
	}
}
