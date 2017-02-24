package se.oscarb.trendytrailers.model;

import org.parceler.Parcel;

/*
 * A review of a Movie
 */
@Parcel
public class Review {
    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
