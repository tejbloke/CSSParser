package org.css.cssparser;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
/*
 * Static vars and utils methods available
 * */
public class Utils {
	
	//Browser Prefixes
	public final static String WEBKIT="-webkit-";
	public final static String MOZ="-moz-";
	public final static String O="-o-";
	public final static String MS="-ms-";
	public final static ArrayList<String>CSS3_PREFIX_PROPERTIES=new ArrayList<String>();
	//Line and file breaks
	
	static{
		String props="animation-timing-function,transition-timing-function,animation-iteration-count,animation-play-state,column-rule-color,column-rule-style,column-rule-width,marquee-play-count,box-ordinal-group,animation-name,animation-duration,animation-delay,animation-direction,backface-visibility,border-image,box-align,box-direction,box-flex,box-orient,box-pack,box-shadow,box-sizing,column-gap,column-rule,column-span,column-width,column-count,marquee-direction,marquee-speed,marquee-style,perspective-origin,transform-origin,transform-style,transition-property,transition-duration,transition-delay,@keyframes,animation,appearance,columns,perspective,transform,transition";
		String list[]=props.split(",");
		for(int i=0;i<list.length;i++){
			CSS3_PREFIX_PROPERTIES.add(list[i].trim());
		}
	}
	
	
	private final static String BR=System.getProperty("line.separator");
	
	static String getFileAsString(File file,String encoding){
		String str="";
		if(file.exists() && file.length()>0){
			try{
				str=new Scanner(new FileInputStream(file),encoding).useDelimiter("\\A").next();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		else if(!file.exists()){
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
	
	
	
}