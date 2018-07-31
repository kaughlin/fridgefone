package codepath.kaughlinpractice.fridgefone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import codepath.kaughlinpractice.fridgefone.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<Item> mItems;
    private Context mContext;
    Singleton mSingleInstance;

    public ItemAdapter(List<Item> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        mSingleInstance = Singleton.getSingletonInstance();
    }


    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.mFoodNameTextView.setText(mItems.get(position).getName());
//        viewHolder.mSelectCheckImageView.setVisibility(View.INVISIBLE);

        GlideApp.with(mContext)
                .load(mItems.get(position).getImageURL())
                .into(viewHolder.mFoodImageView);

        // check if all are selected
        if (mSingleInstance.ismAllSelected()) {
            viewHolder.mSelectCheckImageView.setVisibility(View.VISIBLE);
            viewHolder.itemView.setAlpha(.85f); // changes opacity of image once clicked
        }
        else if(!mSingleInstance.ismAllSelected()) {
            viewHolder.mSelectCheckImageView.setVisibility(View.INVISIBLE);
            viewHolder.itemView.setAlpha(1f); // changes opacity of image once clicked
        }


        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // when a user long clicks on an item, it calls the MainActivity's delete method which handles deletion

                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the recipe at the position, this won't work if the class is static
                    Item item = mItems.get(position);
                    // open up a pop up and send in food_name to ask if they specifically want to delete THIS item

                    ((MainActivity) mContext).askToDeleteItem(item);
                }
                return true;
            }
        });


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when a user long clicks on an item, it calls the MainActivity's delete method which handles deletion
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION && mSingleInstance.ismSelectItemsBoolean()) {


                    String item_name = viewHolder.mFoodNameTextView.getText().toString();

                    // check if the hash set contains this item if so change the opacity back to regular
                    if (mSingleInstance.getmSelectedNamesSet().contains(item_name)) {
                        mSingleInstance.getmSelectedNamesSet().remove(item_name);
                        viewHolder.mSelectCheckImageView.setVisibility(View.INVISIBLE);
                        view.setAlpha(1f); // changes opacity of image once clicked //TODO  change to dimen later
                        //view.setAlpha(R.dimen.not_selected);
                    }

                    else {
                        mSingleInstance.getmSelectedNamesSet().add(item_name);
                        viewHolder.mSelectCheckImageView.setVisibility(View.VISIBLE);
                        view.setAlpha(.85f); // changes opacity of image once clicked //TODO  change to dimen later
                        //view.setAlpha(R.dimen.selected_view); // changes opacity of image once clicked
                    }
                    Log.d("ItemAdapter", "Selected Items in Fridge Hashset: " + mSingleInstance.getmSelectedNamesSet());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mFoodNameTextView;
        public ImageView mFoodImageView;
        public ImageView mSelectCheckImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id.tvFood_Name);
            mFoodImageView = (ImageView) itemView.findViewById(R.id.ivFood_Image);
            mSelectCheckImageView = (ImageView) itemView.findViewById(R.id.ivSelectCheck);

        }
    }
    // Clean all elements of the recycler
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }
}
