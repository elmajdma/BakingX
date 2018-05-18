package elmajdma.bakingx;

import static elmajdma.bakingx.recipes.RecipFragment.STEPS_LIST_KEY;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.ingredients.IngredientFragment;
import elmajdma.bakingx.recipes.RecipeViewModel;
import elmajdma.bakingx.recipes.RecipesRecyclerViewAdapter;
import elmajdma.bakingx.steps.StepsFragment;
import elmajdma.bakingx.steps.StepsFragment.OnStepsFragmentListener;
import elmajdma.bakingx.videoPlayer.RecipeStepsVideoPlayerFragment;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements OnStepsFragmentListener
 {

  @BindView(R.id.toolbar_baking_widget)
  Toolbar mBakingToolBar;

  @Nullable
  @BindView(R.id.fragment_recipe_video_loader)
  FrameLayout mFragmentVideoLoader;
  @BindView(R.id.fragment_recipe_detailed_loader)
  FrameLayout mFragmentStepsLoader;
  @Nullable
  @BindView(R.id.fragment_ingredient_loader)
  FrameLayout mFragmentIngredientsLoader;

  public static final String NO_VIDEO_AVAILABLE = "no_video";
  private static final String VIDEO_FRAGMENT_TAG = "video_fragment_tag";
  private static final String STEPS_FRAGMENT_TAG = "steps_fragment_tag";
   private static final String VIDEO_PLAYER_RECIP_FRAGMENT_TAG = "video_recipe_fragment_tag";
  private static final String INGREDIENT_FRAGMENT_TAG = "ingredient_fragment_tag";
  public static final String ALL_STEPS_LIST_KEY = "steps_list_key";
  public static final String VIDEO_URL_KEY = "video_key";
  public static final String STEP_DESCRIPTION_KEY = "step_description_key";
   public static final String STEPS_LIST_KEY = "steps_key";
   public static final String RECIPE_ID= "reipeId";
   private VideoPlayerRecipeFragment mVideoPlayerRecipeFragment;
   private RecipeStepsVideoPlayerFragment mRecipeStepsVideoPlayerFragment;
  private StepsFragment mStepsFragment;
  private IngredientFragment mIngredientFragment;
  private RecipeViewModel mRecipeViewModel;
  private RecipesRecyclerViewAdapter mRecipesRecyclerViewAdapter;
  private List<Steps> stepsList = new ArrayList<>();
  FragmentManager fragmentManager = getSupportFragmentManager();
 // int recipePosition = 0;
  int recipeId;
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_details);
    ButterKnife.bind(this);
    setSupportActionBar(mBakingToolBar);
    mBakingToolBar.hideOverflowMenu();
    mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    Intent intent = getIntent();
    //recipePosition = intent.getIntExtra(STEPS_LIST_KEY, -1);
    recipeId=intent.getIntExtra(RECIPE_ID,-1);
    mRecipeViewModel.getAllBakingDetails()
        .observe(this, new Observer<List<BakingApiModel>>() {
          @Override
          public void onChanged(@Nullable List<BakingApiModel> bakingApiModels) {
            //stepsList = bakingApiModels.get(recipePosition).getSteps();
                  for(BakingApiModel bm:bakingApiModels){
                    if(bm.getId()==recipeId){
                      stepsList=bm.getSteps();
                    }
                  }


          }
        });
    if (getResources().getBoolean(R.bool.isTablet)&&
        getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
      mTwoPane = true;
      if (savedInstanceState == null) {
        //createRequestedFragments(recipePosition);
        createRequestedFragments(recipeId);
      }
    } else {
      mTwoPane = false;
      if (savedInstanceState == null) {
        //setStepsFragment(recipePosition);
        setStepsFragment(recipeId);
      }
    }
  }


  private void setStepsFragment(int positon) {
    StepsFragment stepsFragment = new StepsFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_recipe_detailed_loader, stepsFragment);
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        android.R.anim.fade_out);
    Bundle bundle = new Bundle();
    //bundle.putInt(STEPS_LIST_KEY, positon);
    bundle.putInt(RECIPE_ID, positon);
    stepsFragment.setArguments(bundle);
   // fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

/*  @Override
  public void setVideoPositionStepDescripition(int videoToplay) {
    String videoUrl=stepsList.get(videoToplay).getVideoURL();
    String stepDescription=stepsList.get(videoToplay).getDescription();

    if (!mTwoPane) {
      mVideoPlayerRecipeFragment = new VideoPlayerRecipeFragment();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.fragment_recipe_detailed_loader, mVideoPlayerRecipeFragment);
      fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
          android.R.anim.fade_out);
      Bundle bundle = new Bundle();
      bundle.putString(VIDEO_URL_KEY, videoUrl);
      bundle.putString(STEP_DESCRIPTION_KEY, stepDescription);
      mVideoPlayerRecipeFragment.setArguments(bundle);
      fragmentTransaction.addToBackStack(null);
      fragmentTransaction.commit();

    } else {
      mFragmentIngredientsLoader.setVisibility(View.GONE);
      mFragmentVideoLoader.setVisibility(View.VISIBLE);
      mVideoPlayerRecipeFragment.initRecipeExoPlayer(videoUrl,
          stepDescription);
    }

  }*/

   @Override
   public void setVideoPositionStepDescripition(int videoToplay) {
     String videoUrl=stepsList.get(videoToplay).getVideoURL();
     String stepDescription=stepsList.get(videoToplay).getDescription();

     if (!mTwoPane) {
       mRecipeStepsVideoPlayerFragment= new RecipeStepsVideoPlayerFragment();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.fragment_recipe_detailed_loader, mRecipeStepsVideoPlayerFragment);
       fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
           android.R.anim.fade_out);
       Bundle bundle = new Bundle();
       bundle.putString(VIDEO_URL_KEY, videoUrl);
       bundle.putString(STEP_DESCRIPTION_KEY, stepDescription);
       mRecipeStepsVideoPlayerFragment.setArguments(bundle);
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();

     } else {
       mFragmentIngredientsLoader.setVisibility(View.GONE);
       mFragmentVideoLoader.setVisibility(View.VISIBLE);
       mRecipeStepsVideoPlayerFragment.initializePlayer(videoUrl);
           //stepDescription);
     }

   }

  @Override
  public void setIngredientFragmentList() {
    if (mTwoPane) {
      mIngredientFragment = (IngredientFragment) fragmentManager
          .findFragmentByTag(INGREDIENT_FRAGMENT_TAG);
      //if (mIngredientFragment == null) {
      mIngredientFragment = new IngredientFragment();
      Bundle bundle = new Bundle();
      //bundle.putInt(STEPS_LIST_KEY, recipePosition);
      bundle.putInt(RECIPE_ID, recipeId);
      mIngredientFragment.setArguments(bundle);
      mFragmentVideoLoader.setVisibility(View.GONE);
      mFragmentIngredientsLoader.setVisibility(View.VISIBLE);
      fragmentManager.beginTransaction()
          .add(R.id.fragment_ingredient_loader, mIngredientFragment, INGREDIENT_FRAGMENT_TAG)
          .commit();
    }else{
      setIngredientFragment();
    }
  }

  private void createRequestedFragments(int recipePosition) {
    mStepsFragment = (StepsFragment) fragmentManager.findFragmentByTag(STEPS_FRAGMENT_TAG);
    if (mStepsFragment == null) {
      mStepsFragment = new StepsFragment();
      Bundle bundle = new Bundle();
      //bundle.putInt(STEPS_LIST_KEY, recipePosition);
      bundle.putInt(RECIPE_ID, recipePosition);
      mStepsFragment.setArguments(bundle);
      fragmentManager.beginTransaction()
          .add(R.id.fragment_recipe_detailed_loader, mStepsFragment, STEPS_FRAGMENT_TAG).commit();
      mVideoPlayerRecipeFragment = (VideoPlayerRecipeFragment) fragmentManager
          .findFragmentByTag(VIDEO_PLAYER_RECIP_FRAGMENT_TAG);
      if (mVideoPlayerRecipeFragment == null) {
        mVideoPlayerRecipeFragment = new VideoPlayerRecipeFragment();
        mFragmentIngredientsLoader.setVisibility(View.GONE);
        mFragmentVideoLoader.setVisibility(View.VISIBLE);

        fragmentManager.beginTransaction()
            .add(R.id.fragment_recipe_video_loader, mVideoPlayerRecipeFragment, VIDEO_PLAYER_RECIP_FRAGMENT_TAG)
            .commit();
      }
    }
  }



  private void setIngredientFragment() {
    mIngredientFragment = new IngredientFragment();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_recipe_detailed_loader, mIngredientFragment);
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        android.R.anim.fade_out);
    Bundle bundle = new Bundle();
    //bundle.putInt(STEPS_LIST_KEY, recipePosition);
    bundle.putInt(RECIPE_ID, recipeId);
    mIngredientFragment.setArguments(bundle);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }
   @Override
   public void onBackPressed() {
     if (getSupportFragmentManager().getBackStackEntryCount() > 0)
       getSupportFragmentManager().popBackStack();
     else
       finish();    // Finish the activity
   }
}
