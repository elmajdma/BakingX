package elmajdma.bakingx.steps;


import static elmajdma.bakingx.MainActivity.STEPS_LIST_KEY;
import static elmajdma.bakingx.RecipeDetailsActivity.RECIPE_ID;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.recipes.RecipeViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements View.OnClickListener {

  private static final String BUNDLE_RECYCLEBR_LAYOUT = "recycler_layout";
  @BindView(R.id.bt_ingredient_list)
  Button btOpenIngredientList;
  @BindView(R.id.recyclerview_steps)
  RecyclerView stepsRecyclerView;
  private Parcelable savedRecyclerLayoutState;
  private int itemPosition;
  private int recipeId;
  private List<Steps> stepsList=new ArrayList<>();
  private StepsRecyclerViewAdapter mStepsRecyclerViewAdapter;
  private RecipeViewModel mRecipeViewModel;
  private OnStepsFragmentListener mStepPositionCallback;

  public StepsFragment() {
    // Required empty public constructor
  }
  public interface OnStepsFragmentListener {
    void setVideoPositionStepDescripition(int videoToplay);
    void setIngredientFragmentList();


  }
  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLEBR_LAYOUT);
      stepsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
  }
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(BUNDLE_RECYCLEBR_LAYOUT,
        stepsRecyclerView.getLayoutManager().onSaveInstanceState());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_steps, container, false);
    ButterKnife.bind(this, view);
    setRecyclerview();
    Bundle bundle = getArguments();
    //itemPosition = bundle.getInt(STEPS_LIST_KEY);
    recipeId=bundle.getInt(RECIPE_ID);
    btOpenIngredientList.setOnClickListener(this);
    mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    mRecipeViewModel.getAllBakingDetails().observe(getActivity(), this::setStepsRecyclerView);
    return view;
  }
  private void setRecyclerview() {
    stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    stepsRecyclerView.setHasFixedSize(true);
  }
  private void setStepsRecyclerView(List<BakingApiModel> bakingList ) {
    //stepsList=bakingList.get(itemPosition).getSteps();
    for(BakingApiModel bm:bakingList){
      if(bm.getId()==recipeId){
        stepsList=bm.getSteps();
      }
    }

    StepsRecyclerViewAdapter.StepsAdapterOnClickHandler mListener= (position, v) -> {
      mStepPositionCallback.setVideoPositionStepDescripition(position);

    };
    mStepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(getContext(), stepsList,mListener );
    if (savedRecyclerLayoutState != null) {
      stepsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
    stepsRecyclerView.setAdapter(mStepsRecyclerViewAdapter);

  }
  @Override
  public void onClick(View v) {
    mStepPositionCallback.setIngredientFragmentList();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnStepsFragmentListener) {
      mStepPositionCallback = (OnStepsFragmentListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnStepsFragmentListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mStepPositionCallback = null;
  }
}
