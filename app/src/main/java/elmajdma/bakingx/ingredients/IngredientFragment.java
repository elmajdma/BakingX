package elmajdma.bakingx.ingredients;


import static elmajdma.bakingx.MainActivity.STEPS_LIST_KEY;
import static elmajdma.bakingx.RecipeDetailsActivity.RECIPE_ID;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Ingredients;
import elmajdma.bakingx.recipes.RecipeViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientFragment extends Fragment {

  private static final String BUNDLE_RECYCLEBR_LAYOUT = "recycler_layout";
  @BindView(R.id.recyclerview_ingredient)
  RecyclerView ingredientRecyclerView;
  private IngredientRecyclerViewAdapter mIngredientRecyclerViewAdapter;
  private Parcelable savedRecyclerLayoutState;
  private RecipeViewModel mRecipeViewModel;
  private List<Ingredients> ingredientList=new ArrayList<>();
  private int itemPosition;
  private int recipeId;

  public IngredientFragment() {
    // Required empty public constructor
  }
  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    //restore recycler view at same position
    if (savedInstanceState != null) {
      savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLEBR_LAYOUT);
      ingredientRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
  }
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(BUNDLE_RECYCLEBR_LAYOUT,
        ingredientRecyclerView.getLayoutManager().onSaveInstanceState());
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
    ButterKnife.bind(this, view);
    setRecyclerview();
    Bundle bundle = getArguments();
    //itemPosition = bundle.getInt(STEPS_LIST_KEY);
    recipeId=bundle.getInt(RECIPE_ID);
    mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    mRecipeViewModel.getAllBakingDetails().observe(getActivity(), this::setIngredientRecyclerView);
    return view;
  }
  private void setRecyclerview() {
    ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    ingredientRecyclerView.setHasFixedSize(true);
  }
  private void setIngredientRecyclerView(List<BakingApiModel> bakingReciepList) {
    //ingredientList = bakingReciepList.get(itemPosition).getIngredients();
    for(BakingApiModel bm:bakingReciepList){
      if(bm.getId()==recipeId){
        ingredientList=bm.getIngredients();
      }
    }


    mIngredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(getContext(),
        ingredientList);
    if (savedRecyclerLayoutState != null) {
      ingredientRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
    ingredientRecyclerView.setAdapter(mIngredientRecyclerViewAdapter);
  }
}
