package antlr;

import java.util.*;
import java.io.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;

/*
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
*/

public class MyXPathVisitor extends XPathBaseVisitor<ArrayList<Node>> {
    ArrayList<Node> currentNodes = new ArrayList<>();

    @Override
    public ArrayList<Node> visitFileName(XPathParser.FileNameContext ctx) {
        //Visit the children of a node, and return a user-defined result of the operation.
        //https://www.antlr.org/api/Java/org/antlr/v4/runtime/tree/ParseTreeVisitor.html#visit(org.antlr.v4.runtime.tree.ParseTree)
        return visitChildren(ctx);
    }

    @Override
    public ArrayList<Node> visitApRoot(XPathParser.ApRootContext ctx) {
        ArrayList<Node> ans;
        visit(ctx.fileName());//== call visitFileName()?
        ans = visit(ctx.rp());//== call visitRpCurrent()?
        currentNodes = ans;
        return ans;
    }

    @Override
    public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
        ArrayList<Node> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();

        visit(ctx.fileName());
        result.addAll(currentNodes);
        queue.addAll(currentNodes);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            NodeList cur_node_child = cur.getChildNodes();
            for (int i = 0; i < cur_node_child.getLength(); i++) {
                result.add(cur_node_child.item(i));
                //inserts the specified element into this queue if it is possible to do so immediately
                //without violating capacity restrictions
                queue.offer(cur_node_child.item(i));
            }
        }
        //unique(result);
        currentNodes = result;
        ArrayList<Node> new_result = visit(ctx.rp());
        return new_result;
    }

    @Override
    public ArrayList<Node> visitRpParent(XPathParser.RpParentContext ctx) {
        ArrayList<Node> ans = new ArrayList<>();

        for (Node node : currentNodes) {
            Node parent = null;
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                parent = ((Attr) node).getOwnerElement();
            } else {
                parent = node.getParentNode();
            }
            if (!ans.contains(parent)) {
                ans.add(parent);
            }
        }
        currentNodes = ans;
        return ans;
    }

    @Override
    public ArrayList<Node> visitRpAttName(XPathParser.RpAttNameContext ctx) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : currentNodes) {
            Element element = (Element) node;

            if (element.hasAttributes()) {
                NamedNodeMap map = element.getAttributes();
                for (int i = 0; i < map.getLength(); i++) {
                    result.add(map.item(i));
                }
            }
        }
        currentNodes = new ArrayList<>(result);
        return result;
    }

    @Override
    public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {  // ----- (//)
        //our RpCurrentContext class is different from the ref. Are we suppose to change it?
        visit(ctx.rp(0));
        ArrayList<Node> tempResult = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        tempResult.addAll(currentNodes);
        queue.addAll(currentNodes);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            for (int i = 0; i < cur.getChildNodes().getLength(); i++) {
                tempResult.add(cur.getChildNodes().item(i));
                queue.offer(cur.getChildNodes().item(i));
            }
        }
        currentNodes = tempResult;
        tempResult = visit(ctx.rp(1));

        ArrayList<Node> result = new ArrayList<>();
        for (Node node : tempResult) {
            if (!result.contains(node)) {
                result.add(node);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : currentNodes) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node cur = node.getChildNodes().item(i);
                if (cur.getNodeType() == Node.TEXT_NODE && !cur.getNodeValue().isEmpty() && !cur.getNodeValue().equals("\n")) {
                    result.add(cur);
                }
            }
        }

        return result;

    }




}