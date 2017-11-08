package application;

public class IllegalMoveException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor for the Exception which sets the message as to why the exception occurred
	 * @param message The message which crudely tells about the exception description
	 * @author Madhur Tandon
	 */
	public IllegalMoveException(String message)
	{
		super(message);
	}
}

