package codepath.kaughlinpractice.fridgefone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.kaughlinpractice.fridgefone.DetailsAdapter;
import codepath.kaughlinpractice.fridgefone.FridgeClient;
import codepath.kaughlinpractice.fridgefone.GlideApp;
import codepath.kaughlinpractice.fridgefone.R;
import cz.msebera.android.httpclient.Header;


public class DetailsFragment extends Fragment {


    @BindView(R.id.ivRecipeImage) public ImageView mRecipeImageView;
    @BindView(R.id.ivFavoriteStar) public ImageView mFavoriteStarImageView;
    @BindView(R.id.tvDishTitle) public TextView mDishTitleTextView;
    @BindView(R.id.rvDetails) public RecyclerView mDetailsRecyclerView;

    public FridgeClient mClient;
    private boolean mIsFavorited;

    ArrayList<String> mInstructionsList;
    Collection<String> mIngredientsSet;
    DetailsAdapter mDetailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize the client
        mClient = new FridgeClient(getActivity());

        mInstructionsList = new ArrayList<>();
        mIngredientsSet = new HashSet<>();
        mIsFavorited = false;

        Bundle args = getArguments();
        String name = args.getString("name");
        int id = args.getInt("id");
        String image = args.getString("image");
        final ArrayList<String> ingredients = args.getStringArrayList("ingredients");
        mDishTitleTextView.setText(name);

        GlideApp.with(getActivity())
                .load(image)
                .fitCenter()
                .into(mRecipeImageView);

        mFavoriteStarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsFavorited = !mIsFavorited;
                if (mIsFavorited) {
                    mFavoriteStarImageView.setImageResource(R.drawable.white_star_filled);
                    Toast.makeText(getActivity(),mDishTitleTextView.getText().toString() + " added to recipe favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    mFavoriteStarImageView.setImageResource(R.drawable.white_star_outline);
                }
            }
        });


        if (FridgeClient.mUseInstructionsAPI) {

            // execute a GET request expecting a JSON object response
            mClient.getInstructions(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i += 1) {
                            JSONObject partOfInstructions = response.getJSONObject(i);
                            parseInstructions(partOfInstructions);
                        }

                        ArrayList<String> details = new ArrayList<>();
                        details.add(getString(R.string.ingredients));
                        for (String ingredient: ingredients) {
                            details.add(ingredient);
                        }
                        details.add(getString(R.string.instructions));

                        for(String step: mInstructionsList) {
                            details.add(step);
                        }

                        mDetailsAdapter = new DetailsAdapter(details, ingredients.size());
                        // RecyclerView setup (layout manager, use adapter)
                        mDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mDetailsRecyclerView.setAdapter(mDetailsAdapter);
                    } catch (JSONException e) {
                        Log.d("DetailFragment", "Error: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DetailFragment", "Error: " + throwable);
                }
            });
        }
        else {
            // with step breakdown
            String responseString = "[{\"name\":\"\",\"steps\":[{\"number\":1,\"step\":\"Beat the egg in a bowl, add a pinch of salt and the flour and pour in the extra virgin olive oil and the beer: if needed, add a tablespoon of water to make a smooth but not too liquid batter. It is supposed to cover the apples, not to slide off!Peel the apples, core them paying attention not to break them and cut the apples into horizontal slices, 1 cm thick.\",\"ingredients\":[{\"id\":1034053,\"name\":\"extra virgin olive oil\",\"image\":\"olive-oil.jpg\"},{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"},{\"id\":20081,\"name\":\"all purpose flour\",\"image\":\"flour.png\"},{\"id\":14003,\"name\":\"beer\",\"image\":\"beer.jpg\"},{\"id\":2047,\"name\":\"salt\",\"image\":\"salt.jpg\"},{\"id\":1123,\"name\":\"egg\",\"image\":\"egg.jpg\"}],\"equipment\":[{\"id\":404783,\"name\":\"bowl\",\"image\":\"bowl.jpg\"}]},{\"number\":2,\"step\":\"Heat the olive oil in a large frying pan. The right moment to fry the apples is when the oil starts to smoke, as grandma says. Dip the apple slices into the batter and deep fry them until cooked through and golden on both sides.\",\"ingredients\":[{\"id\":4053,\"name\":\"olive oil\",\"image\":\"olive-oil.jpg\"},{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"}],\"equipment\":[{\"id\":404645,\"name\":\"frying pan\",\"image\":\"pan.png\"}]},{\"number\":3,\"step\":\"Transfer the apples into a plate lined with a paper towel. Sprinkle the fritters with icing sugar and serve them warm.\",\"ingredients\":[{\"id\":9003,\"name\":\"apple\",\"image\":\"apple.jpg\"}],\"equipment\":[{\"id\":405895,\"name\":\"paper towels\",\"image\":\"paper-towels.jpg\"}]}]}]";
            JSONArray response = null;
            try {
                response = new JSONArray(responseString);
            } catch (JSONException e) {
                Log.d("DetailFragment", "Error: " + e.getMessage());
            }
            try {
                for (int i = 0; i < response.length(); i += 1) {
                    JSONObject partOfInstructions = response.getJSONObject(i);
                    parseInstructions(partOfInstructions);
                }
                ArrayList<String> details = new ArrayList<>();
                details.add(getString(R.string.ingredients));

                for (String ingredient: ingredients) {
                    details.add(ingredient);
                }
                details.add(getString(R.string.instructions));
                for(String step: mInstructionsList) {
                    details.add(step);
                }

                mDetailsAdapter = new DetailsAdapter(details, ingredients.size());
                // RecyclerView setup (layout manager, use adapter)
                mDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mDetailsRecyclerView.setAdapter(mDetailsAdapter);
            } catch (JSONException e) {
                Log.d("DetailFragment", "Error " + e.getMessage());
            }
        }

    }

    public void parseInstructions(JSONObject partOfInstructions) {
        try {
            JSONArray steps = partOfInstructions.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i += 1) {
                JSONObject step = steps.getJSONObject(i);
                String stepDetails = step.getString("step");
                mInstructionsList.add(stepDetails);
            }
        } catch (JSONException e) {
            Log.d("DetailFragment", "Error " + e.getMessage());
        }
    }


}