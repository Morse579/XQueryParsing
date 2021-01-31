package antlr;

import java.util.*;

import org.w3c.dom.*;

import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MyXPathVisitor extends XPathBaseVisitor<ArrayList<Node>> {
    ArrayList<Node> currentNodes = new ArrayList<>();


    private Node getRoot(String fileName) {


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = db.parse(new File(fileName));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.getDocumentElement();
    }



    //ap doc(fileName)/rp
    //'doc("' fileName '")' '/' rp  #ApRoot
    @Override
    public ArrayList<Node> visitApRoot(XPathParser.ApRootContext ctx) {
        System.out.println("visitApRoot() is called!");
        Node root = getRoot(ctx.fileName().getText());
        return visit(ctx.rp());
    }


    private void getDesOrSelf(ArrayList<Node> res, Queue<Node>  queue) {
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            if (!n.hasChildNodes()) {
                continue;
            }
            NodeList children = n.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                res.add(children.item(i));
                queue.offer(children.item(i));
            }
        }
    }




    //FIXME
    // ap doc(fileName)//rp
    //'doc("' fileName '")' '//' rp 	#ApCurrent
    @Override
    public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
        /*
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
        */
        System.out.println("visitApCurrent() is called!");
        Node root = getRoot(ctx.fileName().getText());
        currentNodes.add(root);
        Queue<Node> queue = new LinkedList<>(currentNodes);
        ArrayList<Node> res = new ArrayList<>(currentNodes);
        getDesOrSelf(res, queue);
        currentNodes = res;
        return visit(ctx.rp());


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
    public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {
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
            for (int i = 0; i < n_childrens.getLength(); i++) {
                Node n_child = n_childrens.item(i);
                //to ensure the text nodes added in have valid content
                if (n_child.getNodeType() == Node.TEXT_NODE && n_child.getTextContent() != null) {
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
            for(int i = 0; i < n_children.getLength(); i++){
                Node n_child = n_children.item(i);
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
            if (filter_res.size() != 0) {
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

    /* From Canvas Discussion Page:
    rp1 eq rp2 is a condition (a filter).

    It will return TRUE if you find one such pair, FALSE otherwise.

    No node sets returned by the filter evaluation function,

    Only the [[ ]]_R and [[ ]]_A evaluation functions return node lists (not sets, by the way).
    */



    public ArrayList<Node> visitFilterEqConst(XPathParser.FilterEqConstContext ctx) {
        ArrayList<Node> rp0 =  visit(ctx.rp());
        String str1 =  ctx.StringConstant().getText();
        for(Node n: rp0){
            if (n.getTextContent().equals(str1)){
                return rp0;
            }
        }
        return new ArrayList<>();
    }



    //Issue might occur here
    @Override
    public ArrayList<Node> visitFilterRp(XPathParser.FilterRpContext ctx) {
        ArrayList<Node> res = visit(ctx.rp());
        return res;
    }

    @Override
    public ArrayList<Node> visitFilterNot(XPathParser.FilterNotContext ctx) {
        ArrayList<Node> res = visit(ctx.filter());
        if(res.size() == 0){
            return res;
        }
        //else
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilterOr(XPathParser.FilterOrContext ctx) {
        ArrayList<Node> res0 =  visit(ctx.filter(0));
        ArrayList<Node> res1 =  visit(ctx.filter(1));
        res0.addAll(res1);

        if(res0.size() != 0){
            return res0;
        }
        return new ArrayList<>();
    }

    @Override
    public  ArrayList<Node> visitFilterAnd(XPathParser.FilterAndContext ctx) {
        ArrayList<Node> res0 =  visit(ctx.filter(0));
        ArrayList<Node> res1 =  visit(ctx.filter(1));
        ArrayList<Node> tmp = new ArrayList<Node>(res0);
        ArrayList<Node> satisfy_both = new ArrayList<Node>();

        for(Node n : tmp){
            if(res0.contains(n) && res1.contains(n)){
                satisfy_both.add(n);
            }
        }
        return satisfy_both;
    }

    @Override
    public  ArrayList<Node> visitFilterEq(XPathParser.FilterEqContext ctx) {
        ArrayList<Node> tmp = new ArrayList<>(currentNodes);

        ArrayList<Node> res0 = visit(ctx.rp(0));
        currentNodes = tmp;//set currentNodes back

        ArrayList<Node> res1 = visit(ctx.rp(1));
        currentNodes = tmp;

        for(Node i : res0){
            for(Node j : res1){
                //isEqualNode(): build in
                if(i.isEqualNode(j)){
                    //true
                    return tmp;
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public  ArrayList<Node> visitFilterIs(XPathParser.FilterIsContext ctx) {
        ArrayList<Node> tmp = new ArrayList<>(currentNodes);

        ArrayList<Node> res0 = visit(ctx.rp(0));
        currentNodes = tmp;//set currentNodes back

        ArrayList<Node> res1 = visit(ctx.rp(1));
        currentNodes = tmp;

        for(Node i : res0){
            for(Node j : res1){
                //isSameNode(): build in
                if(i.isSameNode(j)){
                    //true
                    return tmp;
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public ArrayList<Node> visitFilterCurrent(XPathParser.FilterCurrentContext ctx) {
        return visit(ctx.filter());
    }

    @Override
    // filename : NAME ('.' NAME)?
    public ArrayList<Node> visitFileName(XPathParser.FileNameContext ctx) {
        return visitRpChildren(ctx);
    }


}