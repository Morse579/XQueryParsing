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
		// TODO FINISHED
		Node temp = xlmParser(ctx.fileName().getText());
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitApCurrent(XPathParser.ApCurrentContext ctx) {
		// TODO FINISHED
	    Node root = xlmParser(ctx.fileName().getText());
	    this.currentNodes.add(root);
	    Queue<Node> queue = new LinkedList<>(this.currentNodes);
	    ArrayList<Node> res = new ArrayList<>(this.currentNodes);
	    getDesOrSelf(res, queue);//TODO!
	    this.currentNodes = res;
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitRpParent(XPathParser.RpParentContext ctx) {
		// TODO
		ArrayList<Node> temp = new ArrayList<Node>();
        for(Node n:this.currentNodes) {
            Node temp_node = n.getParentNode();
            if(temp_node != null && !temp.contains(temp_node)) {
                temp.add(temp_node);//may cause error
            }
        }
        this.currentNodes = temp;//change
        return temp;
	}

	@Override
	public ArrayList<Node> visitRpAttName(XPathParser.RpAttNameContext ctx) {
		// TODO FIXME
		ArrayList<Node> temp = new ArrayList<>();
		String target_name = ctx.attName().getText();
		for (Node n : this.currentNodes) {
			Element e = (Element) n;
			String att = e.getAttribute(target_name);
			if (att.length() > 0) {
				temp.add(n);
			}
		}
        return temp;
	}

	@Override
	public ArrayList<Node> visitRpFromCurr(XPathParser.RpFromCurrContext ctx) {
		// TODO
        visit(ctx.rp(0));
        Queue<Node> queue = new LinkedList<>(this.currentNodes);
        ArrayList<Node> all_children = new ArrayList<>(this.currentNodes);
        getDesOrSelf(all_children, queue);
        Set<Node> unique_curr = new HashSet<Node>(all_children);
        this.currentNodes = new ArrayList<Node>(unique_curr);
        visit(ctx.rp(1));
        Set<Node> unique_curr2 = new HashSet<Node>(this.currentNodes);
        this.currentNodes = new ArrayList<Node>(unique_curr2);
        return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
		// TODO
		ArrayList<Node> r = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
        	for (Node n: getChildren(node)) {
        		if(n.getNodeType() == Node.TEXT_NODE && n.getTextContent() != null) {
        			r.add(n);
        		}
        	}
        }
        this.currentNodes = r; //change
        return r;
	}

	@Override
	public ArrayList<Node> visitRpTagName(XPathParser.RpTagNameContext ctx) {
		// TODO
        ArrayList<Node> temp = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
        	ArrayList<Node> children_temp = getChildren(node);
            for (Node n : children_temp) {
                if (n.getNodeName().equals(ctx.getText()) && n.getNodeType() == Node.ELEMENT_NODE)//TODO!
                    temp.add(n);
            }
        }

        this.currentNodes = temp;//change
		return temp;
	}

	@Override
	public ArrayList<Node> visitRpChildren(XPathParser.RpChildrenContext ctx) {
		// TODO FIXME!!!
		ArrayList<Node> temp = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
            temp.addAll(getChildren(node));
        }
        this.currentNodes = temp; //change
        return temp;
	}

	@Override
	public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {
		// TODO FINISHED
		return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpConcat(XPathParser.RpConcatContext ctx) {
		// TODO
		ArrayList<Node> storeOrigin = this.currentNodes;
		visit(ctx.rp(0));
		ArrayList<Node> rp1 = this.currentNodes;//from rp1
		currentNodes = storeOrigin;
		visit(ctx.rp(1));
		ArrayList<Node> rp2 = this.currentNodes;//from rp2
		rp1.addAll(rp2);
		//FIXME!!!
        Set<Node> unique_curr = new HashSet<Node>(rp1);
        this.currentNodes = new ArrayList<Node>(unique_curr); //change
		return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpRoot(XPathParser.RpRootContext ctx) {
		// TODO rp '/' rp
        visit(ctx.rp(0));
        visit(ctx.rp(1));
        Set<Node> unique_curr = new HashSet<Node>(this.currentNodes);
        ArrayList<Node> temp = new ArrayList<>();
        for(Node n: unique_curr) {
        	temp.add(n);
        }
        this.currentNodes = temp;//FIXME!!! change
		return temp;
	}

	@Override
	public ArrayList<Node> visitRpFilter(XPathParser.RpFilterContext ctx) {
		// TODO
		//TODO!!!check
        visit(ctx.rp());
        ArrayList<Node> temp = new ArrayList<>(this.currentNodes);
        ArrayList<Node> res = new ArrayList<>();
        for (Node n : temp) {
        	this.currentNodes = new ArrayList<>();
        	this.currentNodes.add(n);
        	ArrayList<Node> filter_res = visit(ctx.filter());
            if (filter_res.size() != 0) {
                res.add(n);
            }
        }
        this.currentNodes = res;//change
        return res;
	}

	@Override
	public ArrayList<Node> visitRpPathNodes(XPathParser.RpPathNodesContext ctx) {
		//FINISHED
		ArrayList<Node> temp = visit(ctx.rp());
		return temp;
	}

	@Override
	public ArrayList<Node> visitFilterEqConst(XPathParser.FilterEqConstContext ctx) {
		// TODO
        ArrayList<Node> temp =  new ArrayList<Node>();
        ArrayList<Node> left = (ArrayList<Node>) visit(ctx.rp());
        String temp_str =  ctx.StringConstant().getText();
        temp_str = temp_str.replace("\"","");
        //System.out.println("target string:" + temp_str);
        for(Node n: left){
            if (n.getTextContent().equals(temp_str)){
            	this.currentNodes = left;
            	return left;
            }
        }
        return temp;
	}

	@Override
	public ArrayList<Node> visitFilterNot(XPathParser.FilterNotContext ctx) {
		// TODO check
//        HashSet<Node> rp1 = new HashSet<Node>(this.currentNodes);
//        HashSet<Node> rp_negative = new HashSet<Node>(visit(ctx.filter()));
//        HashSet<Node> temp = new HashSet<Node>();
//        temp.addAll(rp1);
//        temp.removeAll(rp_negative);
//        ArrayList<Node> temp_f = new ArrayList<Node>(temp);
//        this.currentNodes = new ArrayList<Node>(rp1);
//		return temp_f;
		ArrayList<Node> res_filted = visit(ctx.filter());
		if (res_filted.size() == 0){
			return this.currentNodes;
		}
		return new ArrayList<Node>();
	}

	@Override
	public ArrayList<Node> visitFilterOr(XPathParser.FilterOrContext ctx) {
		// TODO FIXME!!!
		ArrayList<Node> original = new ArrayList<Node>(this.currentNodes);
        ArrayList<Node> rp1 = visit(ctx.filter(0));
        this.currentNodes = original;
        ArrayList<Node> rp2 = visit(ctx.filter(1));
        rp1.addAll(rp2);
        Set<Node> union = new HashSet<Node>(rp1);
        ArrayList<Node> temp = new ArrayList<Node>(union);
        this.currentNodes = original; //check
		return temp;
	}

	@Override
	public ArrayList<Node> visitFilterAnd(XPathParser.FilterAndContext ctx) {
		// TODO check
		ArrayList<Node> original = this.currentNodes;
        ArrayList<Node> rp1 = visit(ctx.filter(0));
        this.currentNodes = original;
        ArrayList<Node> rp2 = visit(ctx.filter(1));
        // intersection FIXME!!!
        rp1.retainAll((rp2));
        Set<Node> intersection = new HashSet<Node>(rp1);
        ArrayList<Node> temp = new ArrayList<Node>(intersection);
        this.currentNodes = temp;//check FIXME!!!
        return temp;
	}

	@Override
	public ArrayList<Node> visitFilterRp(XPathParser.FilterRpContext ctx) {
		// TODO check!!!
        //ArrayList<Node> temp = new ArrayList<>(this.currentNodes);
        //ArrayList<Node> rt = visit(ctx.rp());
        //this.currentNodes = temp;
        visit(ctx.rp());
        return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitFilterEq(XPathParser.FilterEqContext ctx) {
		// TODO Auto-generated method stub
		//System.out.println("call FilterEq");
		ArrayList<Node> store = new ArrayList<>(this.currentNodes);//store original nodes

		ArrayList<Node> res0 = visit(ctx.rp(0));
		//System.out.println("first filter gives " + res0.size() + " nodes");
		this.currentNodes = store;//set currentNodes back

		ArrayList<Node> res1 = visit(ctx.rp(1));
		//System.out.println("second filter gives " + res0.size() + " nodes");
		this.currentNodes = store;

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
		// TODO Auto-generated method stub
		ArrayList<Node> store = new ArrayList<>(this.currentNodes);

		ArrayList<Node> res0 = visit(ctx.rp(0));
		this.currentNodes = store;//set currentNodes back

		ArrayList<Node> res1 = visit(ctx.rp(1));
		this.currentNodes = store;

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
		// TODO FINISHED	
		return visit(ctx.filter());
	}
	
	
	
	@Override
	public ArrayList<Node> visitTagName(XPathParser.TagNameContext ctx) {
		// TODO FINISHED check FIXME!!!
		return visitChildren(ctx);
	}

	@Override
	public ArrayList<Node> visitAttName(XPathParser.AttNameContext ctx) {
		// TODO FINISHED check FIXME!!!
		return visitChildren(ctx);
	}

	private Node xlmParser(String input_f) {
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

	private ArrayList<Node> getChildren(Node curr){
		ArrayList<Node> children = new ArrayList<Node>();
        NodeList temp = curr.getChildNodes();
        for (int i = 0; i < temp.getLength(); i++) {
        	children.add(temp.item(i));
        }
        return children;
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

}