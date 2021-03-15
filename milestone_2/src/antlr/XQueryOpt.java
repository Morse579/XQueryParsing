package antlr;

import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w3c.dom.Node;
import java.util.List;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/** TODO!
 * I only change the name from XPath to XQuery
 */

/**
 * Exiting node-to-xml transformer is used.
 * The method uses reference: convertNodeToString(Node node)()
 * Reference: https://frontbackend.com/java/how-to-convert-xml-node-object-to-string-in-java
 */



public class XQueryOpt {
    public static void main(String[] args) throws IOException {
        //System.out.println("ACCEPT INPUT FILE:");
        //read test file from arg input
        String filename = "";
        if (0 < args.length) {
            filename = args[0];
            //System.out.print("FILENAME INPUT: " + filename);
            //File file = new File(filename);
        }
        String line = Files.readString(Paths.get(filename), StandardCharsets.UTF_8);
        /* dev: input == XPathTest.txt
    	List<String> lines = Files.readAllLines(Paths.get("XPathTest.txt"), StandardCharsets.UTF_8);
         */

        //open a file to write output in xml format
        File outputFile = new File("Output.txt"); // output file for result
        File rewriteQ = new File("QueryOutput.txt"); //output file for rewrite query
        if (!outputFile.exists()) outputFile.createNewFile(); // create output file

        @SuppressWarnings("resource")
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        FileOutputStream queryOutputStream = new FileOutputStream(rewriteQ);
        StringBuffer sb = new StringBuffer();

        //int idx = 0;
        //test query line by line
//        for(String line : lines){

            //XPath eval
            //System.out.println(line);
        CharStream input_l = CharStreams.fromString(line);
        XQueryLexer lexer = new XQueryLexer(input_l);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        XQueryParser parser = new XQueryParser(tokens);
        ParseTree tree = parser.xq();
        MyXQueryVisitor visitor_opt = new MyXQueryVisitor();
        ArrayList<Node> output_l  = (ArrayList<Node>) visitor_opt.visit(tree);
        //System.out.println("outputsize: " + output_l.size());
            //idx++;

            /*output the result to terminal while testing
            System.out.println("Test " + idx + ": " + line);
            for (Node n : output_l) {
                System.out.println(n.getNodeName());
                System.out.println(n.getTextContent());
            }
            */
            //System.out.println("SUMMARY:\n output list size: "+ output_l.size()+"\n");
            //outout to a file in xml format
            /*write input info:
            sb.append("Test case No.").append(idx).append(":\n");
            sb.append(line);
            fileOutputStream.write(sb.toString().getBytes());
            */

        for (Node n : output_l) {
            String curOutput = convertNodeToString(n);
            System.out.print(curOutput);
            fileOutputStream.write(curOutput.getBytes());
        }
            //fileOutputStream.write(("-------------------------END OF TEST------------------------\n").getBytes());
//        }
        
        //write rewrite query into output file
        Optimized q_opt = new Optimized();
        String q_result = "";//q_opt.inputRewrite(tree.XqFLWRContext);
        queryOutputStream.write(q_result.getBytes());
        
    }



    private static String convertNodeToString(Node node) {
        try {
            StringWriter writer = new StringWriter();
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException te) {
            System.out.println("Fail to transform node to string");
            te.printStackTrace();
        }

        return "";
    }
    
    public static ArrayList<Node> evalRewrite(String rewrittenInput){
        try {
            CharStream input = CharStreams.fromString(rewrittenInput);
            XQueryLexer lexer = new XQueryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            XQueryParser parser = new XQueryParser(tokens);
            parser.removeErrorListeners();
            ParseTree tree = parser.xq();

            MyXQueryVisitor rewriteVisitor = new MyXQueryVisitor();
            rewriteVisitor.needRewrite = false;
            ArrayList<Node> results = rewriteVisitor.visit(tree);
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in xquery.evalRewrite: " + e.getMessage());
        }
        return null;
    }
}