package weblog;

/*
 * For example, a new contact would be:
 * 
 *  category:	contact
 *  action:		new
 *  message:	{callsign:"T3ST", ... }
 */
public class EventStreamMessage {

	private String category;
	private String action;
	private String message;
	
	public EventStreamMessage(String category, String action, String message) {
		this.setCategory(category);
		this.setAction(action);
		this.setMessage(message);
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "EventStreamMessage [category=" + category + ", action=" + action + ", message=" + message + "]";
	}
		
}
