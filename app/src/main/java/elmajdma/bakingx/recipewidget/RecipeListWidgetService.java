package elmajdma.bakingx.recipewidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

public class RecipeListWidgetService extends RemoteViewsService {

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new RecipeListRemoteViewsFactory(this.getApplicationContext());
  }

}
