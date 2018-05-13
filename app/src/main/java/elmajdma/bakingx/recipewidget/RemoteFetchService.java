package elmajdma.bakingx.recipewidget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.remote.BakingRetrofitClient;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class RemoteFetchService extends Service {
  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  public static List<BakingApiModel> listIngredientsList;

  public RemoteFetchService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  /*
   * Retrieve appwidget id from intent it is needed to update widget later
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
      appWidgetId = intent.getIntExtra(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);
    fetchDataFromWeb();
    return super.onStartCommand(intent, flags, startId);
  }
  /**
   * method which fetches data(json) from web aquery takes params
   * remoteJsonUrl = from where data to be fetched String.class = return
   * format of data once fetched i.e. in which format the fetched data be
   * returned AjaxCallback = class to notify with data once it is fetched
   */
  @SuppressLint("CheckResult")
  private void fetchDataFromWeb() {
    Flowable<List<BakingApiModel>> baking=BakingRetrofitClient.getBakingobject();
    /*baking.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(bakingApiModels -> listIngredientsList=bakingApiModels);
    //populateWidget();*/
  baking.subscribe(new Consumer<List<BakingApiModel>>() {
    @Override
    public void accept(List<BakingApiModel> bakingApiModels) throws Exception {
      listIngredientsList=bakingApiModels;
    }
  });
    /*baking.subscribeOn(Schedulers.computation()).subscribe(event -> {
    listIngredientsList=event;

    }, onError -> {
      Log.e("Error_lad_widget_data" ,Thread.currentThread().getName());
      onError.printStackTrace();
    });*/

    populateWidget();
  }

  /**
   * Method which sends broadcast to WidgetProvider
   * so that widget is notified to do necessary action
   * and here action == WidgetProvider.DATA_FETCHED
   */
  private void populateWidget() {

    Intent widgetUpdateIntent = new Intent();
    widgetUpdateIntent.setAction(RecipeIngredientWidget.DATA_FETCHED);
    widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        appWidgetId);
    sendBroadcast(widgetUpdateIntent);
    this.stopSelf();
  }

}
