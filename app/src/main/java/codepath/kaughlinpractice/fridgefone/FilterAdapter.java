package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import codepath.kaughlinpractice.fridgefone.model.Recipe;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>{
    private ArrayList<String> mFilters;
    private HashMap<String, Boolean> mUserDict;
    public boolean isFilterSelected;
    private FilterInterface mFilterInterface;
    private Singleton mSingleInstance;
    private Context mContext;

    public interface FilterInterface {
        void regenerateRecipes();
    }

    public FilterAdapter(Context context, ArrayList<String> mFilters, FilterInterface filterInstance){
        this.mFilters = mFilters;
        this.mSingleInstance = Singleton.getSingletonInstance();
        this.mUserDict = mSingleInstance.getmUserDict();
        this.mFilterInterface = filterInstance;
        this.mContext = context;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View filterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_filter_item, parent, false);
        FilterViewHolder filterViewHolder = new FilterViewHolder(filterView);
        return filterViewHolder;
    }

    @Override
    public void onBindViewHolder(final FilterViewHolder holder, final int position) {
        holder.mFilterTextView.setText(mFilters.get(position));

        isFilterSelected = mUserDict.get(Recipe.recipe_traits[position]);
        if (isFilterSelected) {
            holder.mFilterTextView.setBackgroundColor(mContext.getResources().getColor(R.color.filter_gray));
        } else {
            holder.mFilterTextView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        holder.mFilterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilterSelected = mUserDict.get(Recipe.recipe_traits[position]);
                mUserDict.put(Recipe.recipe_traits[position], !isFilterSelected);
                mFilterInterface.regenerateRecipes();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {

        TextView mFilterTextView;

        public FilterViewHolder(View view) {
            super(view);
            mFilterTextView = view.findViewById(R.id.FilterTextView);
        }
    }
}
