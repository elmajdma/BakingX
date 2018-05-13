package elmajdma.bakingx.recipewidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.BakingApiModel;
import java.util.ArrayList;
import java.util.List;

public class ListProviderFactory implements RemoteViewsFactory {
  private List<BakingApiModel> listItemList = new ArrayList<>();
  private Context context = null;
  private int appWidgetId;

  public ListProviderFactory(Context context,  Intent intent) {
    this.context = context;
    appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);

    populateListItem();
  }
  private void populateListItem() {
    if(RemoteFetchService.listIngredientsList !=null )
      listItemList = RemoteFetchService.listIngredientsList;

    else
      listItemList = new ArrayList<BakingApiModel>();

  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDataSetChanged() {

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public int getCount() {
    return listItemList.size();
  }

  @Override
  public RemoteViews getViewAt(int position) {
    ArrayList<String> ingredients=new ArrayList<>();

    final RemoteViews remoteView = new RemoteViews(
        context.getPackageName(), R.layout.widget_list_row);
    BakingApiModel listItem = listItemList.get(position);
    remoteView.setTextViewText(R.id.tv_widget_recipe_title, listItem.getName());

    for(int i=0; i<listItem.getIngredients().size();i++){
      String ingredient =listItem.getIngredients().get(i).getIngredient()+" "+
          String.valueOf(listItem.getIngredients().get(i).getQuantity())+" "+listItem.getIngredients().get(i).getMeasure();
      ingredients.add(ingredient);

      //remoteView.setTextViewText(R.id.tv_widget_recipe_ingredient, listItem.getIngredients().get(i).getIngredient());
      //remoteView.setTextViewText(R.id.tv_widget_recipe_ingredient_measure, listItem.getIngredients().get(i).getMeasure());
      //remoteView.setTextViewText(R.id.tv_widget_recipe_ingredient_quntity, String.valueOf(listItem.getIngredients().get(i).getQuantity()));
    }
    remoteView.setTextViewText(R.id.tv_widget_recipe_ingredient, ingredients.toString());
    //remoteView.setTextViewText(R.id.content, listItem.content);

    return remoteView;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
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
