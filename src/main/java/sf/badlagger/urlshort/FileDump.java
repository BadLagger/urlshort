package sf.badlagger.urlshort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class FileDump {
	
	private String defaultName = null;
	private File filePath = null;
	protected String dataString = null;
	
	protected FileDump(String defaultName){
		this.defaultName = defaultName;
	}
	
	protected abstract boolean checkData();
	protected abstract boolean createDefaultData();
	
	protected boolean save() {
		try {
			FileOutputStream outStream = new FileOutputStream(filePath);
			outStream.write(dataString.getBytes());
			outStream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkFile() {
		try {
			FileInputStream inStream = new FileInputStream(filePath);
			dataString = new String(inStream.readAllBytes());
			inStream.close();
			return checkData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean isExist(String filePath) {
		this.filePath = new File(filePath);
		return (this.filePath.exists() && !this.filePath.isDirectory());
	}
	
	public boolean setFilePath(String filePath) {
		if (isExist(filePath)) {
			return checkFile();
		}
		
		return false;
	}
	
	public boolean setDefault() {
		
		if (isExist(defaultName)) {
			return checkFile();
		} else {
			try {
				return (filePath.createNewFile() && createDefaultData());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
}
