package elmajdma.bakingx.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Ingredients;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.data.remote.BakingRetrofitClient;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {

  private List<BakingApiModel> bakingData = new ArrayList<>();
  private Flowable<List<BakingApiModel>> x;
  private int itemPosition;
  private LiveData<List<BakingApiModel>> selectedbakingdata = new MutableLiveData<List<BakingApiModel>>();
  private LiveData<Object> stepsMutableList = new MutableLiveData<>();
  private MutableLiveData<List<Ingredients>> ingredientMutableList = new MutableLiveData<>();

  public RecipeViewModel() {
    selectedbakingdata = LiveDataReactiveStreams
        .fromPublisher(BakingRetrofitClient.getBakingobject());
  }

  public LiveData<List<BakingApiModel>> getAllBakingDetails() {
    return selectedbakingdata;
  }

}
