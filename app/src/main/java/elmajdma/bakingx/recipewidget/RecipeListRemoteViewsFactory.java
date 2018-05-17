package elmajdma.bakingx.recipewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import elmajdma.bakingx.R;
import elmajdma.bakingx.RecipeDetailsActivity;
import elmajdma.bakingx.data.model.WidgetRecipeModel;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;

public class RecipeListRemoteViewsFactory
    implements RemoteViewsService.RemoteViewsFactory  {
  private Context mContext;
  private Cursor mCursor;

  public RecipeListRemoteViewsFactory(Context mContext) {
    this.mContext = mContext;
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDataSetChanged() {
    if(mCursor!=null) mCursor.close();
    mCursor = mContext.getContentResolver().query(
        RecipeEntry.CONTENT_URI,
        null,
        null,
        null,
        RecipeEntry.COLUMN_RECIPE_TITLE);
  }

  @Override
  public void onDestroy() {
    mCursor.close();
  }

  @Override
  public int getCount() {
    if (mCursor == null) return 0;
     return mCursor.getCount();
  }

  @Override
  public RemoteViews getViewAt(int position) {
    if (mCursor == null || mCursor.getCount() == 0) return null;
    mCursor.moveToPosition(position);
      int recipeTitleIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_TITLE);
      int recipeIngredientIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_INGREDIENT);
      int recipeServingIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_SERVING);
      int recipeIdIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_ID);

    final RemoteViews remoteView = new RemoteViews(
        mContext.getPackageName(), R.layout.widget_list_row);

    remoteView.setTextViewText(R.id.tv_widget_recipe_title,
        mCursor.getString(recipeTitleIndex));
    remoteView.setTextViewText(R.id.tv_widget_recipe_ingredient,
        mCursor.getString(recipeIngredientIndex));
    String servingNum= "Serving: "+ mCursor.getString(recipeServingIndex);
    remoteView.setTextViewText(R.id.tv_widget_recipe_serving,
        servingNum);
    // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
            extras.putInt(RecipeDetailsActivity.STEPS_LIST_KEY, mCursor.getInt(recipeIdIndex));
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
           remoteView.setOnClickFillInIntent(R.id.tv_widget_recipe_ingredient, fillInIntent);

return remoteView;


  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1; // Treat all items in the GridView the same
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
