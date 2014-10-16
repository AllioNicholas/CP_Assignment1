package hangmantest;

@SuppressWarnings("serial")
public class HangmanFailureException extends Exception {
	public HangmanFailureException(String excuse) {
		super(excuse);
	}
}
