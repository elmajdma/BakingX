package elmajdma.bakingx.recipes;


import static elmajdma.bakingx.MainActivity.GRID_LAYOUT_KEY;
import static elmajdma.bakingx.MainActivity.LAYOUT_KEY;
import static elmajdma.bakingx.MainActivity.LINER_LAYOUT_KEY;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import elmajdma.bakingx.CalcFittedColumn;
import elmajdma.bakingx.R;
import elmajdma.bakingx.RecipeDetailsActivity;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Ingredients;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;
import elmajdma.bakingx.recipes.RecipesRecyclerViewAdapter.RecipeAdapterOnClickHandler;
import elmajdma.bakingx.recipewidget.RecipeWidgetService;
import elmajdma.bakingx.steps.StepsFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipFragment extends Fragment {

  public static final String RECIPES_LIST_KEY = "recipes_key";
  public static final String INGREDIENT_LIST_KEY = "ingredient_key";
  public static final String STEPS_LIST_KEY = "steps_key";
  public static final String POSITION_KEY = "position_key";
  private static final String BUNDLE_RECYCLEBR_LAYOUT = "recycler_layout";
  public static final String RECIPE_ID= "reipeId";
  @BindView(R.id.recyclerview_recipes)
  RecyclerView recipeRecyclerView;
  List<BakingApiModel> bakingRecipList;
  private RecipesRecyclerViewAdapter mRecipesRecyclerViewAdapter;
  private Parcelable savedRecyclerLayoutState;
  private RecipeViewModel mRecipeViewModel;

  public RecipFragment() {
    // Required empty public constructor
  }

  public static boolean isTablet(Context context) {
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK)
        >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLEBR_LAYOUT);
      recipeRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(BUNDLE_RECYCLEBR_LAYOUT,
        recipeRecyclerView.getLayoutManager().onSaveInstanceState());
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    mRecipeViewModel.getAllBakingDetails()
        .observe(getActivity(), new Observer<List<BakingApiModel>>() {
          @Override
          public void onChanged(@Nullable List<BakingApiModel> bakingApiModels) {
            setDataforRecipesRecyclerView(bakingApiModels);
          }
        });
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recip, container, false);
    ButterKnife.bind(this, view);
    Bundle bundle = getArguments();
    recipesRecyclerView(bundle.getInt(LAYOUT_KEY));
    return view;
  }
  private void setDataforRecipesRecyclerView(List<BakingApiModel> bakingReciepList) {
    RecipesRecyclerViewAdapter.RecipeAdapterOnClickHandler mListener=new RecipeAdapterOnClickHandler() {
      @Override
      public void onRecipeCardClick(int position, View v) {
        //startDetailedRecipeActivity(position);
        startDetailedRecipeActivity(bakingReciepList.get(position).getId());
      }
      @Override
      public void onRecipeHeartClick(int position, View v) {
        try {
          setRecipeFavoritelist(bakingReciepList.get(position),v);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
    mRecipesRecyclerViewAdapter = new RecipesRecyclerViewAdapter(getContext(), bakingReciepList,
        mListener);
    recipeRecyclerView.setAdapter(mRecipesRecyclerViewAdapter);
  }
  private void recipesRecyclerView(int layoutKey) {
    switch (layoutKey) {
      case GRID_LAYOUT_KEY:
        int mNoOfColumns = CalcFittedColumn.calculateNoOfColumns(getActivity());
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));
        recipeRecyclerView.setHasFixedSize(true);
        break;
      case LINER_LAYOUT_KEY:
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipeRecyclerView.setHasFixedSize(true);
    }
  }
  private void startDetailedRecipeActivity(int stepList) {
    Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
    //intent.putExtra(STEPS_LIST_KEY, stepList);
    intent.putExtra(RECIPE_ID, stepList);
    startActivity(intent);
  }
  private void setRecipeFavoritelist(BakingApiModel recipe, View view) throws IOException {
    switch ((Integer) view.getTag()) {
      case (R.drawable.heart_black):
        view.setTag(R.drawable.heart_red);
        Picasso.with(getContext()).load(R.drawable.heart_red).into((ImageView) view);
        insertRecipeintoFavoirte(recipe);
        break;
      case (R.drawable.heart_red):
        view.setTag(R.drawable.heart_black);
        Picasso.with(getContext()).load(R.drawable.heart_black).into((ImageView) view);
        deleteRecipeFromFavoriteList(recipe);
    }
  }

  private void insertRecipeintoFavoirte(BakingApiModel recipe) throws IOException {
    ContentValues contentValues = new ContentValues();
    contentValues.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
    contentValues.put(RecipeEntry.COLUMN_RECIPE_TITLE, recipe.getName());
    contentValues.put(RecipeEntry.COLUMN_SERVING, recipe.getServings());
    contentValues.put(RecipeEntry.COLUMN_RECIPE_INGREDIENT,
        ingredientListToString(recipe.getIngredients()));
    Uri uri = getActivity().getContentResolver().insert(RecipeEntry.CONTENT_URI, contentValues);
    if (uri != null) {
      Toast.makeText(getContext(), "Recipe saved in Favorite List", Toast.LENGTH_LONG).show();
      RecipeWidgetService.startActionUpdateRecipeWidgets(getContext());
    } else {
      Toast.makeText(getContext(), "An Error Occurred While Saving Recipe !!", Toast.LENGTH_LONG).show();
    }
  }

  private String ingredientListToString(List<Ingredients> listItem) {
List<String> ingredientList=new ArrayList<>();
    for (int i = 0; i < listItem.size(); i++) {
      String ingredient = listItem.get(i).getIngredient() + " " +
          String.valueOf(listItem.get(i).getQuantity()) + " " +
          listItem.get(i).getMeasure();
      ingredientList.add(ingredient);
    }
    return ingredientList.toString();
  }
  private void deleteRecipeFromFavoriteList(BakingApiModel recipe) {
    Uri uri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI,
       recipe.getId() );
    int deltedNum = getActivity().getContentResolver().delete(
        uri,
        RecipeEntry.COLUMN_RECIPE_ID,
        new String[]{String.valueOf(recipe.getId())}
    );
    if (deltedNum > -1) {
      Toast.makeText(getContext(), "Recipe Removed From Favorite List", Toast.LENGTH_LONG).show();
      RecipeWidgetService.startActionUpdateRecipeWidgets(getContext());
    } else {
      Toast.makeText(getContext(), "An Error Occurred While Remove Recipe !!", Toast.LENGTH_LONG).show();
    }
  }
}
