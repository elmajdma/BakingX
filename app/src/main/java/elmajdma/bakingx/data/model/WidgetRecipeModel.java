package elmajdma.bakingx.data.model;

public class WidgetRecipeModel {
  private String recipeTitle;
  private String recipeIgredient;
  private int recipeId;
  private int recipeServing;

  public WidgetRecipeModel(String recipeTitle, String recipeIgredient, int recipeId,
      int recipeServing) {
    this.recipeTitle = recipeTitle;
    this.recipeIgredient = recipeIgredient;
    this.recipeId = recipeId;
    this.recipeServing = recipeServing;
  }

  public String getRecipeTitle() {
    return recipeTitle;
  }

  public void setRecipeTitle(String recipeTitle) {
    this.recipeTitle = recipeTitle;
  }

  public String getRecipeIgredient() {
    return recipeIgredient;
  }

  public void setRecipeIgredient(String recipeIgredient) {
    this.recipeIgredient = recipeIgredient;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  public int getRecipeServing() {
    return recipeServing;
  }

  public void setRecipeServing(int recipeServing) {
    this.recipeServing = recipeServing;
  }
}
