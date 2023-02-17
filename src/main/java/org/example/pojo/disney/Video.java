
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Video {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("publishedKey")
    @Expose
    private String publishedKey;
    @SerializedName("link")
    @Expose
    private Link__1 link;
    @SerializedName("section")
    @Expose
    private Section__1 section;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("dateModified")
    @Expose
    private Integer dateModified;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("prerollAdEnabled")
    @Expose
    private Boolean prerollAdEnabled;
    @SerializedName("extID")
    @Expose
    private String extID;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("linkText")
    @Expose
    private String linkText;
    @SerializedName("viewType")
    @Expose
    private String viewType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lastPublishedDate")
    @Expose
    private Integer lastPublishedDate;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("pageTitle")
    @Expose
    private Object pageTitle;
    @SerializedName("length")
    @Expose
    private Integer length;
    @SerializedName("image")
    @Expose
    
    private Image image;
    @SerializedName("social")
    @Expose
    private Social__1 social;
    @SerializedName("editorial")
    @Expose

    private Editorial__1 editorial;
    @SerializedName("m3u8")
    @Expose
    private String m3u8;
    @SerializedName("mp4")
    @Expose
    private String mp4;
    @SerializedName("hqMp4")
    @Expose
    private String hqMp4;
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;
    @SerializedName("redirect")
    @Expose
    private Object redirect;
    @SerializedName("amp")
    @Expose
    private Boolean amp;
    @SerializedName("classification")
    @Expose
    
    private Classification classification;
    @SerializedName("taxonomy")
    @Expose
    
    private Taxonomy taxonomy;
    @SerializedName("meta")
    @Expose
    
    private Meta meta;
    @SerializedName("owner")
    @Expose
    
    private Owner__1 owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPublishedKey() {
        return publishedKey;
    }

    public void setPublishedKey(String publishedKey) {
        this.publishedKey = publishedKey;
    }

    public Link__1 getLink() {
        return link;
    }

    public void setLink(Link__1 link) {
        this.link = link;
    }

    public Section__1 getSection() {
        return section;
    }

    public void setSection(Section__1 section) {
        this.section = section;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getDateModified() {
        return dateModified;
    }

    public void setDateModified(Integer dateModified) {
        this.dateModified = dateModified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrerollAdEnabled() {
        return prerollAdEnabled;
    }

    public void setPrerollAdEnabled(Boolean prerollAdEnabled) {
        this.prerollAdEnabled = prerollAdEnabled;
    }

    public String getExtID() {
        return extID;
    }

    public void setExtID(String extID) {
        this.extID = extID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLastPublishedDate() {
        return lastPublishedDate;
    }

    public void setLastPublishedDate(Integer lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Object getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(Object pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Social__1 getSocial() {
        return social;
    }

    public void setSocial(Social__1 social) {
        this.social = social;
    }

    public Editorial__1 getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial__1 editorial) {
        this.editorial = editorial;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public String getHqMp4() {
        return hqMp4;
    }

    public void setHqMp4(String hqMp4) {
        this.hqMp4 = hqMp4;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Object getRedirect() {
        return redirect;
    }

    public void setRedirect(Object redirect) {
        this.redirect = redirect;
    }

    public Boolean getAmp() {
        return amp;
    }

    public void setAmp(Boolean amp) {
        this.amp = amp;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Owner__1 getOwner() {
        return owner;
    }

    public void setOwner(Owner__1 owner) {
        this.owner = owner;
    }

}
