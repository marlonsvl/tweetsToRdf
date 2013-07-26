/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.pojos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

/**
 *
 * @author utpl
 */
@ManagedBean(name = "pJ")
@ApplicationScoped
public class PruebaJackson {

    /**
     * @param args the command line arguments
     */
    
    
    private static  VirtuosoUpdateRequest vur;
    private static VirtGraph set;
    private static final String graph = "http://www.utpl.edu.ec/tweetsvoc";
    private static final String prefix = "http://www.utpl.edu.ec/tweets/";
    
    public static Connection getConnection(){
        Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcutf8 = "&useUnicode=true&characterEncoding=UTF-8";
            conexion = DriverManager.getConnection("jdbc:mysql://lord.utpl.edu.ec:3306/crawler?user=crawler&password=utpl2011"+jdbcutf8);
            
        } catch (ClassNotFoundException ex) {
            System.out.println("error conexion1 :"+ex.getCause());
        } catch (SQLException ex){
            System.out.println("error conexion2 :"+ex.getCause());
        }
        return conexion;

    }
    
    public static void insertarTriple1(String sujeto, String predicado, String objeto){
        Connection c = getConnection();
        Statement st = null;
        String saux = sujeto.replace("'", "\\'").replace('"', '\"');
        String paux = predicado.replace("'", "\\'").replace('"', '\"');
        String oaux = objeto.replace("'", "\\'").replace('"', '\"');
        if(c != null){
            try {
                String query;
                query = "insert into crawler.triples(sujeto, predicado, objeto) values('"+saux+"', '"+paux+"', '"+oaux+"');";
                st = c.createStatement();
                st.executeUpdate(query);
                System.out.println("triple insertado correctamente");
            } catch (SQLException ex) {
                System.err.println("error en la conexion y conteo: "+ex.getMessage());
            }finally{
                try {
                    if(st != null) st.close();
                    if(c != null) c.close();
                } catch (SQLException ex) {
                    System.out.println("error cerrando..."+ex.getCause());
                }
            }
        }
    }
    
    
    public static void insertarTriple(String sujeto, String predicado, String objeto, boolean isSujeto){
        String saux = sujeto.replace("'", "\\'").replace('"', '\"');
        String paux = predicado.replace("'", "\\'").replace('"', '\"');
        String oaux = "";
        Double doaux = null;
        Integer ioaux;
        String str = "";
        if(isSujeto){
            str = "INSERT INTO GRAPH <"+graph+"> { <"+prefix+saux+"> <"+prefix+paux+"> <"+objeto+"> . }";
        }else{
            
            try{
                doaux = Double.parseDouble(objeto);
            }catch(Exception ex){
                doaux = null;
            }
            
            try{
                ioaux = Integer.parseInt(objeto);
            }catch(Exception ex){
                ioaux = null;
            }
            
            if(doaux != null){
                str = "INSERT INTO GRAPH <"+graph+"> { <"+prefix+saux+"> <"+prefix+paux+"> <"+doaux+"> . }";
            }else if(ioaux != null){
                str = "INSERT INTO GRAPH <"+graph+"> { <"+prefix+saux+"> <"+prefix+paux+"> <"+ioaux+"> . }";
            }else{
                oaux = objeto.replace("\\", "\\\\");
                oaux = oaux.replace("'", " \\'");
                //oaux = oaux.replace('"', '\"');
                //oaux = oaux.replace('{', ' ');
                //oaux = oaux.replace('}', ' ');
                //oaux = oaux.replace('(', ' ');
                //oaux = oaux.replace(')', ' ');
                oaux = oaux.replaceAll("[\r\n]+", " ");
                str = "INSERT INTO GRAPH <"+graph+"> { <"+prefix+saux+"> <"+prefix+paux+"> '"+oaux+"' . }";
                
            }
            
        }
        try{
            str = str.trim();
            vur = VirtuosoUpdateFactory.create(str, set);
            vur.exec();
            //System.out.println("triple saved successfully");
        }catch(Exception ex){
            System.err.println("Error insertando: "+str);
            
        }
        
    }
    
    public static List<Triples> getTriples(){
        Connection c = getConnection();
        Statement st = null;
        ResultSet rs = null;
        int numero = 0;
        List<Triples> lista = new ArrayList<Triples>();
        if(c != null){
            try {
                String query;
                query = "SELECT * FROM crawler.triples limit 100 ";
                st = c.createStatement();
                rs = st.executeQuery(query);
                while(rs.next()){
                    Triples triple = new Triples();
                    triple.setSujeto(rs.getString(1));
                    triple.setPredicado(rs.getString(2));
                    triple.setSujeto(rs.getString(3));
                    lista.add(triple);
                }
                
            } catch (SQLException ex) {
                System.err.println("error en la conexion y conteo: "+ex.getMessage());
            }finally{
                try {
                    if(rs != null) rs.close();
                    if(st != null) st.close();
                    if(c != null) c.close();
                } catch (SQLException ex) {
                    System.out.println("error cerrando..."+ex.getCause());
                }
            }
        }
        return lista;
    }
    
    public static List<String> getJsons(){
        Connection c = getConnection();
        Statement st = null;
        ResultSet rs = null;
        int numero = 0;
        List<String> lista = new ArrayList<String>();
        if(c != null){
            try {
                String query;
                query = "SELECT json FROM crawler.tweets2 where json is not null limit 100000 ";
                st = c.createStatement();
                rs = st.executeQuery(query);
                while(rs.next()){
                    lista.add(rs.getString(1));
                }
                
            } catch (SQLException ex) {
                System.err.println("error en la conexion y conteo: "+ex.getMessage());
            }finally{
                try {
                    if(rs != null) rs.close();
                    if(st != null) st.close();
                    if(c != null) c.close();
                } catch (SQLException ex) {
                    System.out.println("error cerrando..."+ex.getCause());
                }
            }
        }
        return lista;
    }
    
//    public static void main(String[] args) {
//        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//        String string = "";
//        
//        try {
//            List<String> lista = getJsons();
//            System.out.println(lista.size());
//            for (int i = 0; i < lista.size(); i++) {
//                string = lista.get(i);
//                //System.out.println(string);
//                Tweet t = mapper.readValue(string, Tweet.class);
//                triplesTweet(t);
//            }
//            
//        } catch (IOException ex) {
//            System.out.println(string);
//            ex.printStackTrace();
//        }
//    }
    
    public String iniciar(){
        set = new VirtGraph ("jdbc:virtuoso://localhost:1111/charset=UTF-8/", "dba", "dba");
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        String string = "";
        System.out.println("\nexecute: CLEAR GRAPH <http://www.utpl.edu.ec/tweets>");
        String str = "CLEAR GRAPH <"+graph+">";
        vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();       
        
        try {
            List<String> lista = getJsons();
            System.out.println(lista.size());
            for (int i = 0; i < lista.size(); i++) {
                string = lista.get(i);
                //System.out.println(string);
                Tweet t = mapper.readValue(string, Tweet.class);
                triplesTweet(t);
            }
            
        } catch (IOException ex) {
            System.out.println(string);
            ex.printStackTrace();
        }
        return "";
    }
    
//    public static void main(String[] args) {
//        set = new VirtGraph ("jdbc:virtuoso://localhost:1111/charset=UTF-8/", "dba", "dba");
//        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//        String string = "";
//        System.out.println("\nexecute: CLEAR GRAPH <http://www.utpl.edu.ec/tweets>");
//        String str = "CLEAR GRAPH <"+graph+">";
//        vur = VirtuosoUpdateFactory.create(str, set);
//        vur.exec();       
//        
//        try {
//            List<String> lista = getJsons();
//            System.out.println(lista.size());
//            for (int i = 0; i < lista.size(); i++) {
//                string = lista.get(i);
//                //System.out.println(string);
//                Tweet t = mapper.readValue(string, Tweet.class);
//                triplesTweet(t);
//            }
//            
//        } catch (IOException ex) {
//            System.out.println(string);
//            ex.printStackTrace();
//        }
//        
////        System.out.println("\nexecute: SELECT * FROM <http://www.utpl.edu.ec/tweetvoc> WHERE { ?s ?p ?o }");
////        Query sparql = QueryFactory.create("SELECT * FROM <"+graph+"> WHERE { ?s <http://www.utpl.edu.ec/tweets/text> ?o ."
////                + "}");
////        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
////        com.hp.hpl.jena.query.ResultSet results = vqe.execSelect();
////        while (results.hasNext()) {
////            QuerySolution rs = results.nextSolution();
////            RDFNode s = rs.get("s");
////            RDFNode p = rs.get("p");
////            RDFNode o = rs.get("o");
////            System.out.println(" { " + s + " " + p + " " + o + " . }");
////        }
//    }
    
    
    public static Integer getAleatorio(){
        Integer aux = (int)Math.rint(Math.random()*1000000+1);
        return aux;
    }
    
    public static void triplesTweet(Tweet t){
        if(t.getFilter_level() != null)insertarTriple(t.getId().toString(), "created_at", t.getCreated_at(),false);
        if(t.getFavorite_count() != null) insertarTriple(t.getId().toString(), "favorite_count", t.getFavorite_count().toString(),false);
        if(t.getFavorited() != null) insertarTriple(t.getId().toString(), "favorited", t.getFavorited().toString(),false);
        if(t.getFilter_level() != null) insertarTriple(t.getId().toString(), "filter_level", t.getFilter_level().toString(),false);
        if(t.getId_str() != null) insertarTriple(t.getId().toString(), "id_str", t.getId_str().toString(),false);
        if(t.getIn_reply_to_screen_name() != null) insertarTriple(t.getId().toString(), "in_reply_to_screen_name", t.getIn_reply_to_screen_name().toString(),false);
        if(t.getIn_reply_to_status_id() != null) insertarTriple(t.getId().toString(), "in_reply_to_status_id", prefix+t.getIn_reply_to_status_id().toString(),true);
        if(t.getIn_reply_to_status_id_str() != null) insertarTriple(t.getId().toString(), "in_reply_to_status_id_str", t.getIn_reply_to_status_id_str().toString(),false);
        if(t.getIn_reply_to_user_id() != null) insertarTriple(t.getId().toString(), "in_reply_to_user_id", prefix+t.getIn_reply_to_user_id().toString(),true);
        if(t.getIn_reply_to_user_id_str() != null) insertarTriple(t.getId().toString(), "in_reply_to_user_id_str", t.getIn_reply_to_user_id_str().toString(),false);
        if(t.getLang() != null) insertarTriple(t.getId().toString(), "lang", t.getLang().toString(),false);
        if(t.getPossibly_sensitive() != null) insertarTriple(t.getId().toString(), "possibly_sensitive", t.getPossibly_sensitive().toString(),false);
        if(t.getRetweet_count() != null) insertarTriple(t.getId().toString(), "retweet_count", t.getRetweet_count().toString(),false);
        if(t.getRetweeted() != null) insertarTriple(t.getId().toString(), "retweeted", t.getRetweeted().toString(),false);
        if(t.getSource() != null) insertarTriple(t.getId().toString(), "source", t.getSource().toString(),false);
        if(t.getText() != null) insertarTriple(t.getId().toString(), "text", t.getText().toString(),false);
        if(t.getTruncated() != null) insertarTriple(t.getId().toString(), "truncated", t.getTruncated().toString(),false);
        if(t.getWithheld_copyright() != null) insertarTriple(t.getId().toString(), "withheld_copyright", t.getWithheld_copyright().toString(),false);
        
        if(t.getWithheld_in_countries() != null){
            String[] wic = t.getWithheld_in_countries();
            for (int i = 0; i < wic.length; i++) {
                String string = wic[i];
                Integer aux = getAleatorio();
                insertarTriple(aux.toString(), "withheld_in_countries", string,false);
                insertarTriple(t.getId_str(), "hasWithheldInCountries", prefix+aux.toString(),true);
            }
        }
        if(t.getWithheld_scope() != null) insertarTriple(t.getId().toString(), "withheld_scope", t.getWithheld_scope().toString(),false);
        if(t.getMetadata() != null){
            Integer aux = getAleatorio();
            if(t.getMetadata().getResult_type() != null) insertarTriple(aux.toString(), "result_type", t.getMetadata().getResult_type(),false);
            if(t.getMetadata().getIso_language_code() != null) insertarTriple(aux.toString(), "iso_language_code", t.getMetadata().getIso_language_code(),false);
            insertarTriple(t.getId().toString(), "hasMetadata", prefix+aux.toString(),true);
            
        }
        if(t.getEntities() != null) triplesEntities(t.getEntities(), t.getId_str());
        if(t.getCoordinates() != null){
            Integer aux = getAleatorio();
            if(t.getCoordinates().getType() != null) insertarTriple(aux.toString(), "type", t.getCoordinates().getType(),false);
            Float [] c = t.getCoordinates().getCoordinates();
            insertarTriple(aux.toString(), "longitude", c[0].toString(),false);
            insertarTriple(aux.toString(), "latitude", c[1].toString(),false);
            insertarTriple(t.getId_str(), "hasCoordinates", prefix+aux.toString(),true);
        }
        if(t.getCurrent_user_retweet() != null) insertarTriple(t.getId().toString(), "current_user_retweet", prefix+t.getCurrent_user_retweet().getId_str(),true);
        if(t.getPlace() != null) triplesPlace(t.getPlace(), t.getId_str());
        if(t.getScopes() != null) insertarTriple(t.getId().toString(), "followers_scopes", t.getScopes().getFollowers().toString(),false);
        if(t.getContributors() != null){
            Contributor[] cs = t.getContributors();
            for (int i = 0; i < cs.length; i++) {
                Contributor contributor = cs[i];
                insertarTriple(contributor.getId_str(), "screen_name", contributor.getScreen_name(),false);
                insertarTriple(contributor.getId_str(), "isContributor", prefix+t.getId_str(),true);
            }
        }
        
        if(t.getUser() != null){
            insertarTriple(t.getId().toString(), "isPostBy", prefix+t.getUser().getId_str(),true);
            insertarUser(t.getUser());
            if(t.getUser().getEntities() != null){
                triplesEntitiesUser(t.getUser().getEntities());
            }

            if(t.getUser().getStatus() != null){
                triplesTweet(t.getUser().getStatus());
            }
        }
        if(t.getRetweeted_status() != null){
            triplesTweet(t.getRetweeted_status());
        }
    }
    
    public static void triplesPlace(Place p, String idtweet){
        if(p.getCountry() != null) insertarTriple(p.getId(), "country", p.getCountry(),false);
        if(p.getCountry_code() != null) insertarTriple(p.getId(), "country_code", p.getCountry_code(),false);
        if(p.getFull_name() != null) insertarTriple(p.getId(), "full_name", p.getFull_name(),false);
        if(p.getPlace_type() != null) insertarTriple(p.getId(), "place_type", p.getPlace_type(),false);
        if(p.getUrl() != null) insertarTriple(p.getId(), "url", p.getUrl(),false);
        if(p.getName() != null) insertarTriple(p.getId(), "name", p.getName(),false);
        if(p.getAttributes().getId() != null){
            if(p.getAttributes().getStreet_address() != null) insertarTriple(p.getAttributes().getId(), "street_address", p.getAttributes().getStreet_address(),false);
            if(p.getAttributes().getTwitter() != null) insertarTriple(p.getAttributes().getId(), "twitter", p.getAttributes().getTwitter(),false);
            insertarTriple(p.getId(), "hasAttributes", prefix+p.getAttributes().getId(),true);
        }
        if(p.getBounding_box() != null){
            Float [][][] coord = p.getBounding_box().getCoordinates();
            Integer bb = getAleatorio();
            insertarTriple(bb.toString(), "positionType", p.getBounding_box().getType(),false);
            for (int i = 0; i < coord.length; i++) {
                Float[][] floatses = coord[i];
                for (int j = 0; j < floatses.length; j++) {
                    Float[] floats = floatses[j];
                    Integer aux = getAleatorio();
                    for (int k = 0; k < floats.length; k++) {
                        Float float1 = floats[k];
                        if(k == 0 || k%2 == 0)
                            insertarTriple(aux.toString(), "latitude", float1.toString(),false);
                        else
                            insertarTriple(aux.toString(), "latitude", float1.toString(),false);
                    }
                    insertarTriple(bb.toString(), "coordinates", prefix+aux.toString(),true);
                }
            }
            insertarTriple(p.getId(), "hasBoundingBox", prefix+bb.toString(),true);
        }
        insertarTriple(idtweet, "place", p.getId(),true);
    }
    
    
    
    public static void triplesEntities(Entities e, String idtweet){
        if(e.getHashtags() != null) triplesHashTags(e.getHashtags(), idtweet);
        if(e.getSymbols() != null) triplesSymbols(e.getSymbols(), idtweet);
        if(e.getUrls() != null) triplesUrlsTweet(e.getUrls(), idtweet);
        if(e.getUser_mentions() != null) triplesUserMentions(e.getUser_mentions(), idtweet);
        if(e.getMedia() != null) triplesMedia(e.getMedia(), idtweet);
    }
    
    public static void triplesMedia(Media[] m, String idtweet){
        for (int i = 0; i < m.length; i++) {
            Media media = m[i];
            if(media.getDisplay_url() != null) insertarTriple(media.getId_str(), "display_url", media.getDisplay_url(),false);
            if(media.getExpanded_url() != null) insertarTriple(media.getId_str(), "expanded_url", media.getExpanded_url(),false);
            if(media.getMedia_url() != null) insertarTriple(media.getId_str(), "media_url", media.getMedia_url(),false);
            if(media.getMedia_url_https() != null) insertarTriple(media.getId_str(), "media_url_https", media.getMedia_url_https(),false);
            if(media.getSource_status_id() != null) insertarTriple(media.getId_str(), "source_status_id", media.getSource_status_id_str(),false);
            if(media.getType() != null) insertarTriple(media.getId_str(), "type", media.getType(),false);
            if(media.getUrl() != null) insertarTriple(media.getId_str(), "url", media.getUrl(),false);
            if(media.getIndices() != null){
                Integer [] inds = media.getIndices();
                for (int j = 0; j < inds.length; j++) {
                    Integer integer = inds[j];
                    insertarTriple(media.getId().toString(), "indice", integer.toString(),false);
                }
            }
            if(media.getSizes() != null){
                if(media.getSizes().getLarge() != null){
                    Size large = media.getSizes().getLarge();
                    Integer aux = getAleatorio();
                    if(large.getH() != null) insertarTriple(aux.toString(), "h", large.getH().toString(),false);
                    if(large.getW() != null) insertarTriple(aux.toString(), "w", large.getW().toString(),false);
                    if(large.getResize() != null) insertarTriple(aux.toString(), "resize", large.getResize(),false);
                    insertarTriple(aux.toString(), "type", "large",false);
                    insertarTriple(media.getId_str(), "hasSize", prefix+aux.toString(),true);
                }
                if(media.getSizes().getMedium() != null){
                    Size medium = media.getSizes().getMedium();
                    Integer aux = getAleatorio();
                    if(medium.getH() != null) insertarTriple(aux.toString(), "h", medium.getH().toString(),false);
                    if(medium.getW() != null) insertarTriple(aux.toString(), "w", medium.getW().toString(),false);
                    if(medium.getResize() != null) insertarTriple(aux.toString(), "resize", medium.getResize(),false);
                    insertarTriple(aux.toString(), "type", "medium",false);
                    insertarTriple(media.getId_str(), "hasSize", prefix+aux.toString(),true);
                }
                if(media.getSizes().getSmall() != null){
                    Size small = media.getSizes().getLarge();
                    Integer aux = getAleatorio();
                    if(small.getH() != null) insertarTriple(aux.toString(), "h", small.getH().toString(),false);
                    if(small.getW() != null) insertarTriple(aux.toString(), "w", small.getW().toString(),false);
                    if(small.getResize() != null) insertarTriple(aux.toString(), "resize", small.getResize(),false);
                    insertarTriple(aux.toString(), "type", "small",false);
                    insertarTriple(media.getId_str(), "hasSize", prefix+aux.toString(),true);
                }
                if(media.getSizes().getThumb() != null){
                    Size thumb = media.getSizes().getThumb();
                    Integer aux = getAleatorio();
                    if(thumb.getH() != null) insertarTriple(aux.toString(), "h", thumb.getH().toString(),false);
                    if(thumb.getW() != null) insertarTriple(aux.toString(), "w", thumb.getW().toString(),false);
                    if(thumb.getResize() != null) insertarTriple(aux.toString(), "resize", thumb.getResize(),false);
                    insertarTriple(aux.toString(), "type", "thumb",false);
                    insertarTriple(media.getId_str(), "hasSize", prefix+aux.toString(),true);
                }
            }
            insertarTriple(idtweet, "hasMedia", prefix+media.getId_str(),true);
        }
        
    }
    
    public static void triplesHashTags(HashTag[] h, String idTweet){
        for (int i = 0; i < h.length; i++) {
            HashTag hashTag = h[i];
            Integer aux = getAleatorio();
            insertarTriple(aux.toString(), "text", hashTag.getText(), false);
            //System.out.println(aux + " text: "+ hashTag.getText());
            Integer [] inds = hashTag.getIndices();
            for (int j = 0; j < inds.length; j++) {
                Integer integer = inds[j];
                insertarTriple(aux.toString(), "indice", integer.toString(),false);
            }
            insertarTriple(idTweet, "hasHashTag", prefix+aux.toString(),true);
        }
    }
    
    public static void triplesSymbols(Symbols[] s, String idTweet){
        for (int i = 0; i < s.length; i++) {
            Symbols symbol = s[i];
            Integer aux = getAleatorio();
            insertarTriple(aux.toString(), "text", symbol.getText(),false);
            Integer [] inds = symbol.getIndices();
            for (int j = 0; j < inds.length; j++) {
                Integer integer = inds[j];
                insertarTriple(aux.toString(), "indice", integer.toString(),false);
            }
            insertarTriple(idTweet, "hasSymbol", prefix+aux.toString(),true);
        }
    }
    
    public static void triplesUrlsTweet(Url[] u, String idTweet){
        for (int i = 0; i < u.length; i++) {
            Url url = u[i];
            Integer aux = getAleatorio();
            insertarTriple(aux.toString(), "display_url", url.getDisplay_url(),false);
            insertarTriple(aux.toString(), "expanded_url", url.getExpanded_url(),false);
            insertarTriple(aux.toString(), "url", url.getUrl(),false);
            Integer [] inds = url.getIndices();
            for (int j = 0; j < inds.length; j++) {
                Integer integer = inds[j];
                insertarTriple(aux.toString(), "indice", integer.toString(),false);
            }
            insertarTriple(idTweet, "hasUrl", prefix+aux.toString(),true);
        }
    }
    public static void triplesUserMentions(UserMention [] um, String idTweet){
        for (int i = 0; i < um.length; i++) {
            UserMention userMention = um[i];
            insertarTriple(userMention.getId_str(), "id_str", userMention.getId_str(),false);
            insertarTriple(userMention.getId_str(), "name", userMention.getName(),false);
            insertarTriple(userMention.getId_str(), "screen_name", userMention.getScreen_name(),false);
            Integer [] inds = userMention.getIndices();
            for (int j = 0; j < inds.length; j++) {
                Integer integer = inds[j];
                insertarTriple(userMention.getId_str(), "indice", integer.toString(),false);
            }
            insertarTriple(idTweet, "mentioned", prefix+userMention.getId_str(),true);
        }
    }
    
    public static void triplesUrl(Url url){
        Integer aux = getAleatorio();
        if(url.getDisplay_url() != null) insertarTriple(aux.toString(), "display_url", url.getDisplay_url(),false);
        if(url.getExpanded_url() != null) insertarTriple(aux.toString(), "expanded_url", url.getExpanded_url(),false);
        if(url.getUrl() != null) insertarTriple(aux.toString(), "url", url.getUrl(),false);
        if(url.getIndices()!= null){
            for (int i = 0; i < url.getIndices().length; i++) {
                Integer ind = url.getIndices()[i];
                insertarTriple(aux.toString(), "indice", ind.toString(),false);
            }
        }
    }
    
    public static void triplesUrlUser(UrlUser uu){
        
        if(uu.getUrls() != null){
            Url[] us = uu.getUrls();
            for (int i = 0; i < us.length; i++) {
                Url url = us[i];
                triplesUrl(url);
                //System.out.println("ingresa url user");
            }
        }
    }
    public static void triplesDescription(Description d){
        if(d.getUrls() != null){
            UrlUser[] us = d.getUrls();
            for (int i = 0; i < us.length; i++) {
                UrlUser urlUser = us[i];
                triplesUrlUser(urlUser);
            }
        }
    }
    
    public static void triplesEntitiesUser(EntitiesUser eu){
        if(eu.getDescription() != null) triplesDescription(eu.getDescription());
        if(eu.getUrl() != null) triplesUrlUser(eu.getUrl());
    }
    
    public static void insertarUser(TweetUser tu){
        
        if(tu.getContributors_enabled() != null) insertarTriple(tu.getId().toString(), "contributors_enabled", tu.getContributors_enabled().toString(),false);
        if(tu.getCreated_at() != null) insertarTriple(tu.getId().toString(), "created_at", tu.getCreated_at().toString(),false);
        if(tu.getDefault_profile() != null) insertarTriple(tu.getId().toString(), "default_profile", tu.getDefault_profile().toString(),false);
        if(tu.getDefault_profile_image() != null) insertarTriple(tu.getId().toString(), "default_profile_image", tu.getDefault_profile_image().toString(),false);
        if(tu.getDescription() != null) insertarTriple(tu.getId().toString(), "description", tu.getDescription().toString(),false);
        if(tu.getFavourites_count() != null) insertarTriple(tu.getId().toString(), "favourites_count", tu.getFavourites_count().toString(),false);
        if(tu.getFollow_request_sent() != null) insertarTriple(tu.getId().toString(), "follow_request_sent", tu.getFollow_request_sent().toString(),false);
        if(tu.getFollowers_count() != null) insertarTriple(tu.getId().toString(), "followers_count", tu.getFollowers_count().toString(),false);
        if(tu.getFriends_count() != null) insertarTriple(tu.getId().toString(), "friends_count", tu.getFriends_count().toString(),false);
        if(tu.getGeo_enabled() != null) insertarTriple(tu.getId().toString(), "geo_enabled", tu.getGeo_enabled().toString(),false);
        if(tu.getId_str() != null) insertarTriple(tu.getId().toString(), "id_str", tu.getId_str().toString(),false);
        if(tu.getIs_translator() != null) insertarTriple(tu.getId().toString(), "is_translator", tu.getIs_translator().toString(),false);
        if(tu.getLang() != null) insertarTriple(tu.getId().toString(), "lang", tu.getLang().toString(),false);
        if(tu.getListed_count() != null) insertarTriple(tu.getId().toString(), "listed_count", tu.getListed_count().toString(),false);
        if(tu.getLocation() != null) insertarTriple(tu.getId().toString(), "location", tu.getLocation().toString(),false);
        if(tu.getName() != null) insertarTriple(tu.getId().toString(), "name", tu.getName().toString(),false);
        if(tu.getNotifications() != null) insertarTriple(tu.getId().toString(), "notifications", tu.getNotifications().toString(),false);
        if(tu.getProfile_background_color() != null) insertarTriple(tu.getId().toString(), "profile_background_color", tu.getProfile_background_color().toString(),false);
        if(tu.getProfile_background_image_url() != null) insertarTriple(tu.getId().toString(), "profile_background_image_url", tu.getProfile_background_image_url().toString(),false);
        if(tu.getProfile_background_image_url_https() != null) insertarTriple(tu.getId().toString(), "profile_background_image_url_https", tu.getProfile_background_image_url_https().toString(),false);
        if(tu.getProfile_background_tile() != null) insertarTriple(tu.getId().toString(), "profile_background_tile", tu.getProfile_background_tile().toString(),false);
        if(tu.getProfile_banner_url() != null) insertarTriple(tu.getId().toString(), "profile_banner_url", tu.getProfile_banner_url().toString(),false);
        if(tu.getProfile_image_url() != null) insertarTriple(tu.getId().toString(), "profile_image_url", tu.getProfile_image_url().toString(),false);
        if(tu.getProfile_link_color() != null) insertarTriple(tu.getId().toString(), "profile_link_color", tu.getProfile_link_color().toString(),false);
        if(tu.getProfile_sidebar_border_color() != null) insertarTriple(tu.getId().toString(), "profile_sidebar_border_color", tu.getProfile_sidebar_border_color().toString(),false);
        if(tu.getProfile_sidebar_fill_color() != null) insertarTriple(tu.getId().toString(), "profile_sidebar_fill_color", tu.getProfile_sidebar_fill_color().toString(),false);
        if(tu.getProfile_text_color() != null) insertarTriple(tu.getId().toString(), "profile_text_color", tu.getProfile_text_color().toString(),false);
        if(tu.getProfile_use_background_image() != null) insertarTriple(tu.getId().toString(), "profile_use_background_image", tu.getProfile_use_background_image().toString(),false);
        if(tu.getProtected_() != null) insertarTriple(tu.getId().toString(), "protected", tu.getProtected_().toString(),false);
        if(tu.getScreen_name() != null) insertarTriple(tu.getId().toString(), "screen_name", tu.getScreen_name().toString(),false);
        if(tu.getShow_all_inline_media() != null) insertarTriple(tu.getId().toString(), "show_all_inline_media", tu.getShow_all_inline_media().toString(),false);
        if(tu.getStatuses_count() != null) insertarTriple(tu.getId().toString(), "statuses_count", tu.getStatuses_count().toString(),false);
        if(tu.getTime_zone() != null) insertarTriple(tu.getId().toString(), "time_zone", tu.getTime_zone().toString(),false);
        if(tu.getUrl() != null) insertarTriple(tu.getId().toString(), "url", tu.getUrl().toString(),false);
        if(tu.getUtc_offset() != null) insertarTriple(tu.getId().toString(), "utc_offset", tu.getUtc_offset().toString(),false);
        if(tu.getVerified() != null) insertarTriple(tu.getId().toString(), "verified", tu.getVerified().toString(),false);
        if(tu.getWithheld_in_countries() != null) insertarTriple(tu.getId().toString(), "withheld_in_countries", tu.getWithheld_in_countries().toString(),false);
        if(tu.getWithheld_scope() != null) insertarTriple(tu.getId().toString(), "withheld_in_scope", tu.getWithheld_scope().toString(),false);
        if(tu.getFollowing() != null) insertarTriple(tu.getId().toString(), "following", tu.getFollowing().toString(),false);
        
    }
    
}
