package antlr;

import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Node;
import java.util.List;
import java.util.ArrayList;






public class XPath {
    public static void main(String[] args) throws IOException {
    	//read test file XPathTest.txt
    	List<String> lines = Files.readAllLines(Paths.get("./tests/XPathTest.txt"), StandardCharsets.UTF_8);
    	int idx = 0;
    	//test query line by line
    	for(String line : lines){
    		//XPath eval
            System.out.println(line);
        	CharStream input_l = CharStreams.fromString(line);
            //System.out.println(input_l);
        	XPathLexer lexer = new XPathLexer(input_l);
        	CommonTokenStream tokens = new CommonTokenStream(lexer);
            //System.out.println(tokens);
        	XPathParser parser = new XPathParser(tokens);
            ParseTree tree = parser.ap();
            System.out.println(tree);
            MyXPathVisitor visitor = new MyXPathVisitor();
            ArrayList<Node> output_l  = (ArrayList<Node>) visitor.visit(tree);
            System.out.println("Test Case No." + idx + ": " + output_l.size() + " elements found.");



            //output the result
            idx++;
            System.out.println("Test " + idx + ": " + line);
            for (Node n : output_l) {
            	System.out.println(n.getNodeName());
                System.out.println(n.getTextContent());
            }
            System.out.println("SUMMARY:\n output size: "+ output_l.size()+"\n");
    	} 	
    }
}