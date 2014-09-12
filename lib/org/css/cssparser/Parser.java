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
 *  CSSDocument - CParser should be made as singleton - pending
 *  			  parse() method should return CSSDocument object. - done
 *  PrettyPrint - option - pending
 *  PrintSettings - with styling information - pending
 *  *  */

public class Parser {
public final static int PRETTYPRINT=0;
public final static int COMPACT=1;
public final static int COMPRESSED=2;
public final CSSDocument cssDocument=new CSSDocument();
//yet to be implemented.
	//Logger log=Logger.getLogger(CParser.class);

	/**
	 * @param java.lang.String - should be a valid css String
	 * @return CParser object
	 * */
	public Parser(String css){
		
		cssDocument.cssStr=css.trim();
		cssDocument.cssNodes=new CSSNodeArrayList<CSSNode>();
		cssDocument.keyFrames=new ArrayList<KeyframeNode>();
		cssDocument.fontNodes=new ArrayList<FontNode>();
		cssDocument.errorList=new ArrayList<String>();
		
	}
	
	
	
	
	
	/**
	 * @param java.io.File - should be a valid css file
	 * @throws java.io.FileNotFoundException
	 * @return CParser object
	 * */
	public Parser(File cssFile){
		this(Utils.getFileAsString(cssFile,"UTF-8"));
		//BasicConfigurator.configure();
	}
	
	public CSSDocument parse()throws Exception{
		try{
			clearComments();
		if(isWellformed()){
			cssDocument.isWellformed=true;
			separateKeyFrames();
			separateFontFaces();
			splitCss();
			return cssDocument;
			/* testing area */
		}else{
			cssDocument.isWellformed=false;
			cssDocument.errorList.add("MalFormed");
		}
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}

	private boolean isWellformed(){
		if(StringUtils.countMatches(cssDocument.cssStr, "}")==StringUtils.countMatches(cssDocument.cssStr, "{")){
			if(StringUtils.countMatches(cssDocument.cssStr, "/*") != StringUtils.countMatches(cssDocument.cssStr, "*/")){
				return false;
			}
			return true;
		}
		return false;
	}
	
	
	
	
	private void separateKeyFrames() throws Exception{
		if(cssDocument.cssStr.contains("keyframes")){
			int bIndex=cssDocument.cssStr.indexOf("@keyframes");
			if(bIndex==-1){
				if(cssDocument.cssStr.contains("@-webkit-keyframes")){
					bIndex=cssDocument.cssStr.indexOf("@-webkit-keyframes");
				}
				else if(cssDocument.cssStr.contains("@-moz-keyframes")){
					bIndex=cssDocument.cssStr.indexOf("@-moz-keyframes");
				}
				else if(cssDocument.cssStr.contains("@-ms-keyframes")){
					bIndex=cssDocument.cssStr.indexOf("@-ms-keyframes");
				}
				else if(cssDocument.cssStr.contains("@-o-keyframes")){
					bIndex=cssDocument.cssStr.indexOf("@-o-keyframes");
				}
			}
			
			String subStr=cssDocument.cssStr.substring(bIndex,cssDocument.cssStr.length());
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
			cssDocument.cssStr=cssDocument.cssStr.replace(subStr, subStrMod);
			KeyframeNode kFrame=new KeyframeNode(keyframe);
				if(!cssDocument.keyFrames.contains(kFrame)){
					cssDocument.keyFrames.add(kFrame);
				}
			}
			//if stillContains keyframes recurse the method
			if(cssDocument.cssStr.contains("keyframes")){
				separateKeyFrames();
			}
		}

	private void separateFontFaces(){
		if(cssDocument.cssStr.contains("@font-face")){
			Pattern fn=Pattern.compile("@font-face\\s+\\{[^}]+\\}");
			Matcher fm=fn.matcher(cssDocument.cssStr);
				while(fm.find()){
					String font=fm.group(0);
					FontNode fontNode=new FontNode(font);
					
					/*start*/
					if(cssDocument.fontNodes.contains(fontNode)){
						cssDocument.errorList.add("Ignored duplicate FontNode : "+fontNode.getName());
						//log.debug("Ignored duplicate FontNode : "+fontNode.getName());
						}
					cssDocument.fontNodes.add(fontNode);
					/*end*/
				}
				cssDocument.cssStr=cssDocument.cssStr.replaceAll("@font-face\\s+\\{[^}]+\\}", "");	
		}
	}
	
	public void clearComments(){
		if(cssDocument.cssStr.contains("/*")){
			cssDocument.cssStr=cssDocument.cssStr.replaceAll("\\/\\*[^*/]+\\*\\/", "");
		}
	}
	
	public void splitCss(){
		Pattern cn=Pattern.compile("[^{}]+\\{+[^}]+\\}");
		Matcher cm=cn.matcher(cssDocument.cssStr);
			while(cm.find()){
				CSSNode newNode=new CSSNode(cm.group(0));
				if(!cssDocument.cssNodes.contains(newNode)){
					if(cssDocument.cssNodes.containsSameName(newNode.getName())){
						int ind=cssDocument.cssNodes.indexOfCSSNode(newNode.getName());
						CSSNode tempNode=(CSSNode) cssDocument.cssNodes.get(ind);
						tempNode.mergeCSSNodes(newNode);
						cssDocument.cssNodes.set(ind, tempNode);
						cssDocument.errorList.add("Warning: duplicate CSS with diff attributes: "+tempNode);
					}
					else{
						cssDocument.cssNodes.add(newNode);
					}
				CSSValidator.validate(newNode);
				}
				else{
					cssDocument.errorList.add("Warning : duplicate CSS: "+newNode.getName());
					//log.debug("Warning : duplicate CSS: "+newNode.getName());
				}
			}
	}
	
	
	
	
	
	}
		
	
