package nl.focalor.utobot.hipchat.model;

public class Message {
	private String message;
	private User from;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

}
