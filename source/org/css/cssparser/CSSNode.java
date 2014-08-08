package org.css.cssparser;


import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Iterator;

public class CSSNode extends Node{
	
	private String node;
	public String selector;
	private String content;
	private LinkedHashMap<String,String> attributes=new LinkedHashMap<String,String>();
	
	public CSSNode(String node){
		this.node=node;
		selector=node.substring(0, node.indexOf("{")).trim();
		content=node.substring(node.indexOf("{")+1,node.indexOf("}"));
		populateAttributes();
	}
	
	//Copy constructor
	/**
	 * date modified 29/11/2013
	 * Fixed copy constructor issue. 
	 * */
	public CSSNode(CSSNode thatCssNode){
		node=thatCssNode.node;
		selector=thatCssNode.selector;
		content=thatCssNode.content;
		attributes=thatCssNode.attributes;
	}
	
	@Override
	void populateAttributes(){
		String attrList[]=content.split(";");
		for(int i=0;i<attrList.length;i++){
			if(attrList[i].contains(":")){
			String attr[]=attrList[i].split(":");
				if(attr.length==2){
						attributes.put(attr[0].trim(),attr[1].trim());
				}
			}
		}
	}

	/**
	 * @added 
	 * */
	public static void applyPrefix(LinkedHashMap<String,String> attributes){
		
		Iterator<String>keyItr=attributes.keySet().iterator();
		
		LinkedHashMap<String,String>temp=new LinkedHashMap<String,String>();
		while(keyItr.hasNext()){
			String attr=keyItr.next();
			if(Utils.CSS3_PREFIX_PROPERTIES.contains(attr)){
				Iterator<String>attrItr=Utils.CSS3_PREFIX_PROPERTIES.iterator();
				while(attrItr.hasNext()){
					String Eattr=attrItr.next();
					if(Eattr.equals(attr)){
						String val=attributes.get(attr);
						temp.put("-webkit-"+attr, val);
						temp.put("-moz-"+attr, val);
						temp.put("-o-"+attr, val);
						temp.put("-ms-"+attr, val);
					}
				}
			}
			
		}
		
		Iterator<String>tempItr=temp.keySet().iterator();
		while(tempItr.hasNext()){
			String key=tempItr.next();
			String val=temp.get(key);
			attributes.put(key,val);
		}
		
	}
	
	@Override
	public String toString(){
		applyPrefix(attributes);
		Iterator<String>itr=attributes.keySet().iterator();
		String mainCss="";
		while(itr.hasNext()){
			String attr=itr.next();
			String val=attributes.get(attr);
			String line=attr+":"+val+";";
			mainCss=mainCss+TAB+line+BR;
		}
		mainCss=selector+"{"+BR+mainCss+"}";
		return mainCss;
	}
	
	public String getName(){
		return selector;
	}
	
	public LinkedHashMap<String,String> getAttributeMap(){
		return attributes;
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	/*::IMPORTANT:: List implementation classes use Wrapperclass equals() in its contains() method*/
	/*last modification: equals functionality issue fixed*/
	@Override
	public boolean equals(Object obj){
		CSSNode node=(CSSNode)obj;
		if(selector.equals(node.getName()) && (attributes.size()==node.attributes.size())){
		LinkedHashMap<String,String>extAttrMap=node.getAttributeMap();
		Iterator<String>itr1=extAttrMap.keySet().iterator();
		
		while(itr1.hasNext()){
			String attr=itr1.next();
			if(attributes.containsKey(attr)){
				String extval=extAttrMap.get(attr);
				String thisval=attributes.get(attr);
				if(!extval.equals(thisval)){
					return false;
				}
			}
			else{
				return false;
			}
		}
		return true;
		}
		return false;
	}
	
	/**
	 * @return returns true if attribute exists else returns false
	 * date added 28/11/2013
	 * */
	public boolean hasAttr(String attr){
		return attributes.keySet().contains(attr);
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
	
	/**
	 * @return void
	 * removes the attribute
	 * date added 28/11/2013
	 * */
	public void removeAttr(String attrKey){
		    attributes.remove(attrKey);
	}

	public void mergeCSSNodes(CSSNode extNode){
		//attributes
		LinkedHashMap<String,String>extAttributeMap=extNode.getAttributeMap();
		Iterator<String>extAttrItr=extAttributeMap.keySet().iterator();
			while(extAttrItr.hasNext()){
				String attr=extAttrItr.next();
				String value=extAttributeMap.get(attr);
				attributes.put(attr, value);
			}
	}
	
	public HashSet<String> getSelectorList(){
		String select=getName();
		String list[]=select.split(",");
		HashSet<String>selectList=new HashSet<String>();
			for(int i=0;i<list.length;i++){
				selectList.add(processSelector(list[i]));
			}
		return selectList;
	}
	
	private static String processSelector(String s){
		if(s.contains("[") && s.contains("=")){
			String attr=s.substring(s.indexOf("["),s.indexOf("=")).trim()+"]";
			if(s.indexOf("[")==0){
				s=attr;
			}
			else{
				s=s.substring(0,s.indexOf("[")).trim()+attr;
			}
		}
		else if(s.contains("[") && !s.contains("=")){
			String attr=s.substring(s.indexOf("["),s.length()).trim();
			if(s.indexOf("[")==0){
				s=attr;
			}
			else{
				s=s.substring(0,s.indexOf("[")).trim()+attr;
			}
		}
		if(s.contains(":")){
			s=s.substring(0,s.indexOf(":"));
		}
		return s;
	}

	
	
}
