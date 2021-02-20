package antlr;

import java.util.*;

import org.w3c.dom.*;

import javax.sound.midi.SysexMessage;
import javax.xml.parsers.*;
import java.io.*;

import org.xml.sax.SAXException;

import antlr.XQueryParser.AttNameContext;
import antlr.XQueryParser.TagNameContext;


public class MyXQueryVisitor extends XQueryBaseVisitor<ArrayList<Node>> {
	ArrayList<Node> currentNodes = new ArrayList<Node>();
	Map<String, ArrayList<Node>> xqMap = new HashMap<>();
	//TODO!!! check
	//Document document = null;

	@Override
	public ArrayList<Node> visitApRoot(XQueryParser.ApRootContext ctx) {
		//System.out.println("call visitApRoot");
		xlmParser(ctx.fileName().getText());
		return visitChildren(ctx);

	}

	@Override
	public ArrayList<Node> visitApCurrent(XQueryParser.ApCurrentContext ctx) {
		//System.out.println("call visitApCurrent");
		ArrayList<Node> root = xlmParser(ctx.fileName().getText());
		currentNodes = root;

		Queue<Node> queue = new LinkedList<>(this.currentNodes);
		ArrayList<Node> res = new ArrayList<>(this.currentNodes);
		getDescendent(res, queue);//TODO!
		this.currentNodes = res;
		visit(ctx.rp());
		//System.out.println("length of currentNodes: " + currentNodes.size());
		return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpParent(XQueryParser.RpParentContext ctx) {
		// CHECKED!
		ArrayList<Node> temp = new ArrayList<Node>();

		for(Node n: currentNodes) {
			Node temp_node = n.getParentNode();
			if(temp_node != null && !temp.contains(temp_node)) {
				//System.out.println("add a parent node");
				temp.add(temp_node);//may cause error
			}
		}

		currentNodes = temp;//change
		return temp;
	}

	@Override
	public ArrayList<Node> visitRpAttName(XQueryParser.RpAttNameContext ctx) {
		// TODO FINISHED
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
	public ArrayList<Node> visitRpFromCurr(XQueryParser.RpFromCurrContext ctx) {
		// TODO
        visit(ctx.rp(0));
        Queue<Node> queue = new LinkedList<>(this.currentNodes);
        ArrayList<Node> all_children = new ArrayList<>(this.currentNodes);
        getDescendent(all_children, queue);
        Set<Node> unique_curr = new HashSet<Node>(all_children);
        this.currentNodes = new ArrayList<Node>(unique_curr);
        visit(ctx.rp(1));
        Set<Node> unique_curr2 = new HashSet<Node>(this.currentNodes);
        this.currentNodes = new ArrayList<Node>(unique_curr2);
        return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpText(XQueryParser.RpTextContext ctx) {
		// TODO FINISHED
		ArrayList<Node> r = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
        	for (Node n: getChildren(node)) {
        		if(n.getNodeType() == Node.TEXT_NODE && n.getTextContent() != null) {
        			//if null is kept, output will have a lot of \n
        			r.add(n);
        		}
        	}
        }
        this.currentNodes = r; //change
        return r;
	}

	@Override
	public ArrayList<Node> visitRpTagName(XQueryParser.RpTagNameContext ctx) {
		//System.out.println("call visitRpTagName");
        ArrayList<Node> temp = new ArrayList<Node>();
        for (Node node : this.currentNodes) {
        	ArrayList<Node> children_temp = getChildren(node);
            for (Node n : children_temp) {
                if (n.getNodeName().equals(ctx.getText()) ) {
					//&& n.getNodeType() == Node.ELEMENT_NODE
					temp.add(n);
				}
            }
        }

        this.currentNodes = temp;//change
		return temp;
	}

	@Override
	public ArrayList<Node> visitRpChildren(XQueryParser.RpChildrenContext ctx) {
		// TODO FIXed

		//all next level children of the currentNodes
		ArrayList<Node> res = new ArrayList<>();
		for(Node n: currentNodes){
			NodeList n_children = n.getChildNodes();
			for(int i = 0; i < n_children.getLength(); i++){
				Node n_child = n_children.item(i);
				if(n_child.getNodeType() == Node.ELEMENT_NODE){
					//System.out.println("child content: " + n_child.getTextContent());
					res.add(n_child);
				}
			}
		}
		currentNodes = res;
		return res;


	}

	@Override
	public ArrayList<Node> visitRpCurrent(XQueryParser.RpCurrentContext ctx) {
		// TODO FINISHED
		return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpConcat(XQueryParser.RpConcatContext ctx) {
		// TODO
		ArrayList<Node> storeOrigin = this.currentNodes;
		visit(ctx.rp(0));
		ArrayList<Node> rp1 = this.currentNodes;//from rp1
		currentNodes = storeOrigin;
		visit(ctx.rp(1));
		ArrayList<Node> rp2 = this.currentNodes;//from rp2
		rp1.addAll(rp2);
		//FIXME!!!
        this.currentNodes = rp1; //change
		return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitRpRoot(XQueryParser.RpRootContext ctx) {
		// TODO rp '/' rp
        visit(ctx.rp(0));
        visit(ctx.rp(1));
        Set<Node> unique_curr = new HashSet<Node>(this.currentNodes);
        ArrayList<Node> temp = new ArrayList<>(unique_curr);

		return temp;
	}

	@Override
	public ArrayList<Node> visitRpFilter(XQueryParser.RpFilterContext ctx) {
		//System.out.println("call visitRpFilter");
		visit(ctx.rp());
		ArrayList<Node> temp = new ArrayList<>(currentNodes);
		ArrayList<Node> res = new ArrayList<>();
		for (Node n : temp) {
			currentNodes = new ArrayList<>();
			currentNodes.add(n);
			ArrayList<Node> filter_res = visit(ctx.filter());
			//System.out.println("number of nodes after the filter: " + filter_res.size());
			if (filter_res.size() != 0) {
				res.add(n);
			}
		}
		this.currentNodes = res;//change
		return res;
	}

	@Override
	public ArrayList<Node> visitRpPathNodes(XQueryParser.RpPathNodesContext ctx) {
		//FINISHED
		ArrayList<Node> temp = visit(ctx.rp());
		return temp;
	}

	@Override
	public ArrayList<Node> visitFilterEqConst(XQueryParser.FilterEqConstContext ctx) {
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
	public ArrayList<Node> visitFilterNot(XQueryParser.FilterNotContext ctx) {
		//fixed!
		//original issue: we forget to set currentNodes back
		//System.out.println("FilterNOT: call visitFilterNot");
		ArrayList<Node> original = new ArrayList<Node>(currentNodes);
		ArrayList<Node> res_filted = visit(ctx.filter());
		currentNodes = original;

		if (res_filted.size() == 0){
			//System.out.println("FilterNOT: all current nodes satisfy 'not filter'");
			return currentNodes;
		}
		return new ArrayList<Node>();
	}

	@Override
	public ArrayList<Node> visitFilterOr(XQueryParser.FilterOrContext ctx) {
		// TODO FIXME!!!
		ArrayList<Node> original = new ArrayList<Node>(this.currentNodes);
        ArrayList<Node> rp1 = visit(ctx.filter(0));
        this.currentNodes = original;
        ArrayList<Node> rp2 = visit(ctx.filter(1));
        rp1.addAll(rp2);
        Set<Node> union = new HashSet<Node>(rp1);
        ArrayList<Node> temp = new ArrayList<Node>(union);
        //this.currentNodes = original; //check
		return temp;
	}

	@Override
	public ArrayList<Node> visitFilterAnd(XQueryParser.FilterAndContext ctx) {
		// TODO check
		ArrayList<Node> original = this.currentNodes;
        ArrayList<Node> rp1 = visit(ctx.filter(0));
        this.currentNodes = original;
        ArrayList<Node> rp2 = visit(ctx.filter(1));
        // intersection FIXME!!!
        rp1.retainAll((rp2));
        Set<Node> intersection = new HashSet<Node>(rp1);
        ArrayList<Node> temp = new ArrayList<Node>(intersection);
        //this.currentNodes = temp;//check FIXME!!!
        return temp;
	}

	@Override
	public ArrayList<Node> visitFilterRp(XQueryParser.FilterRpContext ctx) {
		// TODO check!!!
        //ArrayList<Node> temp = new ArrayList<>(this.currentNodes);
        //ArrayList<Node> rt = visit(ctx.rp());
        //this.currentNodes = temp;
        visit(ctx.rp());
        return this.currentNodes;
	}

	@Override
	public ArrayList<Node> visitFilterEq(XQueryParser.FilterEqContext ctx) {
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
	public ArrayList<Node> visitFilterIs(XQueryParser.FilterIsContext ctx) {
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
	public ArrayList<Node> visitFilterCurrent(XQueryParser.FilterCurrentContext ctx) {
		return visit(ctx.filter());
	}
	
	
	
	@Override
	public ArrayList<Node> visitTagName(XQueryParser.TagNameContext ctx) {
		// TODO FINISHED check FIXME!!!
		return visitChildren(ctx);
	}

	@Override
	public ArrayList<Node> visitAttName(XQueryParser.AttNameContext ctx) {
		// TODO FINISHED check FIXME!!!
		return visitChildren(ctx);
	}

	private ArrayList<Node> xlmParser(String input_f) {
		ArrayList<Node> temp = new ArrayList<>();
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
			document = builder.parse(new File(input_f));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		return document.getDocumentElement();
		document.getDocumentElement().normalize();
		temp.add(document);
		currentNodes = temp;
		//System.out.println("1: length of currentNodes: " + currentNodes.size());
		return temp;

	}



	private ArrayList<Node> getChildren(Node curr){
		//only retrieve children of next level
		ArrayList<Node> children = new ArrayList<Node>();
        NodeList temp = curr.getChildNodes();
        for (int i = 0; i < temp.getLength(); i++) {
        	children.add(temp.item(i));
        }
        return children;
	}
	

    private void getDescendent(ArrayList<Node> res, Queue<Node> queue) {
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
    
    
    //XQ START

	@Override
	public ArrayList<Node> visitXqVar(XQueryParser.XqVarContext ctx) {
		return xqMap.get(ctx.getText());
	}

	//TODO!!! CHECK
	@Override
	public ArrayList<Node> visitXqStrConst(XQueryParser.XqStrConstContext ctx) {
		String s  = ctx.StringConstant().getText(); //need to check the input string
		Node nodeT = makeText(s); //calling helper function
		ArrayList<Node> temp = new ArrayList<Node>();
		temp.add(nodeT);
		return temp;
	}

	@Override
	public ArrayList<Node> visitXqAp(XQueryParser.XqApContext ctx) {
		return visit(ctx.ap());
	}

	@Override public ArrayList<Node> visitXqParenthesis(XQueryParser.XqParenthesisContext ctx) {
		return visit(ctx.xq());
	}

	@Override public ArrayList<Node> visitXqConcat(XQueryParser.XqConcatContext ctx) {
		HashMap<String, ArrayList<Node>> originCopy = new HashMap<>(xqMap);
		ArrayList<Node> temp = new ArrayList<Node>();
		ArrayList<Node> ans = new ArrayList<Node>();
		ArrayList<Node> left = (ArrayList<Node>) visit(ctx.xq(0));
		temp.addAll(left);
		xqMap = originCopy;
		ArrayList<Node> right = (ArrayList<Node>) visit(ctx.xq(1));
		temp.addAll(right);
		xqMap = originCopy;

		for(Node n: temp){
			if(!ans.contains(n)){
				ans.add(n);
			}
		}
		return ans;
	}

	@Override
	public ArrayList<Node> visitXqDescend(XQueryParser.XqDescendContext ctx) {
		currentNodes = visit(ctx.xq());
		return visit(ctx.rp());
	}

	@Override
	public ArrayList<Node> visitXqFromCurr(XQueryParser.XqFromCurrContext ctx) {
		currentNodes = visit(ctx.xq());
		ArrayList<Node> res = new ArrayList<>(currentNodes);
		Queue<Node> queue = new LinkedList<>(currentNodes);
		getDescendent(res, queue);
		currentNodes = res;
		visit(ctx.rp());
		return currentNodes;
	}
	
	
	//HELPER FUNCTIONS 
	//makeElem
	public Node makeElem(String tagName, List<Node> list){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = builder.newDocument();

		Node newElem = doc.createElement(tagName);
		for (Node node : list) {
			Node newNode = doc.importNode(node, true);
			newElem.appendChild(newNode);
		}
		return newElem;
	}
	
	//makeText
	//TODO!!! check
	public Node makeText(String stringConst){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		//Build Document
		Document doc = builder.newDocument();;
		Node textNode = doc.createTextNode(stringConst);
		return textNode;
	}
	

	@Override
	public ArrayList<Node> visitXqNew(XQueryParser.XqNewContext ctx) {
		ArrayList<Node> temp = visit(ctx.xq());
		String tagName = ctx.tagName(0).getText();
		ArrayList<Node> ans = new ArrayList<>();
		ans.add(makeElem(tagName, temp));
		return ans;
	}

	@Override
	public ArrayList<Node> visitXqFLWR(XQueryParser.XqFLWRContext ctx) {
		//TODO! check
		ArrayList<Node> temp = new ArrayList<Node>();
		Map<String, ArrayList<Node>> original = new HashMap<>(this.xqMap);
		//check forClause
		if(ctx.forClause()==null) {
			return temp;
		}
		//check letClause
		if(ctx.letClause()!=null) {
			visit(ctx.letClause());
		}
		//check whereClause
		if(ctx.whereClause()!=null) {
			visit(ctx.whereClause());
		}
		//check returnClause
        temp.addAll(visit(ctx.returnClause()));
		return temp;
	}

	@Override
	public ArrayList<Node> visitXqLet(XQueryParser.XqLetContext ctx) {
		HashMap<String, ArrayList<Node>> temp = new HashMap<>(xqMap);
		visit(ctx.letClause());
		ArrayList<Node> res = visit(ctx.xq());
		xqMap = temp;
		return res;
	}

	@Override
	public ArrayList<Node> visitForClause(XQueryParser.ForClauseContext ctx) {
		//for every FOR loop, we store the var and the corresponding xq
		int for_index = 0;

		HashMap<String, ArrayList<Node>> original = new HashMap<>(textMap);
		ArrayList<HashMap<String, Node>> allPairs = new ArrayList<>();
		ArrayList<HashMap<String, ArrayList<Node>>> res = new ArrayList<>();

		//A double entry queue storing maps, each map stores a var:node pair
		Deque<HashMap<String, Node>> deque = new Deque<HashMap<String, Node>><>();
		//dealing with the var0 and xq0
		String var0 = ctx.var(0).getText();
		ArrayList<Node> list_0 = (ArrayList<Node>) visit(ctx.xq(0));
		for (Node n : list_0) {
			HashMap<String, Node> map = new HashMap<>();
			map.put(var0, n);
			deque.offer(map);
		}
		for_index++;
		//for every var in the first for-loop
		//there may be var2 under the first for-loop
		while (for_index < ctx.xq().size()) {
			for (int i = 0; i < deque.size(); ++i) {
				//var_n
				HashMap<String, Node> currMap = deque.poll();
				String currVar = ctx.var(idx).getText();

				//just run for once
				for (String var : currMap.keySet()) {
					ArrayList<Node> objs = new ArrayList<>();
					objs.add(currMap.get(var));
					xqMap.put(var, objs);
				}
				//	for var_n+1 in ...:
				//dealing with next xq
				ArrayList<Node> list_i = (ArrayList<Node>) visit(ctx.xq(for_index));

				for (Node n : list_i) {
					HashMap<String, Node> map = new HashMap<>(currMap);
					map.put(currVar, n);
					deque.offer(map);
				}
			}
			for_index++;

		}

		while (!deque.isEmpty()) {
			allPairs.add(deque.poll());
		}

		for (HashMap<String, Node> map : allPairs) {
			HashMap<String, ArrayList<Node>> temp = new HashMap<>();
			//run once, convert each map to var,ArrayList<Node> format
			for (String var : map.keySet()) {
				ArrayList<Node> nodeList = new ArrayList<>();
				nodeList.add(map.get(var));
				temp.put(var, nodeList);
			}
			res.add(temp);
		}

		//reset
		xqMap = original;

		return res;

	}

	@Override
	public ArrayList<Node> visitLetClause(XQueryParser.LetClauseContext ctx) {
		//TODO
		//fix style
		for (int i = 0; i < ctx.var().size(); i++) {
			xqMap.put(ctx.var(i).getText(), visit(ctx.xq(i)));
		}
		return currentNodes;
	}

	@Override
	public ArrayList<Node> visitWhereClause(XQueryParser.WhereClauseContext ctx) {
		return visit(ctx.cond());
	}

	@Override
	public ArrayList<Node> visitReturnClause(XQueryParser.ReturnClauseContext ctx) {
		return visit(ctx.xq());
	}



	//XQ Cond START

	@Override
	public ArrayList<Node> visitXqCondEq(XQueryParser.XqCondEqContext ctx) {
		ArrayList<Node> originNodesCopy = new ArrayList<>(currentNodes);
		Map<String, ArrayList<Node>> originMapCopy = new HashMap<>(xqMap);
		ArrayList<Node> res0 = visit(ctx.xq(0));
		currentNodes = originNodesCopy;
		xqMap = originMapCopy;
		ArrayList<Node> res1 = visit(ctx.xq(1));
		currentNodes = originNodesCopy;
		xqMap = originMapCopy;

		for(Node i : res0){
			for(Node j : res1){
				//isEqualNode(): build in
				if(i.isEqualNode(j)){
					//true
					return originNodesCopy;
				}
			}
		}

		return new ArrayList<>();
	}

	@Override
	public ArrayList<Node> visitXqCondIs(XQueryParser.XqCondIsContext ctx) {
		ArrayList<Node> originNodesCopy = new ArrayList<>(currentNodes);

		ArrayList<Node> res0 = visit(ctx.xq(0));
		currentNodes = originNodesCopy;
		ArrayList<Node> res1 = visit(ctx.xq(1));
		currentNodes = originNodesCopy;

		for(Node i : res0){
			for(Node j : res1){
				//isSameNode(): build in
				if(i.isSameNode(j)){
					//true
					return originNodesCopy;
				}
			}
		}

		return new ArrayList<>();

	}

	@Override
	public ArrayList<Node> visitXqCondEmpty(XQueryParser.XqCondEmptyContext ctx) {
		ArrayList<Node> originNodesCopy = new ArrayList<>(currentNodes);
		ArrayList<Node> res = visit(ctx.xq());
		currentNodes = originNodesCopy;

		if(res.size() == 0){
			return currentNodes;
		}
		return new ArrayList<>();
	}

	@Override
	public ArrayList<Node> visitXqCondSatisfy(XQueryParser.XqCondSatisfyContext ctx) {
		for (int i = 0; i < ctx.var().size(); i++) {
			ArrayList<Node> nodes = visit(ctx.xq(i));
			xqMap.put(ctx.var(i).getText(), nodes);
		}
		return visit(ctx.cond());
	}

	@Override
	public ArrayList<Node> visitXqCondXqParenthesis(XQueryParser.XqCondXqParenthesisContext ctx) {
		return visit(ctx.cond());
	}

	@Override
	public ArrayList<Node> visitXqCondAnd(XQueryParser.XqCondAndContext ctx) {
		//TODO: CHECK ME
		//CURRENTLY FOLLOWING THE SAME FORMAT AS FILTER AND
		ArrayList<Node> originNodesCopy = currentNodes;
		ArrayList<Node> res0 = visit(ctx.cond(0));
		currentNodes = originNodesCopy;
		ArrayList<Node> res1 = visit(ctx.cond(1));

		res0.retainAll(res1);
		Set<Node> intersection = new HashSet<Node>(res1);
		ArrayList<Node> res = new ArrayList<Node>(intersection);
		return res;
	}

	//
	@Override
	public ArrayList<Node> visitXqCondOr(XQueryParser.XqCondOrContext ctx) {
		ArrayList<Node> originNodesCopy = new ArrayList<Node>(this.currentNodes);
		ArrayList<Node> cond1 = visit(ctx.cond(0));
		currentNodes = originNodesCopy;
		ArrayList<Node> cond2 = visit(ctx.cond(1));
		cond1.addAll(cond2);
		Set<Node> union = new HashSet<Node>(cond1);
		ArrayList<Node> res = new ArrayList<Node>(union);
		return res;
	}

	//'not' cond
	@Override
	public ArrayList<Node> visitXqCondNot(XQueryParser.XqCondNotContext ctx) {
		ArrayList<Node> original = new ArrayList<Node>(currentNodes);
		ArrayList<Node> res = visit(ctx.cond());
		if(res.size() == 0){
			currentNodes = original;
			return currentNodes;
		}
		return new ArrayList<>();
	}



	@Override public ArrayList<Node> visitVar(XQueryParser.VarContext ctx) {
		return xqMap.get(ctx.getText());
	}








}