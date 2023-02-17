
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Image {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("link")
    @Expose
    private Link__2 link;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("dynamicSource")
    @Expose
    private String dynamicSource;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("pano360")
    @Expose
    private Boolean pano360;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("imageWarning")
    @Expose
    private Boolean imageWarning;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Link__2 getLink() {
        return link;
    }

    public void setLink(Link__2 link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDynamicSource() {
        return dynamicSource;
    }

    public void setDynamicSource(String dynamicSource) {
        this.dynamicSource = dynamicSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getPano360() {
        return pano360;
    }

    public void setPano360(Boolean pano360) {
        this.pano360 = pano360;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getImageWarning() {
        return imageWarning;
    }

    public void setImageWarning(Boolean imageWarning) {
        this.imageWarning = imageWarning;
    }

}
