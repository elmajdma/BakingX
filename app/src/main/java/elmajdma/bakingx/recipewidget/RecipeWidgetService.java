package elmajdma.bakingx.recipewidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.WidgetRecipeModel;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;
import java.util.ArrayList;
import java.util.List;


public class RecipeWidgetService extends IntentService {
  private static final String ACTION_UPDATE_FOVARITE_RECIPE_WIDGET_LIST =
      "elmajdma.bakingx.recipewidget.action.UPDATE_FOVARITE_RECIPE_WIDGET_LIST";

  public RecipeWidgetService() {
    super("RecipeWidgetService");
  }
  public static void startActionUpdateRecipeWidgets(Context context) {
    Intent intent = new Intent(context, RecipeWidgetService.class);
    intent.setAction(ACTION_UPDATE_FOVARITE_RECIPE_WIDGET_LIST);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      if (ACTION_UPDATE_FOVARITE_RECIPE_WIDGET_LIST.equals(intent.getAction())) {
        handleActionUpdateRecipeWidgets();
      }
    }
  }
  private void handleActionUpdateRecipeWidgets() {
    /*List<WidgetRecipeModel> widgetRecipeModels=new ArrayList<>();
    Cursor cursor = getContentResolver().query(
        RecipeEntry.CONTENT_URI,
        null,
        null,
        null,
        RecipeEntry.COLUMN_RECIPE_TITLE);

    if (cursor != null && cursor.getCount() > 0) {
      int recipeTitleIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_TITLE);
      int recipeIngredientIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_INGREDIENT);
      int recipeServingIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_SERVING);
      int recipeIdIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_ID);
      long timeNow = System.currentTimeMillis();
      try {
        while (cursor.moveToNext()) {
        widgetRecipeModels.add(new WidgetRecipeModel(cursor.getString(recipeTitleIndex),
            cursor.getString(recipeIngredientIndex),cursor.getInt(recipeIdIndex),
            cursor.getInt(recipeServingIndex)));
        }
      } finally {
        cursor.close();
      }
    }*/
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

    int[] appWidgetIds = appWidgetManager
        .getAppWidgetIds(new ComponentName(this,RecipeIngredientWidget.class));
      //Trigger data update to handle the GridView widgets and force a data refresh
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
    //Now update all widgets
   RecipeIngredientWidget
      .updateRecipeWidgets(this, appWidgetManager, appWidgetIds);

  }
}
