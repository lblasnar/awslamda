
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("jsonschema2pojo")
public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("publishedKey")
    @Expose
    private String publishedKey;
    @SerializedName("link")
    @Expose
    private Link link;
    @SerializedName("section")
    @Expose
    private Section section;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("dateModified")
    @Expose
    private Integer dateModified;
    @SerializedName("firstPublished")
    @Expose
    private Integer firstPublished;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("linkText")
    @Expose
    private String linkText;
    @SerializedName("pageTitle")
    @Expose
    private String pageTitle;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("calloutText")
    @Expose
    private String calloutText;
    @SerializedName("lastPublishedDate")
    @Expose
    private Integer lastPublishedDate;
    @SerializedName("deckline")
    @Expose
    private String deckline;
    @SerializedName("editorial")
    @Expose
    private Editorial editorial;
    @SerializedName("bodyExist")
    @Expose
    private Boolean bodyExist;
    @SerializedName("displayComments")
    @Expose
    private Boolean displayComments;
    @SerializedName("sponsored")
    @Expose
    private Boolean sponsored;
    @SerializedName("noSyndication")
    @Expose
    private Boolean noSyndication;
    @SerializedName("addToSiteSearch")
    @Expose
    private Boolean addToSiteSearch;
    @SerializedName("social")
    @Expose
    private Social social;
    @SerializedName("disableFeaturedMedia")
    @Expose
    private String disableFeaturedMedia;
    @SerializedName("featuredMedia")
    @Expose
    private FeaturedMedia featuredMedia;
    @SerializedName("imageOverride")
    @Expose
    private ImageOverride imageOverride;
    @SerializedName("redirect")
    @Expose
    private Object redirect;
    @SerializedName("amp")
    @Expose
    private Boolean amp;
    @SerializedName("classification")
    @Expose
    private Classification__1 classification;
    @SerializedName("taxonomy")
    @Expose
    private Taxonomy__1 taxonomy;
    @SerializedName("translatedPost")
    @Expose
    private List<Object> translatedPost;
    @SerializedName("breakingNews")
    @Expose
    private Boolean breakingNews;

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

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
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

    public Integer getFirstPublished() {
        return firstPublished;
    }

    public void setFirstPublished(Integer firstPublished) {
        this.firstPublished = firstPublished;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalloutText() {
        return calloutText;
    }

    public void setCalloutText(String calloutText) {
        this.calloutText = calloutText;
    }

    public Integer getLastPublishedDate() {
        return lastPublishedDate;
    }

    public void setLastPublishedDate(Integer lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }

    public String getDeckline() {
        return deckline;
    }

    public void setDeckline(String deckline) {
        this.deckline = deckline;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Boolean getBodyExist() {
        return bodyExist;
    }

    public void setBodyExist(Boolean bodyExist) {
        this.bodyExist = bodyExist;
    }

    public Boolean getDisplayComments() {
        return displayComments;
    }

    public void setDisplayComments(Boolean displayComments) {
        this.displayComments = displayComments;
    }

    public Boolean getSponsored() {
        return sponsored;
    }

    public void setSponsored(Boolean sponsored) {
        this.sponsored = sponsored;
    }

    public Boolean getNoSyndication() {
        return noSyndication;
    }

    public void setNoSyndication(Boolean noSyndication) {
        this.noSyndication = noSyndication;
    }

    public Boolean getAddToSiteSearch() {
        return addToSiteSearch;
    }

    public void setAddToSiteSearch(Boolean addToSiteSearch) {
        this.addToSiteSearch = addToSiteSearch;
    }

    public Social getSocial() {
        return social;
    }

    public void setSocial(Social social) {
        this.social = social;
    }

    public String getDisableFeaturedMedia() {
        return disableFeaturedMedia;
    }

    public void setDisableFeaturedMedia(String disableFeaturedMedia) {
        this.disableFeaturedMedia = disableFeaturedMedia;
    }

    public FeaturedMedia getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(FeaturedMedia featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public ImageOverride getImageOverride() {
        return imageOverride;
    }

    public void setImageOverride(ImageOverride imageOverride) {
        this.imageOverride = imageOverride;
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

    public Classification__1 getClassification() {
        return classification;
    }

    public void setClassification(Classification__1 classification) {
        this.classification = classification;
    }

    public Taxonomy__1 getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy__1 taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List<Object> getTranslatedPost() {
        return translatedPost;
    }

    public void setTranslatedPost(List<Object> translatedPost) {
        this.translatedPost = translatedPost;
    }

    public Boolean getBreakingNews() {
        return breakingNews;
    }

    public void setBreakingNews(Boolean breakingNews) {
        this.breakingNews = breakingNews;
    }

}
