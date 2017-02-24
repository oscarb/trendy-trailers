package se.oscarb.trendytrailers.detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.databinding.ItemReviewBinding;
import se.oscarb.trendytrailers.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviewList;

    public ReviewsAdapter() {
        reviewList = Collections.emptyList();
    }

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Review> getReviews() {
        return reviewList;
    }

    public void setReviews(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_review, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        holder.bindReview(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        ViewHolder(ItemReviewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }

        private void bindReview(Review review) {
            if (binding.getReviewViewModel() == null) {
                binding.setReviewViewModel(new ItemReviewViewModel(review));
            } else {
                binding.getReviewViewModel().setReview(review);
            }
        }
    }
}
