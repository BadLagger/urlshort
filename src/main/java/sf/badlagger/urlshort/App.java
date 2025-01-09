package sf.badlagger.urlshort;

import com.beust.jcommander.JCommander;

public class App 
{
	
	static final int USER_ID_LENGTH = 15;
	
    public static void main( String[] args )
    {
    	Args arguments = new Args();
        
    	JCommander.newBuilder().addObject(arguments).build().parse(args);
    	
    	if(arguments.checkHelp())
    		return;
    	
    	CfgDump cfg = new CfgDump("sets.cfg");
    	
    	if ((arguments.cfgPath == null) || !cfg.setFilePath(arguments.cfgPath)) {
    		if (!cfg.setDefault()) {
    			System.out.println("Oops! errors in cfg creatings");
    			return;
    		}
    	}
    	
    	System.out.format("URL Prefix from config: %s\n", cfg.getPrefix());
    	System.out.format("URL LiveTime from config: %d\n", cfg.getLivetime());
    	
    	JsonSimpleDump users = new JsonSimpleDump("users.list");
    	StringWithDate user = null;
    	
    	if ((arguments.usersPath == null) || !users.setFilePath(arguments.usersPath)) {
    		if (!users.setDefault()) {
    			System.out.println("Oops! errors in users list creatings");
    			return;
    		}
    	}
    	
    	System.out.format("Users number: %d\n", users.getValsNumber());
    	
    	if ((arguments.id != null)) {
    		user = users.getVal(arguments.id);
    		if (user == null) {
    			if (users.addNewVal(arguments.id)) {
    				System.out.println("New user was successfully added");
    			}
    			
    			user = users.getVal(arguments.id);
    			if (user == null) {
    				System.out.println("New user lost!!!");
    				return;
    			}
    		}
    	} else {
    		System.out.println("Generate new User Id");
    		System.out.println("The power of the User Id set is: " + Generator.getUuidsNumber(USER_ID_LENGTH));
    		do {
    			String newId = Generator.getUuid(USER_ID_LENGTH);
    			user = users.getVal(newId);
    			if (user != null) {
    				user = null;
    				continue;
    			} else {
    				if (!users.addNewVal(newId)) {
    					System.out.format("User with id %s already exists! Re-generate\n", newId);
    					continue;
    				}
    				System.out.println("New user was successfully generated");
    				user = users.getVal(newId);
    			}
    		} while (user == null);
    	}
    	
    	System.out.format("User ID: %s was added at %s hash: %X\n", user.getVal(), user.getPrettyDate("dd/MM/yyyy HH:mm:ss"), user.hashCode());
    	
    	users.removeVal("12345687");
    }
}
