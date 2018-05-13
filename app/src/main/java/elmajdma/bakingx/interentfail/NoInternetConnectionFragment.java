package elmajdma.bakingx.interentfail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import elmajdma.bakingx.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoInternetConnectionFragment extends Fragment {

  @BindView(R.id.tv_no_iternet_connection)
  TextView noIternentConnection;


  public NoInternetConnectionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_no_internet_connection, container, false);
    ButterKnife.bind(this, view);
    noIternentConnection.setText(R.string.check_internet);
    return view;
  }

}
