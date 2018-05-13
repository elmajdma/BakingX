package elmajdma.bakingx.data.remote;

import elmajdma.bakingx.data.model.BakingApiModel;
import elmajdma.bakingx.data.model.Ingredients;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BakingApiService {

  @GET("/topher/2017/May/59121517_baking/baking.json")
  Flowable<List<BakingApiModel>> readJson();

  @GET("/topher/2017/May/59121517_baking/baking.json")
  Flowable<List<Ingredients>> getIngredient(@Query("id") int bakingId);

  @GET("/topher/2017/May/59121517_baking/baking.json")
  Flowable<BakingApiModel> getSingleBaking(@Query("id") int bakingId);
}
