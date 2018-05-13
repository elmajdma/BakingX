package elmajdma.bakingx.recipewidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import elmajdma.bakingx.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidget extends AppWidgetProvider {
  public static final String DATA_FETCHED = "elmajdma.bakingx.recipewidget.DATA_FETCHED";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      Intent serviceIntent = new Intent(context, RemoteFetchService.class);
      serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
          appWidgetId);
      context.startService(serviceIntent);
    }
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

  private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

    // which layout to show on widget
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
        R.layout.recipe_widget_layout);

    // RemoteViews Service needed to provide adapter for ListView
    Intent svcIntent = new Intent(context, WidgetService.class);
    // passing app widget id to that RemoteViews Service
    svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    // setting a unique Uri to the intent
    // don't know its purpose to me right now
    svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
    // setting adapter to listview of the widget
    remoteViews.setRemoteAdapter(R.id.widget_listView,
        svcIntent);
    // setting an empty view in case of no data
    //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
    return remoteViews;
  }

  /*
   * It receives the broadcast as per the action set on intent filters on
   * Manifest.xml once data is fetched from RemotePostService,it sends
   * broadcast and WidgetProvider notifies to change the data the data change
   * right now happens on ListProvider as it takes RemoteFetchService
   * listItemList as data
   */
  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    if (intent.getAction().equals(DATA_FETCHED)) {
      int appWidgetId = intent.getIntExtra(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);
      AppWidgetManager appWidgetManager = AppWidgetManager
          .getInstance(context);
      RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
      appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

  }

}

