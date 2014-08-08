package org.css.cssparser;

import java.util.ArrayList;

public class CSSNodeArrayList<T> extends ArrayList {

	public boolean containsSameName(String name){

		return (indexOfCSSNode(name)>=0);
		
	}
	
	public int indexOfCSSNode(String cName){
		
		for(int i=0;i<this.size();i++){
			CSSNode n=(CSSNode)this.get(i);
			if(n.getName().equals(cName)){
				return i;
			}
		}
		return -1;
	}
	
	
}
