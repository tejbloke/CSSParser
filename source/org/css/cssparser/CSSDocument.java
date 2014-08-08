package org.css.cssparser;
import java.util.ArrayList;

public class CSSDocument {
	private String cssStr;
	private ArrayList<KeyframeNode> keyFrames;
	public CSSNodeArrayList<CSSNode>cssNodes;
	public boolean isWellformed;
	public ArrayList<String>errorList;
	public ArrayList<FontNode>fontNodes;
	public final static int PRETTYPRINT=0;
	public final static int COMPACT=1;
	public final static int COMPRESSED=2;	
}
