package sf.badlagger.urlshort;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UsersDump extends FileDump {

	public class User {
		private String id = null;
		private Calendar date = null;
		
		public User(String id, Calendar date) {
			this.id = id;
			this.date = date;
		}
		
		public User(String id) {
			this.id = id;
			this.date = Calendar.getInstance();
		}
		
		public String getId() {
			return id;
		}
		
		public Calendar getDate() {
			return date;
		}
		
		public String getPrettyDate(String format) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date.getTime());
		}
	}
	
	JsonNode rootNode = null;
	
	protected UsersDump(String defaultName) {
		super(defaultName);
	}
	
	private boolean loadDataToJson() {
		try {
			rootNode = new ObjectMapper().readTree(dataString);
			return true;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean checkData() {
		
		if (dataString.length() > 0) { 
			return loadDataToJson();
		}
		
		return createDefaultData();
	}

	@Override
	protected boolean createDefaultData() {
		dataString = "{}";
		
		if (loadDataToJson()) {
			return save();
		}
		return false;
	}
	
	public boolean checkUser(String id) {
		
		if (rootNode == null)
			return false;
		
		if (rootNode.get(id) == null) {
			return false;
		}
		return true;
	}
	
	public User getUser(String id) {
		
		if (rootNode == null)
			return null;
		
		JsonNode userNode = rootNode.get(id);
		
		if (userNode == null)
			return null;
		
		long dateMs = userNode.asLong();
		
		if (dateMs == 0) {
			return null;
		}
		
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(dateMs);
		
		return new User(id, date);
	}
	
	public int getUsersNumber() {
		return (rootNode != null) ? rootNode.size() : 0;
	}
	
	public boolean addNewUser(String id) {
		
		if (rootNode == null)
			return false;
		
		if (!checkUser(id)) {
			User user = new User(id);
			((ObjectNode)rootNode).put(user.getId(), user.getDate().getTime().getTime());
			try {
				dataString =  (new ObjectMapper()).writeValueAsString(rootNode);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return false;
			}
			System.out.format("DataString: %s\n", dataString);
			return save();
		}
		
		return false;
	}
}
