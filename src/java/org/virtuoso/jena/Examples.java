/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.virtuoso.jena;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

/**
 *
 * @author utpl
 */
public class Examples {
    
    private static final String graph = "http://www.utpl.edu.ec/tweetsvoc";
    private static final String prefix = "http://www.utpl.edu.ec/tweets/";
    
    public static void main(String [] args){
        VirtGraph set = new VirtGraph ("jdbc:virtuoso://localhost:1111/charset=UTF-8/", "dba", "dba");
        Query sparql = QueryFactory.create("SELECT ?name ?oo FROM <"+graph+"> WHERE { ?s  <http://www.utpl.edu.ec/tweets/text> ?o . "
                + "filter regex(?o, 'snowden'). ?tweet <http://www.utpl.edu.ec/tweets/hasHashTag> ?s . ?tweet <http://www.utpl.edu.ec/tweets/isPostBy> ?oo ."
                + " ?oo <http://www.utpl.edu.ec/tweets/screen_name> ?name}");
        //Query sparql = QueryFactory.create("SELECT distinct * FROM <"+graph+"> WHERE { <http://www.utpl.edu.ec/tweets/482913> ?p ?o . }");
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
        ResultSet results = vqe.execSelect();
        while (results.hasNext()) {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("name");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("oo");
            System.out.println(" { " + s + " " + p + " " + o + " . }"); 
            //System.out.println(" { " + p +  " " + o + " }");
        }
        
    }
}
