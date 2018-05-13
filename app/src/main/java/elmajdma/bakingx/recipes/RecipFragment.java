package elmajdma.bakingx.recipes;


import static elmajdma.bakingx.MainActivity.GRID_LAYOUT_KEY;
import static elmajdma.bakingx.MainActivity.LAYOUT_KEY;
import static elmajdma.bakingx.MainActivity.LINER_LAYOUT_KEY;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.CalcFittedColumn;
import elmajdma.bakingx.R;
import elmajdma.bakingx.RecipeDetailsActivity;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.steps.StepsFragment;
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
    RecipesRecyclerViewAdapter.RecipeAdapterOnClickHandler mListener = (position, v) -> {
      startDetailedRecipeActivity(position);
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
    intent.putExtra(STEPS_LIST_KEY, stepList);
    startActivity(intent);
  }
}
