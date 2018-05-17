package elmajdma.bakingx;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.data.utils.InternetCheck;
import elmajdma.bakingx.interentfail.NoInternetConnectionFragment;
import elmajdma.bakingx.recipes.RecipFragment;


public class MainActivity extends AppCompatActivity {
  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  public static final String RECIPES_LIST_KEY = "recipes_key";
  public static final String INGREDIENT_LIST_KEY = "ingredient_key";
  public static final String STEPS_LIST_KEY = "steps_key";
  public static final String POSITION_KEY = "position_key";
  public static final String LAYOUT_KEY = "layout_key";
  public static final int LINER_LAYOUT_KEY=1;
  public static final int GRID_LAYOUT_KEY=2;
  @BindView(R.id.toolbar_baking_widget)
  Toolbar mBakingToolBar;
  @BindView(R.id.fragment_loader)
  FrameLayout mFragmentLoader;
  private int layoutRequested=1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(mBakingToolBar);
    mBakingToolBar.hideOverflowMenu();
    if (getResources().getBoolean(R.bool.isTablet)) {
      layoutRequested=GRID_LAYOUT_KEY;
    }
    InternetCheck internetCheck = new InternetCheck(internet -> {
      if(!internet){
        getNoInternetFragment();
      }else {
          setmainFragment(layoutRequested);

      }
    });
  }
  private void setmainFragment(int layoutKey) {
        Fragment fragment = new RecipFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_loader, fragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
            android.R.anim.fade_out);
    Bundle bundle = new Bundle();
    bundle.putInt(LAYOUT_KEY, layoutKey);
    fragment.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();
  }

  public static boolean isTablet(Context context)
  {
    return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
        >= Configuration.SCREENLAYOUT_SIZE_LARGE;
  }
  private void getNoInternetFragment(){
    Fragment fragment = new NoInternetConnectionFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment_loader, fragment);
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
        android.R.anim.fade_out);
    fragmentTransaction.commitAllowingStateLoss();
  }
  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
      getSupportFragmentManager().popBackStack();
    else
      finish();    // Finish the activity
  }
}
