package se.oscarb.trendytrailers.explore;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.databinding.ItemPosterBinding;
import se.oscarb.trendytrailers.model.Movie;

public class MoviePostersAdapter extends RecyclerView.Adapter<MoviePostersAdapter.ViewHolder> {

    private List<Movie> movieList;

    public MoviePostersAdapter() {
        movieList = Collections.emptyList();
    }

    public MoviePostersAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public MoviePostersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPosterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_poster, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MoviePostersAdapter.ViewHolder holder, int position) {
        holder.bindMovie(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPosterBinding binding;


        public ViewHolder(ItemPosterBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }

        private void bindMovie(Movie movie) {
            if (binding.getPosterViewModel() == null) {
                binding.setPosterViewModel(new ItemPosterViewModel(itemView.getContext(), movie));
            } else {
                binding.getPosterViewModel().setMovie(movie);
            }
        }
    }
}
