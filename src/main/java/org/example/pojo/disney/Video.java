
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Video {

    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("publishedKey")
    @Expose
    String publishedKey;
    @SerializedName("link")
    @Expose
    Link__1 link;
    @SerializedName("section")
    @Expose
    Section__1 section;
    @SerializedName("date")
    @Expose
    Integer date;
    @SerializedName("dateModified")
    @Expose
    Integer dateModified;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("prerollAdEnabled")
    @Expose
    Boolean prerollAdEnabled;
    @SerializedName("extID")
    @Expose
    String extID;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("linkText")
    @Expose
    String linkText;
    @SerializedName("viewType")
    @Expose
    String viewType;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("lastPublishedDate")
    @Expose
    Integer lastPublishedDate;
    @SerializedName("caption")
    @Expose
    String caption;
    @SerializedName("pageTitle")
    @Expose
    Object pageTitle;
    @SerializedName("length")
    @Expose
    Integer length;
    @SerializedName("image")
    @Expose
    Image image;
    @SerializedName("social")
    @Expose
    Social__1 social;
    @SerializedName("editorial")
    @Expose

    Editorial__1 editorial;
    @SerializedName("m3u8")
    @Expose
    String m3u8;
    @SerializedName("mp4")
    @Expose
    String mp4;
    @SerializedName("hqMp4")
    @Expose
    String hqMp4;
    @SerializedName("sponsored")
    @Expose
    Boolean sponsored;
    @SerializedName("redirect")
    @Expose
    Object redirect;
    @SerializedName("amp")
    @Expose
    Boolean amp;
    @SerializedName("classification")
    @Expose
    Classification classification;
    @SerializedName("taxonomy")
    @Expose
    Taxonomy taxonomy;
    @SerializedName("meta")
    @Expose
    Meta meta;
    @SerializedName("owner")
    @Expose
    Owner__1 owner;
}
