package org.css.cssparser;
import java.util.ArrayList;
import java.util.Iterator;

public class CSSDocument {
	
	public String cssStr;
	public ArrayList<KeyframeNode> keyFrames;
	public CSSNodeArrayList<CSSNode> cssNodes;
	public boolean isWellformed;
	public ArrayList<String>errorList;
	public ArrayList<FontNode>fontNodes;
	public final static int PRETTYPRINT=0;
	public final static int COMPACT=1;
	public final static int COMPRESSED=2;
	
	
	public String toCSS(){
		StringBuffer css=new StringBuffer();
		
		if(fontNodes.size()>0){
			css.append("/*Fonts*/"+Node.BR);
		}
		
		Iterator<FontNode>itr3=fontNodes.iterator();
		while(itr3.hasNext()){
			css.append(itr3.next()+Node.BR);
		}
		
		if(cssNodes.size()>0){
			css.append("/*CSS*/"+Node.BR);
		}
		Iterator<CSSNode>itr1=cssNodes.iterator();
		while(itr1.hasNext()){
			css.append(itr1.next()+Node.BR);
		}
		
		if(keyFrames.size()>0){
			css.append("/*Keyframes*/"+Node.BR);
		}
		
		Iterator<KeyframeNode>itr2=keyFrames.iterator();
		while(itr2.hasNext()){
			css.append(itr2.next()+Node.BR);
		}
		
		return css.toString();
	}
}
