package antlr;

import java.nio.file.*;
import java.io.File;
import java.nio.charset.*;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Node;
import java.util.List;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;


public class XPath {
    public static void main(String[] args) throws IOException {
    	//read test file XPathTest.txt
    	List<String> lines = Files.readAllLines(Paths.get("./tests/XPathTest.txt"), StandardCharsets.UTF_8);
    	int idx = 0;
    	//test query line by line
    	for(String line : lines){
    		//XPath eval
        	CharStream input_l = CharStreams.fromString(line);
        	XPathLexer lexer = new XPathLexer(input_l);
        	CommonTokenStream tokens = new CommonTokenStream(lexer);
        	XPathParser parser = new XPathParser(tokens);
            ParseTree tree = parser.ap();
            MyXPathVisitor visitor = new MyXPathVisitor();
            ArrayList<Node> output_l = visitor.visit(tree);
            //output the result
            idx++;
            System.out.println("Test " + idx + ": " + line);
            for (Node n : output_l) {
            	System.out.println(n.getNodeName());
            }
            System.out.println("output size "+ output_l.size()+"\n");
    	} 	
    }
}