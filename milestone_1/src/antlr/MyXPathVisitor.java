package antlr;

import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

import org.xml.sax.SAXException;

import antlr.XPathParser.AttNameContext;
import antlr.XPathParser.TagNameContext;


public class MyXPathVisitor extends XPathBaseVisitor<ArrayList<Node>> {
	ArrayList<Node> currentNodes = new ArrayList<Node>();

	@Override
	public ArrayList<Node> visitApRoot(XPathParser.ApRootContext ctx) {
		// TODO Auto-generated method stub
		Node result = xlmParser(ctx.fileName().getText());
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
		// TODO Auto-generated method stub
		System.out.println("call visitApCurrent");
	    Node root = xlmParser(ctx.fileName().getText());
	    currentNodes.add(root);
	    Queue<Node> queue = new LinkedList<>(currentNodes);
	    ArrayList<Node> res = new ArrayList<>(currentNodes);
	    getDesOrSelf(res, queue);
	    currentNodes = res;
	    System.out.print("current node number: " + currentNodes.size());
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitRpParent(XPathParser.RpParentContext ctx) {
		ArrayList<Node> res = new ArrayList<>();
		for (Node n: currentNodes){
			Node tempParent =  n.getParentNode();
			//ensure no null node or duplicate parent
			if(tempParent != null && !res.contains(tempParent)){
				res.add(tempParent);
			}
		}
		currentNodes = res;
		return res;
	}

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

	@Override
	public ArrayList<Node> visitRpFromCurr(XPathParser.RpFromCurrContext ctx) {
		visit(ctx.rp(0));
		Queue<Node> queue = new LinkedList<>(currentNodes);
		ArrayList<Node> all_children = new ArrayList<>(currentNodes);
		ArrayList<Node> res0 = new ArrayList<>();

		getAllChildren(all_children, queue);
		for(Node n : all_children){
			if(!res0.contains(n)){
				res0.add(n);
			}
		}
		currentNodes = res0;
		ArrayList<Node> res1 = new ArrayList<>();

		visit(ctx.rp(1));
		for(Node n : currentNodes){
			if(!res1.contains(n)){
				res1.add(n);
			}
		}
		currentNodes = res1;
		return res1;
	}

	@Override
	public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
		System.out.println("call visitRpText");
		//to extract all text node under current nodes
		ArrayList<Node> res = new ArrayList<>();
		//System.out.println("currentNode number: " + currentNodes.size());
		for (Node n : currentNodes) {
			//System.out.println("currentNode name: " + n.getNodeName() + " content: " + n.getTextContent());
			NodeList n_children = n.getChildNodes();
			if (n_children.getLength() == 0){
				continue;
			}
			for (int i = 0; i < n_children.getLength(); i++) {
				Node n_child = n_children.item(i);
				//to ensure the text nodes added in have valid content
				if (n_child.getNodeType() == Node.TEXT_NODE && n_child.getTextContent() != null) {
					System.out.println("add text node to result: ");
					System.out.println("Node name: " + n.getNodeName() + " content: " + n.getTextContent());
					res.add(n_child);
				}
			}
		}
		currentNodes = res;
		return res;
	}

	@Override
	public ArrayList<Node> visitRpTagName(XPathParser.RpTagNameContext ctx) {
		ArrayList<Node> res = new ArrayList<Node>();
		ArrayList<Node> childrenList = getChildren(currentNodes);
		String tag = ctx.getText();
		for (Node n: childrenList)
			if (n.getNodeName().equals(tag) && n.getNodeType() == Node.ELEMENT_NODE) {
				res.add(n);
			}
		currentNodes = res;
		return res;
	}

	@Override
	public ArrayList<Node> visitRpChildren(XPathParser.RpChildrenContext ctx) {
		//all next level children of the currentNodes
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
	public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {
		return currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpConcat(XPathParser.RpConcatContext ctx) {
		ArrayList<Node> storeOrigin = currentNodes;
		visit(ctx.rp(0));
		ArrayList<Node> res0 = currentNodes;//from rp1
		currentNodes = storeOrigin;
		visit(ctx.rp(1));
		ArrayList<Node> res1 = currentNodes;//from rp2
		res0.addAll(res1);

		currentNodes = res0;
		return res0;
	}

	@Override
	public ArrayList<Node> visitRpRoot(XPathParser.RpRootContext ctx) {
		visit(ctx.rp(0));
		ArrayList<Node> res = visit(ctx.rp(1));
		return res;
	}

	@Override
	public ArrayList<Node> visitRpFilter(XPathParser.RpFilterContext ctx) {
		//visit(ctx.rp());
		System.out.println("call Rpfilter");
		ArrayList<Node> nodes = visit(ctx.rp());
		ArrayList<Node> res = new ArrayList<>();

		for (Node n : nodes) {
			currentNodes = new ArrayList<>();
			currentNodes.add(n);
			ArrayList<Node> filter_res = visit(ctx.filter());
			if (filter_res.size() != 0) {
				System.out.print("node tag name: " + n.getNodeName() +" tag content: "+ n.getTextContent());
				res.add(n);
			}
		}

		currentNodes = res;
		return res;
	}

	@Override
	public ArrayList<Node> visitRpPathNodes(XPathParser.RpPathNodesContext ctx) {
		ArrayList<Node> res = visit(ctx.rp());
		return res;
	}

	@Override
	public ArrayList<Node> visitFilterEqConst(XPathParser.FilterEqConstContext ctx) {
		System.out.println("call FilterEq String constant");
		ArrayList<Node> rp0 =  visit(ctx.rp());
		String str1 =  ctx.StringConstant().getText();
		str1 = str1.replace("\"","");
		System.out.println("target string:" + str1 + "number of nodes to search: " + rp0.size());
		//System.out.println("PUBLIUS equals PUBLIUS?: " + str1.equals("PUBLIUS"));
		for(Node n: rp0){
			System.out.println("this node's text content: " +  n.getTextContent());
			if (n.getTextContent().equals(str1)){
				currentNodes = rp0;
				return rp0;
			}
		}
		return new ArrayList<>();
	}

	@Override
	public ArrayList<Node> visitFilterNot(XPathParser.FilterNotContext ctx) {
		ArrayList<Node> orginal = currentNodes;
		ArrayList<Node> satisfyFilter = visit(ctx.filter());
		for (Node n : orginal){
			if (satisfyFilter.contains(n)){
				orginal.remove(n);
			}
		}

		return orginal;
	}

	@Override
	public ArrayList<Node> visitFilterOr(XPathParser.FilterOrContext ctx) {
		ArrayList<Node> res0 =  visit(ctx.filter(0));
		ArrayList<Node> res1 =  visit(ctx.filter(1));
		//keep duplicates?
		res0.addAll(res1);

		return res0;
	}

	@Override
	public ArrayList<Node> visitFilterAnd(XPathParser.FilterAndContext ctx) {
		ArrayList<Node> original = currentNodes;
		System.out.println("FilterAnd is called");
		System.out.println("carry out filter 0");
		ArrayList<Node> res0 = visit(ctx.filter(0));
		//test：
		System.out.println("filter 0 gives nodes:");
		for(Node i : res0){
			System.out.println("currentNode name: " + i.getNodeName() + " content: " + i.getTextContent());
		}

		if(res0.size() == 0){
			System.out.println("Doesn't even satisfy first filter. RETURN");
			return res0;
		}
		currentNodes = original;
		System.out.println("carry out filter 1");
		ArrayList<Node> res1 =  visit(ctx.filter(1));
		//test：
		System.out.println("filter 1 gives nodes:");
		for(Node j : res1){
			System.out.println("currentNode name: " + j.getNodeName() + " content: " + j.getTextContent());
		}
		ArrayList<Node> tmp = new ArrayList<Node>(res0);
		tmp.addAll(res1);
		ArrayList<Node> satisfy_both = new ArrayList<Node>();

		for(Node n : tmp){
			//System.out.println("currentNode name: " + n.getNodeName() + " content: " + n.getTextContent());
			if(res0.contains(n) && res1.contains(n)){
				//keep duplicates?
				//&& !satisfy_both.contains(n)
				satisfy_both.add(n);
			}
		}
		return satisfy_both;
	}

	@Override
	public ArrayList<Node> visitFilterRp(XPathParser.FilterRpContext ctx) {
		ArrayList<Node> res = visit(ctx.rp());
		return res;
	}

	@Override
	public ArrayList<Node> visitFilterEq(XPathParser.FilterEqContext ctx) {
		System.out.println("call FilterEq");
		ArrayList<Node> store = new ArrayList<>(currentNodes);//store original nodes

		ArrayList<Node> res0 = visit(ctx.rp(0));
		System.out.println("first filter gives " + res0.size() + " nodes");
		currentNodes = store;//set currentNodes back

		ArrayList<Node> res1 = visit(ctx.rp(1));
		System.out.println("second filter gives " + res0.size() + " nodes");
		currentNodes = store;

		for(Node i : res0){
			for(Node j : res1){
				//isEqualNode(): build in
				if(i.isEqualNode(j)){
					//true
					return store;
				}
			}
		}

		return new ArrayList<>();
	}

	@Override
	public ArrayList<Node> visitFilterIs(XPathParser.FilterIsContext ctx) {
		ArrayList<Node> store = new ArrayList<>(currentNodes);

		ArrayList<Node> res0 = visit(ctx.rp(0));
		currentNodes = store;//set currentNodes back

		ArrayList<Node> res1 = visit(ctx.rp(1));
		currentNodes = store;

		for(Node i : res0){
			for(Node j : res1){
				//isSameNode(): build in
				if(i.isSameNode(j)){
					//true
					return store;
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
	public ArrayList<Node> visitTagName(XPathParser.TagNameContext ctx) {
		ArrayList<Node> res = new ArrayList<>();
		ArrayList<Node> children = getChildren(currentNodes);
		for (Node n : children) {
			//tag match
			if (n.getNodeName().equals(ctx.getText())) {
				res.add(n);
			}
		}
		currentNodes = res;
		return res;
	}

	@Override
	public ArrayList<Node> visitAttName(XPathParser.AttNameContext ctx) {
		ArrayList<Node> res = new ArrayList<>();
		String target_name = ctx.getText();
		for (Node n : currentNodes) {
			Element e = (Element) n;
			String att = e.getAttribute(target_name);
			if (att.length() > 0) {
				res.add(n);
			}
		}
		currentNodes = res;
		return res;
	}

	private Node xlmParser(String input_f) {
		System.out.println("ENTER");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		//Build Document
		Document document = null;
		try {
			document = builder.parse(input_f);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		document.getDocumentElement().normalize();
		Element root = document.getDocumentElement();
		return root;
	}

//	private ArrayList<Node> getChildren(Node curr){
//		ArrayList<Node> children = new ArrayList<Node>();
//        NodeList temp = curr.getChildNodes();
//        for (int i = 0; i < temp.getLength(); i++) {
//        	children.add(temp.item(i));
//        }
//        return children;
//	}
	private ArrayList<Node> getChildren(ArrayList<Node> nodes) {
		//get next level children
		ArrayList<Node> res = new ArrayList<>();
		for (Node n : nodes) {
			//node n's next level children
			NodeList n_children = n.getChildNodes();
			for (int i = 0; i < n_children.getLength(); i++) {
				res.add(n_children.item(i));
			}
		}
		return res;
	}
	
	//TODO!
    private void getDesOrSelf(ArrayList<Node> res, Queue<Node> queue) {
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

	private void getAllChildren(ArrayList<Node> res, Queue<Node>  queue) {
		//get all children of descending levels
		while (queue.size() > 0) {
			Node n = queue.poll();
			NodeList n_children = n.getChildNodes();
			if(n_children.getLength() == 0){
				continue;
			}
			for (int i = 0; i < n_children.getLength(); i++) {
				res.add(n_children.item(i));
				queue.add(n_children.item(i));
			}
		}
	}

}