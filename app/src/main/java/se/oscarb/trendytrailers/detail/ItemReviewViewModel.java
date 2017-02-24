package se.oscarb.trendytrailers.detail;

import android.databinding.BaseObservable;

import se.oscarb.trendytrailers.model.Review;
import se.oscarb.trendytrailers.model.ViewModel;

public class ItemReviewViewModel extends BaseObservable implements ViewModel {

    private Review review;

    public ItemReviewViewModel(Review review) {
        this.review = review;
    }

    public String getAuthor() {
        return review.getAuthor();
    }

    public String getContent() {
        return review.getContent();
    }

    public void setReview(Review review) {
        this.review = review;
        notifyChange();
    }

    @Override
    public void destroy() {
        // Empty for now
    }
}
