package org.virtuoso.jena;



//package virtuoso.jena.driver;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.*;

public class VirtuosoSPARQLExample8 {

	/**
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 */
    public static void main(String[] args) {
        String x = "INSERT INTO GRAPH <http://www.utpl.edu.ec/tweetsvoc> { <http://www.utpl.edu.ec/tweets/353166897069563904> <http://www.utpl.edu.ec/tweets/text> 'Djokovic fav destination is Thailand \n" +
"just as that slippery massage \n" +
"He slides so well \n" +
"#Wimbledon' . }";
        System.out.println(x.trim());

/*			STEP 1			*/
        VirtGraph set = new VirtGraph ("jdbc:virtuoso://localhost:1111/charset=UTF-8/", "dba", "dba");

/*			STEP 2			*/
        System.out.println("\nexecute: CLEAR GRAPH <http://test1>");
        String str = "CLEAR GRAPH <http://test1>";
        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();                  

        System.out.println("\nexecute: INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc*' . <aa1> <bb1> 123. }");
        str = "INSERT INTO GRAPH <http://test1> { <aa> <bb> 'cc' . <aa1> <bb1> <abc.com>. }";
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();     
        
        str = "INSERT INTO GRAPH <http://www.utpl.edu.ec/tweetsvoc> { <http://www.utpl.edu.ec/tweets/28054906> <http://www.utpl.edu.ec/tweets/description> 'A Tweet could either RTed or ★ed in different TL    \\'s till it is Trolled." +
" Picture says thousand words,Twitter limits to 140 characters" +
" http://t.co/XCxc6RkQqo ' . }";
        
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();     

/*			STEP 3			*/
/*		Select all data in virtuoso	*/
        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
        Query sparql = QueryFactory.create("SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");

/*			STEP 4			*/
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);

        ResultSet results = vqe.execSelect();
        while (results.hasNext()) {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            System.out.println(" { " + s + " " + p + " " + o + " . }");
        }


        System.out.println("\nexecute: DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }");
        str = "DELETE FROM GRAPH <http://test1> { <aa> <bb> 'cc' }";
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();                  

        System.out.println("\nexecute: SELECT * FROM <http://test1> WHERE { ?s ?p ?o }");
        vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
        results = vqe.execSelect();
        while (results.hasNext()) {
            QuerySolution rs = results.nextSolution();
            RDFNode s = rs.get("s");
            RDFNode p = rs.get("p");
            RDFNode o = rs.get("o");
            System.out.println(" { " + s + " " + p + " " + o + " . }");
        }	
    }
}