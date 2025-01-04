package sf.badlagger.urlshort;

import com.beust.jcommander.JCommander;

import sf.badlagger.urlshort.UsersDump.User;

public class App 
{
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
    	
    	UsersDump users = new UsersDump("users.list");
    	
    	if ((arguments.usersPath == null) || !users.setFilePath(arguments.usersPath)) {
    		if (!users.setDefault()) {
    			System.out.println("Oops! errors in users list creatings");
    			return;
    		}
    	}
    	
    	System.out.format("Users number: %d\n", users.getUsersNumber());
    	
    	if ((arguments.id != null)) {
    		User user = users.getUser(arguments.id);
    		if (user == null) {
    			if (users.addNewUser(arguments.id)) {
    				System.out.println("New user was successfully added");
    			}
    			
    			user = users.getUser(arguments.id);
    			if (user == null) {
    				System.out.println("New user lost!!!");
    				return;
    			}
    		}
    		System.out.format("User ID: %s was added at %s hash: %X\n", user.getId(), user.getPrettyDate("dd/MM/yyyy HH:mm:ss"), user.hashCode());
    	}
    }
}
