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
public class Entities implements Serializable{
    private HashTag[] hashtags;
    private Url[] urls;
    private UserMention[] user_mentions;
    private Symbols[] symbols;
    private Media[] media;

    public Entities() {
    }
    
    public HashTag[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(HashTag[] hashtags) {
        this.hashtags = hashtags;
    }

    public Url[] getUrls() {
        return urls;
    }

    public void setUrls(Url[] urls) {
        this.urls = urls;
    }

    public UserMention[] getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(UserMention[] user_mentions) {
        this.user_mentions = user_mentions;
    }

    public Symbols[] getSymbols() {
        return symbols;
    }

    public void setSymbols(Symbols[] symbols) {
        this.symbols = symbols;
    }

    public Media[] getMedia() {
        return media;
    }

    public void setMedia(Media[] media) {
        this.media = media;
    }
    
    
}
