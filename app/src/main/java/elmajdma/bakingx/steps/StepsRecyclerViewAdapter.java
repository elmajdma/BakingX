package elmajdma.bakingx.steps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.Steps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private StepsRecyclerViewAdapter.StepsAdapterOnClickHandler mClickHandler;
  private Context context;
  private List<Steps> stepsRecipesList = new ArrayList<>();

  public StepsRecyclerViewAdapter(Context context, List<Steps> stepsRecipesList, StepsAdapterOnClickHandler mClickHandler) {
    this.context = context;
    this.stepsRecipesList = stepsRecipesList;
    this.mClickHandler=mClickHandler;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View stepsView = inflater.inflate(R.layout.steps_item, parent, false);
    return new StepsRecyclerViewAdapter.StepsViewHolder(stepsView, mClickHandler);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    StepsRecyclerViewAdapter.StepsViewHolder stepsViewHolder
        = (StepsViewHolder) holder;
    configureViewHolderSteps(stepsViewHolder, position);
  }

  @Override
  public int getItemCount() {
    return (stepsRecipesList != null) ? stepsRecipesList.size() : 0;
  }

  @SuppressLint("StaticFieldLeak")
  private void configureViewHolderSteps(StepsRecyclerViewAdapter.StepsViewHolder stepsViewHolder,
      int position) {
    Steps steps = stepsRecipesList.get(position);
    //String shortDescription=stepsRecipesList.get(position);
    if (steps!=null) {
      stepsViewHolder.tvStepsTitle.setText(steps.getShortDescription());
      if(!steps.getThumbnailURL().isEmpty()){
        Picasso.with(context).load(steps.getThumbnailURL()).into(stepsViewHolder.imgVideoThumb);
      }else{
        Picasso.with(context).load(R.drawable.cupcake).into(stepsViewHolder.imgVideoThumb);
      }

   }
  }

  public interface StepsAdapterOnClickHandler {

    void onStepsCardClick(int position, View v);
  }

  public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   @BindView(R.id.img_video_thumb)
    ImageView imgVideoThumb;
    @BindView(R.id.tv_step_title)
    TextView tvStepsTitle;
    @BindView(R.id.tv_step_description)
    TextView tvStepsDescription;
    @BindView(R.id.cardview_steps_item)
    CardView stepsCard;
    private StepsRecyclerViewAdapter.StepsAdapterOnClickHandler mListener;

    public StepsViewHolder(View itemView,
        StepsRecyclerViewAdapter.StepsAdapterOnClickHandler listener) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mListener = listener;
      stepsCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      mListener.onStepsCardClick(getAdapterPosition(), v);

    }
  }


}
