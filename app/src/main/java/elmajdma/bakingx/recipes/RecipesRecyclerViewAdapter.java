package elmajdma.bakingx.recipes;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;
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
      isReceipeSelected(bakingApiModel, recipeViewHolder.imgFavoriteHeart);
      if (!bakingApiModel.getImage().isEmpty()) {
        Picasso.with(context).load(bakingApiModel.getImage()).into(recipeViewHolder.imgRecipeItem);
      } else {
        Picasso.with(context).load(R.drawable.bread).into(recipeViewHolder.imgRecipeItem);
      }
    }
  }
  public interface RecipeAdapterOnClickHandler {
    void onRecipeCardClick(int position, View v);
    void onRecipeHeartClick(int position, View v);
  }

  public class RecipeViewHolder extends RecyclerView.ViewHolder{

    //public class RecipeViewHolder extends RecyclerView.ViewHolder  {
    @BindView(R.id.image_baking_recip_item)
    ImageView imgRecipeItem;
    @BindView(R.id.img_favorite_heart)
    ImageView imgFavoriteHeart;
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

      recipeCard.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.onRecipeCardClick(getAdapterPosition(), v);
        }
      });
      imgFavoriteHeart.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.onRecipeHeartClick(getAdapterPosition(), v);
        }
      });
    }
    }
  private void isReceipeSelected(BakingApiModel recipe, ImageView imageView) {
    Uri uri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI,
        recipe.getId());
    Cursor mCursor = context.getContentResolver().query(
        uri,
        null,
        RecipeEntry.COLUMN_RECIPE_ID,
        new String[]{String.valueOf(recipe.getId())},
        null);
    if (mCursor != null && mCursor.moveToFirst()) {
      imageView.setTag(R.drawable.heart_red);
      Picasso.with(context).load(R.drawable.heart_red).into(imageView);

    } else {
      imageView.setTag(R.drawable.heart_black);
      Picasso.with(context).load(R.drawable.heart_black).into(imageView);
    }
  }
}
