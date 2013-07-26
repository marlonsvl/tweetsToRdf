/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.pojos;

import java.io.Serializable;

/**
 *
 * @author utpl
 */
public class Triples implements Serializable{

    private String sujeto;
    private String predicado;
    private String objeto;
    public Triples() {
    }

    public String getSujeto() {
        return sujeto;
    }

    public void setSujeto(String sujeto) {
        this.sujeto = sujeto;
    }

    public String getPredicado() {
        return predicado;
    }

    public void setPredicado(String predicado) {
        this.predicado = predicado;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }
    
}
