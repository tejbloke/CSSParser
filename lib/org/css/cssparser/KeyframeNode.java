package org.css.cssparser;

public class KeyframeNode extends Node{

	/**
	 * @param String node
	 */
	private String node;
	private String keyframeName;
	/**
	 * @param node
	 * */
	public KeyframeNode(String node){
		node=removeBrowserPrefixes(node);
		this.node=node.substring(node.indexOf("{")+1,node.lastIndexOf("}"));
		keyframeName=node.substring(0,node.indexOf("{"));
	}
	
	@Override
	public String getName(){
		return keyframeName;
	}
	
	@Override
	public boolean equals(Object obj){
		return false;
	}

	@Override
	void populateAttributes(){
		
	}
	
	@Override 
	public String toString(){
		StringBuffer retStr=new StringBuffer();
		System.out.println(keyframeName+"{"+node+"}");
		return AT_KEYFRAMES+node;
	}
	
	//Static Utility Methods and Constants
	private static String PREFIX_WEBKIT="-webkit-";
	private static String PREFIX_MOZ="-moz-";
	private static String PREFIX_MS="-ms-";
	private static String PREFIX_O="-o-";
	private static String AT_KEYFRAMES="@keyframes ";
	
	private static String removeBrowserPrefixes(String str){
		str=str.replace(PREFIX_WEBKIT, "");
		str=str.replace(PREFIX_MOZ, "");
		str=str.replace(PREFIX_MS, "");
		str=str.replace(PREFIX_O, "");
		str=str.replace(AT_KEYFRAMES,"");
		return str;
	}

}
