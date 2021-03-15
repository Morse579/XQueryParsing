import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Optimized {


    public String inputRewrite(XQueryParser.XqFLWRContext ctx){
        String output = "";

        //find the total number of forClause in the overall input
        int numFor = ctx.forClause().var().size();

        List<HashSet<String>> classify = new LinkedList<>();
        List<String> relation = new LinkedList<>();
        for(int i = 0; i < numFor;i++) {
            String key = ctx.forClause().var(i).getText();
            String xq = ctx.forClause().xq(i).getText();
            String parent = xq.split("/")[0];

            int size = classify.size();
            boolean found = false;

            // construct the classification
            for(int j = 0; j < size; j++) {
                HashSet<String> curSet = classify.get(j);
                if(curSet.contains(parent)) {
                    curSet.add(key);
                    found = true;
                    break;
                }
            }

            //this key is not under other key, create a new rewritten for clause
            if(!found) {
                HashSet<String> newSet = new HashSet<>();
                newSet.add(key);
                classify.add(newSet);
                relation.add(key);
            }
        }
        if(classify.size() == 1) {
            System.out.println("No need to join!");
            return "";
        }





        // --------where-------
        //seperately evaluete each condition in where clause, split will remove "and"
        String[] where = ctx.whereClause().cond().getText().split("and");
        //create a new empty matrix of string storing conditions
        String[][] cond = new String[where.length][2];

        //for each where clause, split nodes from "eq" and store into the matrix created above
        for(int i = 0; i < where.length;i++) {
            cond[i][0] = where[i].split("eq|=")[0];
            cond[i][1] = where[i].split("eq|=")[1];
        }

        int[][] relaWhere = new int[cond.length][2];

        //classify this where clause belongs to which trunk of for group
        for(int i=0; i < cond.length; i++) {
            String cur0 = cond[i][0];
            String cur1 = cond[i][1];
            //initiate as -1
            relaWhere[i][0] = -1;
            relaWhere[i][1] = -1;

            for(int j = 0; j < classify.size();j++) {
                if(classify.get(j).contains(cur0)) {
                    relaWhere[i][0] = j;
                }
                if(classify.get(j).contains(cur1)) {
                    relaWhere[i][1] = j;
                }
            }
        }

        int class_size = classify.size();
        //print out
        output += "for $tuple in";
        for (int i = 1; i < class_size;i++) {
            output += " join (";
        }

        output = PrintJoin(classify, ctx, output, cond, relaWhere);
        if(output == "") {
            return "";
        }

        //------construct the return part------

        // <triplet> {$b1, $b2, $b3} </triplet>
        String retClause = ctx.returnClause().xq().getText();

        //split by dollar sign
        // <triplet> {    b1,      b2,       b3} </triplet>
        String[] tempRet = retClause.split("\\$");

        //construct the basic return format for each "for"
        // <triplet> {$tuple/    b1,$tuple/      b2,$tuple/       b3} </triplet>
        for (int i = 0; i < tempRet.length-1; i++) {
            tempRet[i] = tempRet[i]+"$tuple/";
        }

        //<triplet> {$tuple/
        retClause  = tempRet[0];


        //e.g. tempRet[1] == b1,$tuple/
        //tempRet[2] == b2,$tuple/
        //tempRet[3] == b3} </triplet>
        for (int i = 1; i < tempRet.length; i++) {
            //b1    $tuple/
            //b2    $tuple/
            //b3} </triplet>
            String[] cur1 = tempRet[i].split(",",2);
            //b1,$tuple/
            //b2,$tuple/
            //b3    </triplet>
            String[] cur2 = tempRet[i].split("}",2);
            //b1,$tuple
            //b2,$tuple
            //b3} <       triplet>
            String[] cur3 = tempRet[i].split("/",2);

            //b1    $tuple/
            //b2    $tuple/
            //b3} </triplet>
            String[] cur = cur1;
            if(cur2[0].length() < cur[0].length()) {
                //b1,$tuple/
                //b2,$tuple/
                cur = cur2;
            }
            if(cur3[0].length() < cur[0].length()) {
                //b1,$tuple
                //b2,$tuple
                cur = cur3;
            }

            //b1,$tuple/*
            //b2,$tuple/*
            //b3} </triplet>/*
            tempRet[i] = cur[0] + "/*";

            //cur = cur3 = b1,$tuple
            //cur = cur1 = b3} </triplet>
            if(cur == cur1) {
                //b3} </triplet>/*,
                tempRet[i] += ",";
            }else if(cur == cur2) {
                tempRet[i] += "}";
            }else {
                //b1,$tuple/*/
                //b2,$tuple/*/
                tempRet[i] += "/";
            }

            //b1,$tuple/*/
            //b2,$tuple/*/
            //b3} </triplet>/*
            tempRet[i] += cur[1];

            //<triplet> {$tuple/b1,$tuple/*/
            //<triplet> {$tuple/b1,$tuple/*/b2,$tuple/*/
            //<triplet> {$tuple/b1,$tuple/*/b2,$tuple/*/b3} </triplet>/*
            retClause = retClause + tempRet[i];
        }

        output += "return\n";
        output += retClause+"\n";

        return output;

    }




    //output = PrintJoin(classify, ctx, output, cond, relaWhere);
    private String PrintJoin(List<HashSet<String>> classify, XQueryParser.FLWRContext ctx, String output,String[][] cond,int[][] relaWhere) {
        //print for clause
        int numFor = ctx.forClause().var().size();

        for(int i = 0; i < classify.size(); i++) {
            HashSet<String> curSet = classify.get(i);

            //tuples == the string after return
            String tuples = "";
            //
            int count = 0;

            //print for
            for(int k = 0; k < numFor; k++) {
                String key = ctx.forClause().var(k).getText();
                if(curSet.contains(key)){

                    //if it's the first for clause, add "for" keyword
                    if(count == 0) {
                        String tmpp = ctx.forClause().xq(k).getText();
                        String[] sl = tmpp.split("return",2);

                        //if sl contains the key word "return"
                        if(sl.length == 2) {
                            tmpp = sl[0] + " return " + sl[1];
                        }
                        output += "for " + key + " in " + tmpp;

                        count++;
                    }else {
                        output += ",\n";
                        output += "                   " + key + " in " + ctx.forClause().xq(k).getText();

                    }


                    if(tuples.equals("")) {
                        tuples = tuples + " <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }else {
                        tuples = tuples + ", <" + key.substring(1) + "> " + " {" + key + "} " + " </" + key.substring(1) + ">";
                    }
                }
            }
            output += "\n";

            //print where
            for(int j = 0;j < cond.length;j++) {
                int count1 = 0;
                if(relaWhere[j][1] == -1 && curSet.contains(cond[j][0])) {
                    if(count1 == 0){
                        count1++;
                        output += "where " + cond[j][0] + " eq " + cond[j][1] +"\n";
                    }else {
                        output += " and  " + cond[j][0] + " eq " + cond[j][1] + "\n";
                    }
                }
            }



            //print return
            tuples = "<tuple> {"+tuples+"} </tuple>,";
            output += "                  return " + tuples + "\n";


            // return cond
            if(i > 0) {
                LinkedList<String> ret0 = new LinkedList<>();
                LinkedList<String> ret1 = new LinkedList<>();

                for (int ii = 0; ii < cond.length; ii++) {
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






    private String PrintJoinCond(LinkedList<String> ret0, LinkedList<String> ret1, String output) {
        //if the return value is empty
        output += "                 [";
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
        output += "]  ";
        return output;
    }

}