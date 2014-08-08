package org.css.cssparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

//import org.apache.log4j.*;

/**@
 * @author teja
 * @since 0.1
 * @version 1.0
 * @category CSSParser
 * Implemetations : 
 *   Supports CSS1, CSS2 and CSS3 style properties
     Supports CSS3 keyframes
     Supports fonts
     Duplicate CSS auto removal
     Duplicate selectors with different style properties will be merged, as per the browser functionality.
     Checks for validity of each style property
     Each css selector data is treated as a separate node, can be added, copied and modified dynamically.
     Optimizing the css 
 * 
 * Yet to be Implemented :
 *  CSSDocument - CParser should be made as singleton
 *  			  parse() method should return CSSDocument object.
 *  PrettyPrint - option
 *  *  */

public class CParser {
private String cssStr;
private ArrayList<KeyframeNode> keyFrames;
public CSSNodeArrayList<CSSNode>cssNodes;
public boolean isWellformed;
public ArrayList<String>errorList;
public ArrayList<FontNode>fontNodes;
public final static int PRETTYPRINT=0;
public final static int COMPACT=1;
public final static int COMPRESSED=2;

	
//yet to be implemented.
	//Logger log=Logger.getLogger(CParser.class);

	/**
	 * @param java.lang.String - should be a valid css String
	 * @return CParser object
	 * */
	public CParser(String css){
		cssStr=css;
		cssStr=cssStr.trim();
		cssNodes=new CSSNodeArrayList<CSSNode>();
		keyFrames=new ArrayList<KeyframeNode>();
		fontNodes=new ArrayList<FontNode>();
		errorList=new ArrayList<String>();
	}
	
	/**
	 * @param java.io.File - should be a valid css file
	 * @throws java.io.FileNotFoundException
	 * @return CParser object
	 * */
	public CParser(File cssFile){
		this(Utils.getFileAsString(cssFile,"UTF-8"));
		//BasicConfigurator.configure();
	}
	
	public void parse()throws Exception{
		try{
			clearComments();
		if(isWellformed()){
			isWellformed=true;
			separateKeyFrames();
			separateFontFaces();
			splitCss();
			/* testing area */
		}
		else{
			isWellformed=false;
			errorList.add("Not WellFormed");
		}
		}catch(Exception e){e.printStackTrace();}
	}

	private boolean isWellformed(){
		if(StringUtils.countMatches(cssStr, "}")==StringUtils.countMatches(cssStr, "{")){
			if(StringUtils.countMatches(cssStr, "/*") != StringUtils.countMatches(cssStr, "*/")){
				return false;
			}
			return true;
		}
		return false;
	}
	
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
	
	
	private void separateKeyFrames() throws Exception{
		if(cssStr.contains("keyframes")){
			int bIndex=cssStr.indexOf("@keyframes");
			if(bIndex==-1){
				if(cssStr.contains("@-webkit-keyframes")){
					bIndex=cssStr.indexOf("@-webkit-keyframes");
				}
				else if(cssStr.contains("@-moz-keyframes")){
					bIndex=cssStr.indexOf("@-moz-keyframes");
				}
				else if(cssStr.contains("@-ms-keyframes")){
					bIndex=cssStr.indexOf("@-ms-keyframes");
				}
				else if(cssStr.contains("@-o-keyframes")){
					bIndex=cssStr.indexOf("@-o-keyframes");
				}
			}
			
			String subStr=cssStr.substring(bIndex,cssStr.length());
			String []list=subStr.split("");
			Stack<String> s=new Stack<String>();
			int flag=0;
			int index=0;
			for(int i=1;i<list.length;i++){
					if(flag==0){
						if(list[i].equals("{")){
							flag=1;
							s.push(list[i]);
						}
					}
					else if(flag==1){
						if(list[i].equals("{")){
							s.push(list[i]);
						}
						else if(list[i].equals("}")){
							s.pop();
						}
						if(s.isEmpty()){
							index=i;
							break;
						}
					}
			}
			String keyframe=subStr.substring(0,index);
			String subStrMod=subStr.replace(keyframe,"");
			cssStr=cssStr.replace(subStr, subStrMod);
			KeyframeNode kFrame=new KeyframeNode(keyframe);
				if(!keyFrames.contains(kFrame)){
					keyFrames.add(kFrame);
				}
			}
			//if stillContains keyframes recurse the method
			if(cssStr.contains("keyframes")){
				separateKeyFrames();
			}
		}

	private void separateFontFaces(){
		if(cssStr.contains("@font-face")){
			Pattern fn=Pattern.compile("@font-face\\s+\\{[^}]+\\}");
			Matcher fm=fn.matcher(cssStr);
				while(fm.find()){
					String font=fm.group(0);
					FontNode fontNode=new FontNode(font);
					
					/*start*/
					if(fontNodes.contains(fontNode)){
						errorList.add("Ignored duplicate FontNode : "+fontNode.getName());
						//log.debug("Ignored duplicate FontNode : "+fontNode.getName());
						}
					fontNodes.add(fontNode);
					/*end*/

					/*//replace the above /start to end/ overridden code with this....- css font node duplication has been ignored.
					 * if(!fontNodes.contains(fontNode)){
							fontNodes.add(fontNode);					
						}
						else{
							errorList.add("Ignored duplicate FontNode : "+fontNode.getName());
						}
					 * */
				}
			cssStr=cssStr.replaceAll("@font-face\\s+\\{[^}]+\\}", "");	
		}
	}
	
	public void clearComments(){
		if(cssStr.contains("/*")){
			cssStr=cssStr.replaceAll("\\/\\*[^*/]+\\*\\/", "");
		}
	}
	
	public void splitCss(){
		Pattern cn=Pattern.compile("[^{}]+\\{+[^}]+\\}");
		Matcher cm=cn.matcher(cssStr);
		int count=0;
			while(cm.find()){
				CSSNode newNode=new CSSNode(cm.group(0));
				if(!cssNodes.contains(newNode)){
					if(cssNodes.containsSameName(newNode.getName())){
						int ind=cssNodes.indexOfCSSNode(newNode.getName());
						CSSNode tempNode=(CSSNode) cssNodes.get(ind);
						tempNode.mergeCSSNodes(newNode);
						cssNodes.set(ind, tempNode);
						errorList.add("Warning: duplicate CSS with diff attributes: "+tempNode);
					}
					else{
						cssNodes.add(newNode);
					}
				CSSValidator.validate(newNode);
				}
				else{
					errorList.add("Warning : duplicate CSS: "+newNode.getName());
					//log.debug("Warning : duplicate CSS: "+newNode.getName());
				}
			}
	}
	
	
	public void addCSSNode(CSSNode node){
		if(!cssNodes.contains(node)){
			cssNodes.add(node);
		}
	}

	public void addFontNode(CSSNode node){
		if(!cssNodes.contains(node)){
			cssNodes.add(node);
		}
	}
	
	
	}
		
	
