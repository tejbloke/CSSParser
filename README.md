CSSParser
=========
Parser Implementation not inspired by or used the standard w3c sac interfaces and classes.

Java programmers are welcome to contribute or give a design suggestion.

Dependencies - commons-lang3-3.1.jar

###Implemetations : 
 *   Supports CSS 1, 2 and 3 style properties
 *   Supports CSS3 keyframes
 *   Supports fonts
 *   Duplicate CSS auto removal
 *   Duplicate selectors with different style properties will be merged, as per the browser functionality.
 *   Checks for validity of each style property
 *   Each css selector data is treated as a separate node, can be added, copied and modified dynamically.
 *   Optimizing the css 
 *   User defined node creation
 
###Yet to be implemented :
 *  KeyframeNode implementation is partial
 *  CSSDocument - CParser should be made as singleton
 *  parse() method should return CSSDocument object
 *  PrettyPrint - option


###Accessible Nodes:
```Java
CSSNodeArrayList<CSSNode>cssNodes;
ArrayList<FontNode>fontNodes;
ArrayList<KeyframeNode> keyFrames;
```
###Methods:
```Java
String toCSS()
void addCSSNode(CSSNode node)
addFontNode(FontNode node)
```
###Initializing the parser:
```Java
CSSDocument doc=new Parser(css).parse();
```				
				
