package elmajdma.bakingx.data.model;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "steps")
public class Steps implements Parcelable {

  public static final Creator<Steps> CREATOR = new Creator<Steps>() {
    @Override
    public Steps createFromParcel(Parcel in) {
      return new Steps(in);
    }

    @Override
    public Steps[] newArray(int size) {
      return new Steps[size];
    }
  };
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("shortDescription")
  @Expose
  private String shortDescription;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("videoURL")
  @Expose
  private String videoURL;
  @SerializedName("thumbnailURL")
  @Expose
  private String thumbnailURL;

  protected Steps(Parcel in) {
    if (in.readByte() == 0) {
      id = null;
    } else {
      id = in.readInt();
    }
    shortDescription = in.readString();
    description = in.readString();
    videoURL = in.readString();
    thumbnailURL = in.readString();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  public void setThumbnailURL(String thumbnailURL) {
    this.thumbnailURL = thumbnailURL;
  }

  @Override
  public String toString() {
    return "Steps{" +
        "id=" + id +
        ", shortDescription='" + shortDescription + '\'' +
        ", description='" + description + '\'' +
        ", videoURL='" + videoURL + '\'' +
        ", thumbnailURL='" + thumbnailURL + '\'' +
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
    dest.writeString(shortDescription);
    dest.writeString(description);
    dest.writeString(videoURL);
    dest.writeString(thumbnailURL);
  }
}
