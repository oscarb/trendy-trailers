package se.oscarb.trendytrailers.model;

import org.parceler.Parcel;

@Parcel
public class Video {
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }
}
