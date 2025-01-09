package sf.badlagger.urlshort;

import com.beust.jcommander.Parameter;

public class Args {
	private final String ID_NAME = "-id";
	private final String ID_DESCRIPTION = "Set user ID. If it isn't set than it will be auto generated";
	private final String URL_NAME = "-url";
	private final String URL_DESCRIPTION = "Set path to the file with long URLs. If it isn't set or file isn't exist than it will be created in application folder with name url.list";
	private final String USERS_NAME = "-users";
	private final String USERS_DESCRIPTION = "Set path to the file with all users ids. If it isn't set or file isn't exist than it will be created in application folder with name users.list";
	private final String DB_NAME = "-db";
	private final String DB_DESCRIPTION = "Set path to the database file. If it isn't set or file isn't exist than it will be created in application folder with name db.list";
	private final String HELP_NAME = "-h";
	private final String HELP_DESCRIPTION = "Show this message and exit (even if another arguments are presented)";
	private final String CFG_NAME = "-cfg";
	private final String CFG_DESCRIPTION = "Set path to the config file. If it isn't set or  file isn't exist than it will be created in application folder with name sets.cfg";
	private final String SHORT_NAME = "-short";
	private final String SHORT_DESCRIPTION = "Set path to the shorl URLs. If it isn't set or  file isn't exist than it will be created in application folder with name short.cfg";
	
	private final String[][] optionsList = {
			{CFG_NAME, CFG_DESCRIPTION},
			{DB_NAME, DB_DESCRIPTION},
			{ID_NAME, ID_DESCRIPTION},
			{HELP_NAME, HELP_DESCRIPTION},
			{URL_NAME, URL_DESCRIPTION},
			{USERS_NAME, USERS_DESCRIPTION},
			{SHORT_NAME, SHORT_DESCRIPTION},
	};
	
	@Parameter(names = ID_NAME, description = ID_DESCRIPTION)
	public String id = null;
	
	@Parameter(names = URL_NAME, description = URL_DESCRIPTION)
	public String urlPath = null;
	
	@Parameter(names = USERS_NAME, description = USERS_DESCRIPTION)
	public String usersPath = null;
	
	@Parameter(names = DB_NAME, description = DB_DESCRIPTION)
	public String dbPath = null;
	
	@Parameter(names = CFG_NAME, description = CFG_DESCRIPTION)
	public String cfgPath = null;
	
	@Parameter(names = SHORT_NAME, description = SHORT_DESCRIPTION)
	public String shortPath = null;
	
	@Parameter(names = HELP_NAME, description = HELP_DESCRIPTION)
	public boolean help;
	public boolean checkHelp() {
		if (help) {
			System.out.println("Short help:");
			for (String[] cmd : optionsList) {
				System.out.format("\t%s\t- %s\n", cmd[0], cmd[1]);
			}
		}
		return help;
	}
}
