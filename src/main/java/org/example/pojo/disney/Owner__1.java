
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Owner__1 {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("link")
    @Expose
    private Link__3 link;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Link__3 getLink() {
        return link;
    }

    public void setLink(Link__3 link) {
        this.link = link;
    }

}
