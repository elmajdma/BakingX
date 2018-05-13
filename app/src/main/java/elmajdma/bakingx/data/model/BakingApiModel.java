package elmajdma.bakingx.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BakingApiModel implements Parcelable {
  public static final Creator<BakingApiModel> CREATOR = new Creator<BakingApiModel>() {
    @Override
    public BakingApiModel createFromParcel(Parcel in) {
      return new BakingApiModel(in);
    }
    @Override
    public BakingApiModel[] newArray(int size) {
      return new BakingApiModel[size];
    }
  };
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("ingredients")
  @Expose
  private List<Ingredients> ingredients = null;
  @SerializedName("steps")
  @Expose
  private List<Steps> steps = null;
  @SerializedName("servings")
  @Expose
  private Integer servings;
  @SerializedName("image")
  @Expose
  private String image;
  protected BakingApiModel(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readInt();
    }
    name = in.readString();
    ingredients = in.createTypedArrayList(Ingredients.CREATOR);
    if (in.readByte() == 0) {
      servings = null;
    } else {
      servings = in.readInt();
    }
    image = in.readString();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredients> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredients> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Steps> getSteps() {
    return steps;
  }

  public void setSteps(List<Steps> steps) {
    this.steps = steps;
  }

  public Integer getServings() {
    return servings;
  }

  public void setServings(Integer servings) {
    this.servings = servings;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "BakingApiModel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", ingredients=" + ingredients +
        ", steps=" + steps +
        ", servings=" + servings +
        ", image='" + image + '\'' +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (id == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(id);
    }
    dest.writeString(name);
    dest.writeTypedList(ingredients);
    if (servings == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeInt(servings);
    }
    dest.writeString(image);
  }
}
