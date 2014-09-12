package org.css.cssparser;
import java.util.HashMap;
import java.util.Iterator;

public class FontNode extends Node{

	private String node;
	private String name;
	private HashMap<String,String>attributes; 
	
	@Override
	public String getName(){
		name=attributes.get("font-family");
		return name;
	}

	FontNode(String node){
		attributes=new HashMap<String,String>();
		this.node=node.substring(node.indexOf("{")+1,node.indexOf("}"));
		populateAttributes();
		name=attributes.get("font-family");
	}
	
	@Override
	void populateAttributes(){
		String attrList[]=node.split(";");
		for(int i=0;i<attrList.length;i++){
			if(attrList[i].contains(":")){
			String attr[]=attrList[i].split(":");
				if(attr.length==2){
						attributes.put(attr[0].trim(),attr[1].trim());
				}
			}
		}
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	/**
	 * @return returns true if attribute exists else returns false
	 * date added 28/11/2013
	 * */
	public boolean hasAttr(String attr){
		return attributes.entrySet().contains(attr);
	}
	
	/**
	 * @return returns attribute value if exist else returns null
	 * date added 28/11/2013
	 * */
	public String getAttr(String attrKey){
		return attributes.get(attrKey);
	}
	
	/**
	 * @return void
	 * sets the value of a attribute
	 * date added 28/11/2013
	 * */
	public void setAttr(String attrKey,String attrVal){
			attributes.put(attrKey, attrVal);
	}
	
	
	@Override
	public boolean equals(Object obj){
		FontNode extNode=(FontNode)obj;
		String extName=extNode.getName();
		if(extName.equals(name)){
			return true;
		}
		return false;
	}
	
	@Override 
	public String toString(){
		String out="";
		
		Iterator<String> attrItr=attributes.keySet().iterator();
		while(attrItr.hasNext()){
			String attr=attrItr.next();
			attr=attr+":"+attributes.get(attr)+";"+BR;
			out=out+TAB+attr;
		}
		
		out="@font-face {"+BR+out+"}";
		
		return out;
	}
	public HashMap<String,String> getAttributes(){
		return attributes;
	}

}
