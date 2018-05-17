package elmajdma.bakingx.recipewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import elmajdma.bakingx.R;
import elmajdma.bakingx.RecipeDetailsActivity;
import elmajdma.bakingx.data.model.WidgetRecipeModel;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidget extends AppWidgetProvider {

  static void updateRecipWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {
    // Get current width to decide on single plant vs garden grid view
    // Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
    //  int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
    RemoteViews rv;
    //if (width < 300) {
    //    rv = getSinglePlantRemoteView(context, imgRes, plantId, showWater);
    //  } else {
    rv = getGardenGridRemoteView(context);
    //  }
    appWidgetManager.updateAppWidget(appWidgetId, rv);
  }


  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId);
      RecipeWidgetService.startActionUpdateRecipeWidgets(context);

    }

  }
  public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
    for (int appWidgetId : appWidgetIds) {
      updateRecipWidget(context, appWidgetManager, appWidgetId);
    }
  }




       // Creates and returns the RemoteViews to be displayed in the GridView mode widget
   private static RemoteViews getGardenGridRemoteView(Context context) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_layout);
       // Set the GridWidgetService intent to act as the adapter for the GridView
       Intent intent = new Intent(context,  RecipeListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_listView, intent);
        // Set the PlantDetailActivity intent to launch when clicked
              Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       views.setPendingIntentTemplate(R.id.widget_listView, appPendingIntent);
        // Handle empty gardens
           // views.setEmptyView(R.id.widget_listView, R.id.empty_view);
          return views;
  }



 @Override
public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                   int appWidgetId, Bundle newOptions) {
   RecipeWidgetService.startActionUpdateRecipeWidgets(context);
     super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
  }
  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    // Perform any action when one or more AppWidget instances have been deleted
  }

  @Override
  public void onEnabled(Context context) {
    // Perform any action when an AppWidget for this provider is instantiated
  }

  @Override
  public void onDisabled(Context context) {
    // Perform any action when the last AppWidget instance for this provider is deleted
  }

}

