package antlr;

import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

import org.xml.sax.SAXException;


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
	    Node root = xlmParser(ctx.fileName().getText());
	    currentNodes.add(root);
	    Queue<Node> queue = new LinkedList<>(currentNodes);
	    ArrayList<Node> res = new ArrayList<>(currentNodes);
	    getDesOrSelf(res, queue);
	    currentNodes = res;
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitRpParent(XPathParser.RpParentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpParent(ctx);
	}

	@Override
	public ArrayList<Node> visitRpAttName(XPathParser.RpAttNameContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpAttName(ctx);
	}

	@Override
	public ArrayList<Node> visitRpFromCurr(XPathParser.RpFromCurrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpFromCurr(ctx);
	}

	@Override
	public ArrayList<Node> visitRpText(XPathParser.RpTextContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpText(ctx);
	}

	@Override
	public ArrayList<Node> visitRpTagName(XPathParser.RpTagNameContext ctx) {
		// TODO Auto-generated method stub
        ArrayList<Node> temp = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
        	ArrayList<Node> children_temp = getChildren(node);
            for (Node n : children_temp) {
                if (n.getNodeName().equals(ctx.getText()))//TODO!
                    temp.add(n);
            }
        }

        currentNodes = temp;
		return temp;
	}

	@Override
	public ArrayList<Node> visitRpChildren(XPathParser.RpChildrenContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpChildren(ctx);
	}

	@Override
	public ArrayList<Node> visitRpCurrent(XPathParser.RpCurrentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpCurrent(ctx);
	}

	@Override
	public ArrayList<Node> visitRpConcat(XPathParser.RpConcatContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpConcat(ctx);
	}

	@Override
	public ArrayList<Node> visitRpRoot(XPathParser.RpRootContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpRoot(ctx);
	}

	@Override
	public ArrayList<Node> visitRpFilter(XPathParser.RpFilterContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpFilter(ctx);
	}

	@Override
	public ArrayList<Node> visitRpPathNodes(XPathParser.RpPathNodesContext ctx) {
		// TODO Auto-generated method stub
		return super.visitRpPathNodes(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterEqConst(XPathParser.FilterEqConstContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterEqConst(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterNot(XPathParser.FilterNotContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterNot(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterOr(XPathParser.FilterOrContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterOr(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterAnd(XPathParser.FilterAndContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterAnd(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterRp(XPathParser.FilterRpContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterRp(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterEq(XPathParser.FilterEqContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterEq(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterIs(XPathParser.FilterIsContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterIs(ctx);
	}

	@Override
	public ArrayList<Node> visitFilterCurrent(XPathParser.FilterCurrentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFilterCurrent(ctx);
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