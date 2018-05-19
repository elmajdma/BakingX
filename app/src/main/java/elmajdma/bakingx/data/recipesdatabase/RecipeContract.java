package elmajdma.bakingx.data.recipesdatabase;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {
  /**
   * The Content Authority is a name for the entire content provider, similar to the relationship
   * between a domain name and its website. A convenient string to use for content authority is
   * the package name for the app, since it is guaranteed to be unique on the device.
   */
  public static final String CONTENT_AUTHORITY = "elmajdma.bakingx.data.recipesdatabase";

  /**
   * The content authority is used to create the base of all URIs which apps will use to
   * contact this content provider.
   */
  private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



  /**
   * Create one class for each table that handles all information regarding the table schema and
   * the URIs related to it.
   */
  public static final class RecipeEntry implements BaseColumns {
    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String TABLE_RECIPE = "recipe";
    // Content URI represents the base location for the table
    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(TABLE_RECIPE).build();

    // create cursor of base type directory for multiple entries
    public static final String CONTENT_DIR_TYPE =
        //  "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_RECIPE;
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_RECIPE;
    public static final String CONTENT_ITEM_TYPE =
        //"vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_RECIPE;
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_RECIPE;

    // Define the table schema
    public static final String COLUMN_RECIPE_TITLE = "recipeTitle";
    public static final String COLUMN_RECIPE_ID ="recipeId";
    public static final String COLUMN_SERVING = "serving";
    public static final String COLUMN_RECIPE_INGREDIENT = "ingredient";

    // Define a function to build a URI to find a specific recipe by it's identifier
    // for building URIs on insertion
    public static Uri buildRecipeUri(long id) {
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }
}
