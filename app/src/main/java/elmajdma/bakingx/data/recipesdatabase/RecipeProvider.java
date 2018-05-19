package elmajdma.bakingx.data.recipesdatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;

public class RecipeProvider extends ContentProvider {

  private static final int RECIPE = 100;
  private static final int RECIPE_ID = 101;
  private static final UriMatcher sUriMatcher = buildUriMatcher();
  private RecipeDBHelper mOpenHelper;

  /**
   * Builds a UriMatcher that is used to determine witch database request is being made.
   */
  public static UriMatcher buildUriMatcher() {
    //String content = MovieContract.CONTENT_AUTHORITY;

    // All paths to the UriMatcher have a corresponding code to return
    // when a match is found (the ints above).
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    matcher.addURI(RecipeContract.CONTENT_AUTHORITY, RecipeEntry.TABLE_RECIPE, RECIPE);
    matcher.addURI(RecipeContract.CONTENT_AUTHORITY, RecipeEntry.TABLE_RECIPE + "/#",
        RECIPE_ID);
    return matcher;
  }

  @Override
  public boolean onCreate() {

    mOpenHelper = new RecipeDBHelper(getContext());
    return true;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    Cursor recipeCursor;
    switch (sUriMatcher.match(uri)) {
      case RECIPE:
        recipeCursor = mOpenHelper.getReadableDatabase().query(
            RecipeEntry.TABLE_RECIPE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        break;
      case RECIPE_ID:
        recipeCursor = mOpenHelper.getReadableDatabase().query(
            RecipeEntry.TABLE_RECIPE,
            projection,
            RecipeEntry.COLUMN_RECIPE_ID + "= ?",
            new String[]{String.valueOf(ContentUris.parseId(uri))},
            null,
            null,
            sortOrder
        );
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Set the notification URI for the cursor to the one passed into the function. This
    // causes the cursor to register a content observer to watch for changes that happen to
    // this URI and any of it's descendants. By descendants, we mean any URI that begins
    // with this path.
    recipeCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return recipeCursor;

  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    final int match = sUriMatcher.match(uri);

    switch (match) {
      case RECIPE: {
        return RecipeEntry.CONTENT_DIR_TYPE;
      }
      case RECIPE_ID: {
        return RecipeEntry.CONTENT_ITEM_TYPE;
      }
      default: {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
      }
    }
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    Uri returnUri = null;
    switch (sUriMatcher.match(uri)) {
      case RECIPE:
                /*
                  Add a new recipe record
                 */
        long rowID = db.insert(RecipeEntry.TABLE_RECIPE, "", values);
                /*
                  If record is added successfully
                 */
        if (rowID > 0) {
          returnUri = RecipeContract.RecipeEntry.buildRecipeUri(rowID);

        } else {
          throw new SQLException("Failed to insert row into: " + uri);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Use this on the URI passed into the function to notify any observers that the uri has
    // changed.
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;

  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int numDeleted;
    switch (match) {
      case RECIPE:
        numDeleted = db.delete(
            RecipeEntry.TABLE_RECIPE, selection, selectionArgs);

        break;
      case RECIPE_ID:
        numDeleted = db.delete(RecipeEntry.TABLE_RECIPE,
            RecipeEntry.COLUMN_RECIPE_ID + " = ?",
            new String[]{String.valueOf(ContentUris.parseId(uri))});
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);

    return numDeleted;

  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
      @Nullable String[] selectionArgs) {
    final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    int rows;

    switch (sUriMatcher.match(uri)) {
      case RECIPE:
        rows = db.update(RecipeEntry.TABLE_RECIPE, values, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    if (rows != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return rows;

  }
}
