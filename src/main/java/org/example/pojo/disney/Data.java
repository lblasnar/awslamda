package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class Data {

    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("publishedKey")
    @Expose
    String publishedKey;
    @SerializedName("link")
    @Expose
    Link link;
    @SerializedName("section")
    @Expose
    Section section;
    @SerializedName("date")
    @Expose
    Integer date;
    @SerializedName("dateModified")
    @Expose
    Integer dateModified;
    @SerializedName("firstPublished")
    @Expose
    Integer firstPublished;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("linkText")
    @Expose
    String linkText;
    @SerializedName("pageTitle")
    @Expose
    String pageTitle;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("calloutText")
    @Expose
    String calloutText;
    @SerializedName("lastPublishedDate")
    @Expose
    Integer lastPublishedDate;
    @SerializedName("deckline")
    @Expose
    String deckline;
    @SerializedName("editorial")
    @Expose
    Editorial editorial;
    @SerializedName("bodyExist")
    @Expose
    Boolean bodyExist;
    @SerializedName("displayComments")
    @Expose
    Boolean displayComments;
    @SerializedName("sponsored")
    @Expose
    Boolean sponsored;
    @SerializedName("noSyndication")
    @Expose
    Boolean noSyndication;
    @SerializedName("addToSiteSearch")
    @Expose
    Boolean addToSiteSearch;
    @SerializedName("social")
    @Expose
    Social social;
    @SerializedName("disableFeaturedMedia")
    @Expose
    String disableFeaturedMedia;
    @SerializedName("featuredMedia")
    @Expose
    FeaturedMedia featuredMedia;
    @SerializedName("imageOverride")
    @Expose
    ImageOverride imageOverride;
    @SerializedName("redirect")
    @Expose
    Object redirect;
    @SerializedName("amp")
    @Expose
    Boolean amp;
    @SerializedName("classification")
    @Expose
    ClassificationData classification;
    @SerializedName("taxonomy")
    @Expose
    TaxonomyData taxonomy;
    @SerializedName("translatedPost")
    @Expose
    List<Object> translatedPost;
    @SerializedName("breakingNews")
    @Expose
    Boolean breakingNews;
}
