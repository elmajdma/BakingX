package elmajdma.bakingx;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class CalcFittedColumn {
  public static int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int) (dpWidth / 180);
    if (noOfColumns < 2)
      noOfColumns = 2;
    return noOfColumns;
  }


}
