package antlr;

import java.io.File;
import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Optimized {

    String indent = "             ";

    private String PrintJoinCond(LinkedList<String> ret0, LinkedList<String> ret1, String output) {
//        System.out.println("call PrintJoinCond()");
//        System.out.println("take variable 0 and 1 and write the [tuple],[tuple] part in joinClause");

        //if the return value is empty
        output += "             [";
        for(int i = 0; i < ret0.size();i++) {
            output += ret0.get(i);
            if(i != ret0.size()-1) {
                output +=",";
            }
        }
        output +="], [";
        for(int i = 0; i < ret1.size();i++) {
            output +=ret1.get(i);
            if(i != ret1.size()-1) {
                output +=",";
            }
        }
        output += "] ";
        return output;
    }


    public String printForandWhere(XQueryParser.XqFLWRContext ctx, String output, ArrayList<ArrayList<String>> classify, String[][] cond , int[][] relaWhere){
        //print for clause
        int numFor = ctx.forClause().var().size();
        for(int i = 0; i < classify.size(); i++) {
            ArrayList<String> curSet = classify.get(i);

            //tuples == the string after return
            String tuples = "";

            int count = 0;

            //print for
            for(int k = 0; k < numFor; k++) {
                String key = ctx.forClause().var(k).getText();
                if(curSet.contains(key)){
                    //if it's the first for clause, add "for" keyword
                    if(count == 0) {
                        String tmpp = ctx.forClause().xq(k).getText();
//                        System.out.println("tmpp:" + tmpp);
                        String[] sl = tmpp.split("return",2);
                        //if sl contains the key word "return"
//                        System.out.println("split with return gives an array of size: " + sl.length );
                        if(sl.length == 2) {
                            tmpp = sl[0] + " return " + sl[1];
                        }
                        output += indent.repeat(count) + "for " + key + " in " + tmpp;

                        count++;

                    }else {
                        output += ",\n";
                        output += indent.repeat(count) + key + " in " + ctx.forClause().xq(k).getText();

                    }


                    if(tuples.equals("")) {
                        tuples = tuples + " <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }else {
//                        System.out.println("in tuples else");
                        tuples = tuples + ", <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }
                }
            }
            output += "\n";

            //print where
//            System.out.println("print where");
            String temp = "";
            int count1 = 0;
            for(int j = 0;j < cond.length;j++) {
//                System.out.println("cond length: " + cond.length);
                if(relaWhere[j][1] == -1 && curSet.contains(cond[j][0])) {
                    if(count1 == 0){
                        count1++;
                        output += indent.repeat(count) + "where " + cond[j][0] + " eq " + cond[j][1] +"\n";
                        temp += "where " + cond[j][0] + " eq " + cond[j][1] +"\n";
                    }else {
                        output += indent.repeat(count) + " and  " + cond[j][0] + " eq " + cond[j][1] + "\n";
                        temp += " and  " + cond[j][0] + " eq " + cond[j][1] + "\n";
                    }
                }
            }
//            System.out.println("where adds:");
//            System.out.println(temp);



            //print return
//            System.out.println("print return of For and Where");
            tuples = "<tuple> {"+tuples+"} </tuple>,";
//            System.out.println("working on tuples:" + tuples);
//            System.out.println(tuples);
            output += indent.repeat(count) + "return " + tuples + "\n";


            // the third and fourth arguments specify that the join is on attributes
            if(i > 0) {
                LinkedList<String> ret0 = new LinkedList<>();
                LinkedList<String> ret1 = new LinkedList<>();

                for (int ii = 0; ii < cond.length; ii++) {
//                    System.out.println("cond[ii][0]: " + cond[ii][0]);
//                    System.out.println("cond[ii][1]: " + cond[ii][1]);

                    if (relaWhere[ii][0] == i && (relaWhere[ii][1] >= 0 && relaWhere[ii][1] < i)) {
                        ret0.add(cond[ii][1].substring(1));
                        ret1.add(cond[ii][0].substring(1));
                    } else if (relaWhere[ii][1] == i && (relaWhere[ii][0] >= 0 && relaWhere[ii][0] < i)) {
                        ret0.add(cond[ii][0].substring(1));
                        ret1.add(cond[ii][1].substring(1));
                    }
                }
                output = PrintJoinCond(ret0, ret1, output);
                if(output == "")
                    return "";
                if(i != classify.size() - 1)
                    output += "),\n";
                else
                    output += ")\n";

            }
        }
        return output;
    }


    public String inputRewrite(XQueryParser.XqFLWRContext ctx) {
        String result = "";

        //-----grouping forClause:------
        XQueryParser.ForClauseContext forClause_ctx = ctx.forClause();

        //group the key that's relevent to each othe in one trunk
        //e.g.
        //for $b1 in doc("input")/book,
        //$aj in $b1/author/first/text(),
        //$a1 in $b1/author,
        //$af1 in $a1/first,
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();;
        for (int i = 0; i < forClause_ctx.xq().size(); i++) {
            String xq = forClause_ctx.xq(i).getText();
            String key = forClause_ctx.var(i).getText();
            String parent = xq.split("/")[0];

            //check if current node groups contains this parent, if so, put current key into that group too
            if (groups.size() == 0) {
                ArrayList<String> newGroup = new ArrayList<String>();
                newGroup.add(key);
                groups.add(newGroup);
            } else {
                boolean findGroup = false;
                for (int j = 0; j < groups.size(); j++) {
                    ArrayList<String> workingGroup = groups.get(j);
                    if (workingGroup.contains(parent)) {
                        findGroup = true;
                        workingGroup.add(key);
                        groups.set(j, workingGroup);
                    }
                }

                if (!findGroup) {
                    ArrayList<String> newGroup = new ArrayList<String>();
                    newGroup.add(key);
                    groups.add(newGroup);
                }
            }
        }

        if (groups.size() == 1) {
            //all keys are relevent to each other. no join operation needed
            return "";
        }


        //------separate whereClause:------
        XQueryParser.WhereClauseContext whereClause_ctx = ctx.whereClause();

        //e.g.
        //where $aj eq "John" and
        //$af1 eq $af21 and $al1 eq $al21 and
        //$af22 eq $af3 and $al22 eq $al3
        //all conditions in allConds need to be satisfy


        String[] allConds = whereClause_ctx.cond().getText().split("and");
        int numConds = allConds.length;
        int[][] varGroup = new int[numConds][2]; //each entry represents a group a variable belongs to
        String[][] eqVars = new String[numConds][2]; //variables before and after "eq"
//        System.out.println("allConds.length: " + allConds.length);

        for (int i = 0; i < allConds.length; i++) {
            String workingCond = allConds[i];
//            System.out.println("working on condition: \n" + workingCond);
            String[] vars = new String[2];
            if (workingCond.contains("eq") || workingCond.contains("=")) {
//                System.out.println("condition contains eq or =");
                vars = workingCond.split("eq|=");
            }

            //trim leading and trailing whitespaces
            //vars.size should be at most 2
            for (int j = 0; j < vars.length; j++) {
                varGroup[i][j] = -1;
//                System.out.println("vars[j]" + vars[j]);
                vars[j] = vars[j].trim();
                //check which group this var belongs to
                //var1 may belongs to group 1 while var2 belongs to group 2
                for (int k = 0; k < groups.size(); k++) {
                    if (groups.get(k).contains(vars[j])) {
                        //the ith condition evaluation's jth (first or second) conponent:
                        varGroup[i][j] = k;
                    }
                }

            }

            eqVars[i] = vars;

        }
//        System.out.println("eqVars.size(): " + eqVars.length);
        //start to print the "join" heading:
        //if there are n groups, group into n-1 tuple:
        //e.g. 3 groups ==> tuple(tuple(group1,group2),group3)
        result += "for $tuple in ";
        //
        for (int i = 1; i < groups.size(); i++) {
            result += "join (\n";
        }

        result = printForandWhere(ctx, result, groups, eqVars ,varGroup);


        //------return components in each for group------
//        System.out.println("working on return in rewrite");
        // convert the return statement in original form to return statements in final form
        //System.out.println("Enter return part!");

        XQueryParser.ReturnClauseContext returnClause_ctx = ctx.returnClause();
        // from:
        // <triplet> {$b1, $b2, $b3} </triplet>
        // convert to:
        // <triplet> {$tuple/b1/*, $tuple/b2/*, $tuple/b3/*} </triplet>

        //second example, original:
        //<book-with-prices>{   $tb,
        //<price-review>{ $a/price }</price-review>,
        //<price>{ $b/price }</price>}</book-with-prices>
        //TO:
        //<book-with-prices>{ $tuple/tb/*,
        //<price-review>{ $tuple/a/*/price }</price-review>,
        //<price>{ $tuple/b/*/price }</price> }</book-with-prices>
        String originalReturn = returnClause_ctx.xq().getText();
        //System.out.println("Original return:" + originalReturn);
        String[] returnParts = originalReturn.split("\\$");
        // result after split:
        // <book-with-prices>{
        // tb,<price-review>{
        // a/price }</price-review>,<price>{
        // b/price}</price>}</book-with-prices>


        //there are three total forms of elements in returnParts:
        //b1,
        //        //<something>{ b2 }</something>,
        //        //b3} </triplet>
        for (int i = 0; i < returnParts.length - 1; i++) {
            returnParts[i] += "$tuple/";
        }

        //Print all returnParts:
        /*
        for (int i = 0; i < returnParts.length; i++) {
            System.out.println(returnParts[i]);
        }
        */


        String returnRefrom = "return " + returnParts[0];

        // results of the for loop above:
        // <book-with-prices>{$tuple
        // loop the parts below:
        // tb,<price-review>{$tuple
        // a/price }</price-review>,<price>{$tuple
        // b/price}</price>}</book-with-prices>
        for (int i = 1; i < returnParts.length; i++) {
//            System.out.println("working on return part: \n" + returnParts[i] );
            String[] cur1 = returnParts[i].split(",", 2);
            String[] cur2 = returnParts[i].split("}", 2);
            String[] cur3 = returnParts[i].split("/", 2);
            String[] working = cur1;

            if (working.length > 1) {
//                System.out.println("case 1 or 2:");
                //two cases:
                //case1: tb,<price-review>{$tuple    -->    tb/*,  <price-review>{$tuple
                //case2: a/price }</price-review>,<price>{$tuple   -->  a/price }</price-review>    <price>{$tuple
                if (returnParts[i].contains("}")) {
                    //case2:
//                    System.out.println("case 2:");
                    working = cur2;
                    //working =   a    price }</price-review>,<price>{$tuple
                }
                working[0] += "/*,";
            } else {
                //two cases of being the last component:
                //1: a/TITLE/text()}</act>
                //2: b3} </triplet>

//                System.out.println("case 3:");
//                String[] temp_chek_slash = cur3[1].split("/");
                if(cur3[1].split("/").length > 2){
                    //there are more "/" after the key
                    //sub case 1:
                    working = cur3;
                    working[0] += "/*/";
                }
                else{
                    working = cur2;
                    working[0] += "/*}";
                }

            }

            returnRefrom += working[0] + working[1];

        }
        returnRefrom += "\n";
        result += returnRefrom;
        System.out.println(result);

        return result;
    }

}