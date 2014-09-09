CSSParser
=========
Dependencies - commons-lang3-3.1.jar

Implemetations : 
 *   Supports CSS 1, 2 and 3 style properties
 *   Supports CSS3 keyframes
 *   Supports fonts
 *   Duplicate CSS auto removal
 *   Duplicate selectors with different style properties will be merged, as per the browser functionality.
 *   Checks for validity of each style property
 *   Each css selector data is treated as a separate node, can be added, copied and modified dynamically.
 *   Optimizing the css 
 *   User defined node creation
 
Yet to be implemented :
 *  KeyframeNode implementation is partial
 *  CSSDocument - CParser should be made as singleton
 *  parse() method should return CSSDocument object
 *  PrettyPrint - option


Accessible Nodes:
CSSNodeArrayList<CSSNode>cssNodes;
ArrayList<FontNode>fontNodes;
ArrayList<KeyframeNode> keyFrames;

Methods:
String toCSS()
void addCSSNode(CSSNode node)
addFontNode(FontNode node)

Initializing the parser:
CParser parser=new CParser(cssString);
				parser.parse();
