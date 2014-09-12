package org.css.cssparser;
import java.io.Serializable;
public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	public final static String BR=System.getProperty("line.separator");
	public final static String FS=System.getProperty("file.separator");
	public final static String TAB="	";
	
	
	
	//Methods to be overridden
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	void populateAttributes(){
		
	}

	
	
}
