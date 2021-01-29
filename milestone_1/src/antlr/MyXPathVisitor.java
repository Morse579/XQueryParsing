package antlr;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;


public class MyXPathVisitor extends XPathBaseVisitor<ArrayList<Node>> {
    ArrayList<Node> currentNodes = new ArrayList<>();

    //
    @Override
    public ArrayList<Node> visitFileName(XPathParser.FileNameContext ctx) {
        //Visit the children of a node, and return a user-defined result of the operation.
        //https://www.antlr.org/api/Java/org/antlr/v4/runtime/tree/ParseTreeVisitor.html#visit(org.antlr.v4.runtime.tree.ParseTree)
        return visitChildren(ctx);
    }
    
    //ap doc(fileName)/rp
    @Override
    public ArrayList<Node> visitApRoot(XPathParser.ApRootContext ctx) {
        //use visitFileName() to jump from root to current level
        visit(ctx.fileName());

        //then use visitRpCurrent() to jump to target relative path
        ArrayList<Node> ans = visit(ctx.rp());

        currentNodes = ans;
        return ans;
    }

    //FIXME
  //ap doc(fileName)//rp
    @Override
    public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
        ArrayList<Node> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        result.addAll(currentNodes);
        queue.addAll(currentNodes);

        //jump from root to current level
        visit(ctx.fileName());

        while (!queue.isEmpty()) {
            Node headNode = queue.poll();
            NodeList headNode_children = headNode.getChildNodes();
            for (int i = 0; i < headNode_children.getLength(); i++) {
                result.add(headNode_children.item(i));
                //inserts the specified element into this queue if it is possible to do so immediately
                //without violating capacity restrictions
                queue.add(headNode_children.item(i));
            }
        }

        currentNodes = result;
        ArrayList<Node> new_result = visit(ctx.rp());
        return new_result;
    }

    //rp: ..
    //a singleton list containing the parent of element node n
    @Override
    public ArrayList<Node> visitRpParent(XPathParser.RpParentContext ctx) {
        ArrayList<Node> res = new ArrayList<>();
        ArrayList<Node> tmp = new ArrayList<>();

        for (Node n : currentNodes) {
            Node parent = null;
            //All nodes, except Attr, Document, DocumentFragment, Entity, and Notation may have a parent
            if (n.getNodeType() == Node.ATTRIBUTE_NODE || n.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE ||
                    n.getNodeType() == Node.DOCUMENT_NODE || n.getNodeType() == Node.ENTITY_NODE ||
                    n.getNodeType() == Node.NOTATION_NODE) {
                //parent = the Element node this attribute is attached to == self or other Element nodes with this attr
                parent = ((Attr) n).getOwnerElement();
            } else {
                parent = n.getParentNode();
            }
            tmp.add(parent);
        }

        for (Node n : tmp){
            if (!res.contains(n)){
                res.add(n);
            }
        }

        currentNodes = res;
        return res;
    }

    //rp: @attName
    @Override
    public ArrayList<Node> visitRpAttName(XPathParser.RpAttNameContext ctx) {
        ArrayList<Node> res = new ArrayList<>();
        for (Node n : currentNodes) {
            Element element = (Element) n;
            if (element.hasAttributes()) {
                NamedNodeMap allAttrs = element.getAttributes();
                for (int i = 0; i < allAttrs.getLength(); i++) {
                    res.add(allAttrs.item(i));
                }
            }
        }
        currentNodes = res;
        return res;
    }

    //FIXME!
    //rp: . 
    //the singleton list with unique entry e
    @Override
    public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {  // ----- (//)
        ArrayList<Node> res0 = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        res0.addAll(currentNodes);
        queue.addAll(currentNodes);

        while (!queue.isEmpty()) {
            Node working = queue.poll();
            NodeList working_children = working.getChildNodes();
            for (int i = 0; i < working_children.getLength(); i++) {
                res0.add(working_children.item(i));
                queue.add(working_children.item(i));
            }
        }
        currentNodes = res0;

        ArrayList<Node> result = new ArrayList<>();
        for (Node n : res0) {
            if (!result.contains(n)) {
                result.add(n);
            }
        }
        return result;
    }

    //rp: text()
    //the text node associated to element node n
    @Override
    public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
        //to extract all text node under current nodes
        ArrayList<Node> res = new ArrayList<>();
        for (Node n : currentNodes) {
            NodeList n_childrens = n.getChildNodes();
            for (int i = 0; i < n_childrens.getLength(); i++) {
                Node working = n_childrens.item(i);
                //to ensure the text nodes added in have valid content
                if (working.getNodeType() == Node.TEXT_NODE && !working.getNodeValue().isEmpty() && !working.getNodeValue().equals("\n")) {
                    res.add(working);
                }
            }
        }

        return res;

    }

    //rp: tagName
    //all children of current node with tagName c
    @Override
    public ArrayList<Node> visitRpTagName(XPathParser.RpTagNameContext ctx) {
        ArrayList<Node> res = new ArrayList<>();
        for(Node n : currentNodes){
            NodeList n_childrens = n.getChildNodes();
            //loop through all children of currentNodes to check if they have the tag
            for(int i = 0; i < n_childrens.getLength(); i++) {
                Node child = n_childrens.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element child_to_ele = (Element) child;
                    //child_to_ele.getTagName() returns a string
                    //!!FIXME!need to extract ctx's tagname as a string
                    if (child_to_ele.getTagName().equals(ctx.tagName())) {
                        res.add(child);
                    }
                }
            }
        }
        currentNodes = res;
        return res;
    }



}