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
class Description implements Serializable{
    private UrlUser[] urls;
    
    public Description() {
    }

    public UrlUser[] getUrls() {
        return urls;
    }

    public void setUrls(UrlUser[] urls) {
        this.urls = urls;
    }
    
}
