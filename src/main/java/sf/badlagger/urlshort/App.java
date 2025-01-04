package sf.badlagger.urlshort;

import com.beust.jcommander.JCommander;

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
    }
}
