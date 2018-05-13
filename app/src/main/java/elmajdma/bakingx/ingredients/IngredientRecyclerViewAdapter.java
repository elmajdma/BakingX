package elmajdma.bakingx.ingredients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.R;
import elmajdma.bakingx.data.model.Ingredients;
import java.util.ArrayList;
import java.util.List;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private IngredientAdapterOnClickHandler mClickHandler;
  private Context context;
  private List<Ingredients> ingredientsRecipesList = new ArrayList<>();

  public IngredientRecyclerViewAdapter(Context context, List<Ingredients> ingredientsRecipesList) {
    this.context = context;
    this.ingredientsRecipesList = ingredientsRecipesList;
  }
  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View ingredientView = inflater.inflate(R.layout.ingredient_item, parent, false);
    return new IngredientViewHolder(ingredientView, mClickHandler);
  }
  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    IngredientRecyclerViewAdapter.IngredientViewHolder ingredientViewHolder
        = (IngredientRecyclerViewAdapter.IngredientViewHolder) holder;
    configureViewHolderIngredients(ingredientViewHolder, position);
  }
  @Override
  public int getItemCount() {
    return (ingredientsRecipesList != null) ? ingredientsRecipesList.size() : 0;
  }
  private void configureViewHolderIngredients(IngredientViewHolder ingredientViewHolder,
      int position) {
    Ingredients ingredients = ingredientsRecipesList.get(position);
    if (ingredients != null) {
      ingredientViewHolder.tvIngredientTitle.setText(ingredients.getIngredient());
      ingredientViewHolder.tvIngredientQt.setText(String.valueOf(ingredients.getQuantity()));
      ingredientViewHolder.tvIngredientQtType.setText(ingredients.getMeasure());
    }
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface IngredientAdapterOnClickHandler {
    void onIngredientCardClick(int position, View v);
  }
  public class IngredientViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {
    @BindView(R.id.tv_ingredient)
    TextView tvIngredientTitle;
    @BindView(R.id.tv_ingredient_qt)
    TextView tvIngredientQt;
    @BindView(R.id.tv_ingredient_qt_type)
    TextView tvIngredientQtType;
    @BindView(R.id.cardview_ingredient_item)
    CardView ingredientCard;
    private IngredientAdapterOnClickHandler mListener;

    public IngredientViewHolder(View itemView, IngredientAdapterOnClickHandler listener) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      mListener = listener;
      ingredientCard.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
      mListener.onIngredientCardClick(getAdapterPosition(), v);
    }
  }
}
