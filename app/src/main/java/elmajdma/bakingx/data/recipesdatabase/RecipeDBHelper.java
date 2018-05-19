package elmajdma.bakingx.data.recipesdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import elmajdma.bakingx.data.recipesdatabase.RecipeContract.RecipeEntry;

public class RecipeDBHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "recipefavorite.db";


  public RecipeDBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    addRecipeTable(db);
  }



  private void addRecipeTable(SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE " + RecipeEntry.TABLE_RECIPE + " (" +
            RecipeEntry.COLUMN_RECIPE_TITLE + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL," +
            RecipeEntry.COLUMN_RECIPE_INGREDIENT + " TEXT NOT NULL," +
            RecipeEntry.COLUMN_SERVING+ " INTEGER NOT NULL," +
            " UNIQUE (" + RecipeEntry.COLUMN_RECIPE_TITLE + ") ON CONFLICT REPLACE);"

    );
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_RECIPE);
    onCreate(sqLiteDatabase);
  }
}
