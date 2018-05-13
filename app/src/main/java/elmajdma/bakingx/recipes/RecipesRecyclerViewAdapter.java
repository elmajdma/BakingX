package elmajdma.bakingx.recipes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.BakingApiModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private RecipeAdapterOnClickHandler mClickHandler;
  private Context context;
  private List<BakingApiModel> bakingRecipesList = new ArrayList<>();

  public RecipesRecyclerViewAdapter(Context context, List<BakingApiModel> bakingRecipesList,
      RecipeAdapterOnClickHandler mClickHandler) {
    this.context = context;
    this.bakingRecipesList = bakingRecipesList;
    this.mClickHandler = mClickHandler;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View recipeView = inflater.inflate(R.layout.recip_item, parent, false);
    return new RecipeViewHolder(recipeView, mClickHandler);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    RecipesRecyclerViewAdapter.RecipeViewHolder recipeViewHolder = (RecipesRecyclerViewAdapter.RecipeViewHolder) holder;
    configureViewHolderRecipes(recipeViewHolder, position);
  }

  @Override
  public int getItemCount() {
    // condition ? passed : failed
    return (bakingRecipesList != null) ? bakingRecipesList.size() : 0;
  }

  @SuppressLint("StaticFieldLeak")
  private void configureViewHolderRecipes(RecipeViewHolder recipeViewHolder, int position) {
    BakingApiModel bakingApiModel = bakingRecipesList.get(position);
    if (bakingApiModel != null) {
      recipeViewHolder.tvRecipeTitle.setText(bakingApiModel.getName());
      recipeViewHolder.tvRecipeServing.setText(String.valueOf(bakingApiModel.getServings()));

      String imageUrl=bakingApiModel.getImage();
            if(imageUrl.isEmpty()){
              Picasso.with(context).load(R.drawable.bread).into(recipeViewHolder.imgRecipeItem);
            }else{
              Picasso.with(context).load(imageUrl).into(recipeViewHolder.imgRecipeItem);
              new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                  Bitmap bitmap = null;
                  String videoPath = "http://techslides.com/demos/sample-videos/small.mp4";
                  MediaMetadataRetriever mediaMetadataRetriever = null;
                  try {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                      mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    bitmap = mediaMetadataRetriever.getFrameAtTime();
                  } catch (Exception e) {
                    e.printStackTrace();

                  } finally {
                    if (mediaMetadataRetriever != null)
                      mediaMetadataRetriever.release();
                  }
                  return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                  super.onPostExecute(bitmap);
                  if (bitmap != null)
                    recipeViewHolder.imgRecipeItem.setImageBitmap(bitmap);
                }
              }.execute();
            }
    }
  }

  /**
   * The interface that receives onClick messages.
   */

  public interface RecipeAdapterOnClickHandler {

    void onRecipeCardClick(int position, View v);

  }

  public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.image_baking_recip_item)
    ImageView imgRecipeItem;
    @BindView(R.id.tv_recip_title)
    TextView tvRecipeTitle;
    @BindView(R.id.tv_serving)
    TextView tvRecipeServing;
    @BindView(R.id.tv_serving_title)
    TextView tvRecipeServingTitle;
    @BindView(R.id.cardview_recipes_item)
    CardView recipeCard;
    private RecipeAdapterOnClickHandler mListener;

    public RecipeViewHolder(View itemView, RecipeAdapterOnClickHandler listener) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mListener = listener;
      recipeCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      mListener.onRecipeCardClick(getAdapterPosition(), v);
    }
  }
}
