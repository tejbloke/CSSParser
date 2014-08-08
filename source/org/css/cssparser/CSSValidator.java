package org.css.cssparser;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class CSSValidator {
	private static Properties cssProp=new Properties();
	private InputStream getInputStream(){
		InputStream r=getClass().getResourceAsStream("/css-attr.properties");
		return r;
	}
	
	static{
	try {
		cssProp.load(new CSSValidator().getInputStream());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	}
	
	public static boolean validate(CSSNode node){
		
		Iterator<String>attrItr=node.getAttributeMap().keySet().iterator();
		while(attrItr.hasNext()){
			String attr=attrItr.next();
			if(cssProp.containsKey(attr)){
				//System.out.println("ContainsAttr:"+attr);
			}
			else if(attr.contains("-")){
				attr=removeBrowserPrefix(attr);
				if(cssProp.containsKey(attr)){
					//System.out.println("ContainsAttr:"+attr);
				}else{
					//System.out.println("ERROR: Invalid attribute= ["+attr+"]");
				}
			}
			else{
				//System.out.println("ERROR: Invalid attribute= ["+attr+"]");
			}
		}
		return false;
	}
	
	private static String removeBrowserPrefix(String attr){
		
		attr=attr.replace(Utils.WEBKIT,"");
		attr=attr.replace(Utils.MOZ,"");
		attr=attr.replace(Utils.O,"");
		attr=attr.replace(Utils.MS,"");
		
		return attr;
	}
	
}
