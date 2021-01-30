package antlr;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

import org.w3c.dom.*;


public class MyXPathVisitor extends XPathBaseVisitor<ArrayList<Node>> {
    ArrayList<Node> currentNodes = new ArrayList<>();

    //
//    @Override
//    public ArrayList<Node> visitFileName(XPathParser.FileNameContext ctx) {
//        //Visit the children of a node, and return a user-defined result of the operation.
//        //https://www.antlr.org/api/Java/org/antlr/v4/runtime/tree/ParseTreeVisitor.html#visit(org.antlr.v4.runtime.tree.ParseTree)
//        return visitChildren(ctx);
//    }
    
    //ap doc(fileName)/rp
    @Override
    public ArrayList<Node> visitApRoot(XPathParser.ApRootContext ctx) {
        //use visitFileName() to jump from root to current level
        visit(ctx.fileName());

        //then use visitRpCurrent() to jump to target relative path
        ArrayList<Node> res = visit(ctx.rp());

        currentNodes = res;
        return res;
    }

    //FIXME
  //ap doc(fileName)//rp
    @Override
    public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
        ArrayList<Node> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
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
                result.add(headNode_children.item(i));
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
        for (Node n: currentNodes){
            Node tempParent =  n.getParentNode();
            if(tempParent != null && !res.contains(tempParent)){
                res.add(tempParent);
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
            //NamedNodeMap getAttributes():
            //A NamedNodeMap containing the attributes of this node (if it is an Element) or null otherwise.
            Element n_to_ele = (Element) n;
            if (n_to_ele.hasAttributes()) {
                NamedNodeMap allAttrs = n_to_ele.getAttributes();
                for (int i = 0; i < allAttrs.getLength(); i++) {
                    res.add(allAttrs.item(i));
                }
            }
        }
        currentNodes = res;
        return res;
    }

    //TEST ME
    //rp: . 
    //the singleton list with unique entry e
    @Override
    public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {  // ----- (//)
        /*
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
        */
        return currentNodes;
    }

    //rp: text()
    //the text node associated to element node n
    @Override
    public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
        //to extract all text node under current nodes
        ArrayList<Node> res = new ArrayList<>();
        for (Node n : currentNodes) {
            NodeList n_childrens = n.getChildNodes();
            for (Node n_child : n_childrens) {
                //to ensure the text nodes added in have valid content
                if (n_child.getNodeType() == Node.TEXT_NODE && !n_child.getTextContent() == null) {
                    res.add(n_child);
                }
            }
        }
        currentNodes = res;
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
                    //ISSUE MIGHT OCCUR HERE
                    //extract ctx's tagname as a string
                    if (child_to_ele.getTagName().equals(ctx.getText())) {
                        res.add(child);
                    }
                }
            }
        }
        currentNodes = res;
        return res;
    }

    //all children of the currentNodes
    @Override
    public ArrayList<Node> visitRpChildren(XPathParser.RpChildrenContext ctx) {
        //all children of the currentnodes
        ArrayList<Node> res = new ArrayList<>();
        for(Node n: currentNodes){
            NodeList n_children = n.getChildNodes();
            for(Node n_child : n_children){
                if(n_child.getNodeType() == Node.ELEMENT_NODE || n_child.getNodeType() == Node.DOCUMENT_NODE){
                    res.add(n_child);
                }
            }
        }
        currentNodes = res;
        return res;
    }

    @Override
    public ArrayList<Node> visitRpConcat (XPathParser.RpConcatContext ctx) {
        ArrayList<Node> temp = new ArrayList<Node>(currentNodes);
        //after visit currentNode will change?
        ArrayList<Node> rp0 = visit(ctx.rp(0));
        currentNodes = temp;//go back to original node
        ArrayList<Node>  rp1 = visit(ctx.rp(1));
        rp0.addAll(rp1);
        currentNodes = rp0;
        return rp0;
    }

    @Override
    public ArrayList<Node> visitRpRoot (XPathParser.RpRootContext ctx) {
        visit(ctx.rp(0));
        ArrayList<Node> res = visit(ctx.rp(1));
        currentNodes = res;
        return res;
    }
    @Override
    public ArrayList<Node> visitRpFilter (XPathParser.RpFilterContext ctx) {
        visit(ctx.rp());
        ArrayList<Node> temp = new ArrayList<>(currentNodes);
        ArrayList<Node> res = new ArrayList<>();

        for (Node n : temp) {
            currentNodes = new ArrayList<>();
            currentNodes.add(n);
            ArrayList<Node> filter_res = visit(ctx.filter());
            if (!filter_res.isEmpty()) {
                res.add(n);
            }
        }

        currentNodes = res;
        return res;
    }

    @Override
    public ArrayList<Node> visitRpPathNodes (XPathParser.RpPathNodesContext ctx) {
        ArrayList<Node> res = visit(ctx.rp());
        return res;
    }

}