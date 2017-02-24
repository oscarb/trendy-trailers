package se.oscarb.trendytrailers.model;


import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

@Parcel
public class VideoListing {
    private List<Video> results;

    public List<Video> getResults() {
        if (results == null) {
            return Collections.emptyList();
        }
        return results;
    }
}
