/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 *
 * @author utpl
 */
@JsonIgnoreProperties
public class EntitiesUser implements Serializable{
    private UrlUser url;
    private Description description;

    public EntitiesUser() {
    }
    
    public UrlUser getUrl() {
        return url;
    }

    public void setUrl(UrlUser url) {
        this.url = url;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
    
}
