package elmajdma.bakingx;


import static elmajdma.bakingx.RecipeDetailsActivity.STEP_DESCRIPTION_KEY;
import static elmajdma.bakingx.RecipeDetailsActivity.VIDEO_URL_KEY;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import elmajdma.bakingx.data.model.Steps;
import elmajdma.bakingx.recipes.RecipeViewModel;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerRecipeFragment extends Fragment  {
  @BindView(R.id.player_view)
  PlayerView mExoPlayerView;
  @BindView(R.id.tv_steps_description)
  TextView stepDescription;
  @BindView(R.id.exo_fullscreen_icon)
  ImageView mFullScreenIcon;
  @BindView(R.id.exo_fullscreen_button)
  FrameLayout mFullScreenButton;
  @BindView(R.id.exo_controller)
  PlayerControlView controlView;
  @BindView(R.id.frameLayout)
  FrameLayout mFrameLayout;
  private final String STATE_RESUME_WINDOW = "resumeWindow";
  private final String STATE_RESUME_POSITION = "resumePosition";
  private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
  private boolean mExoPlayerFullscreen = false;
  private MediaSource mVideoSource;
  private String stepVideoDescUrl;
  private String stepsComplDescripton;
  private Timeline.Window window;
  private DataSource.Factory mediaDataSourceFactory;
  private DefaultTrackSelector trackSelector;
  private boolean shouldAutoPlay;
  private BandwidthMeter bandwidthMeter;
  private MediaSessionCompat mMediaSession;
  private PlaybackStateCompat.Builder mStateBuilder;
  private RecipeViewModel mRecipeViewModel;
  private List<Steps> stepsList;
  private SimpleExoPlayer recipePlayer;
  private Dialog mFullScreenDialog;
  private int mResumeWindow;
  private long mResumePosition;
  public VideoPlayerRecipeFragment() {
    // Required empty public constructor
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_video_player_recipe, container, false);
    ButterKnife.bind(this, view);
    initFullscreenDialog();
    initFullscreenButton();
    Bundle bundle=getArguments();
    if(bundle!=null){
      initRecipeExoPlayer(bundle.getString(VIDEO_URL_KEY), bundle.getString(STEP_DESCRIPTION_KEY));
    }
    if(savedInstanceState !=null){
      initExoPlayer();
    }

    return view ;
  }
  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
      mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
      mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
    }
  }
  @Override
  public void onSaveInstanceState(Bundle outState) {

    outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
    outState.putLong(STATE_RESUME_POSITION, mResumePosition);
    outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

    super.onSaveInstanceState(outState);
  }

  private void initFullscreenDialog() {

    mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (mExoPlayerFullscreen)
          closeFullscreenDialog();
        super.onBackPressed();
      }
    };
  }
  private void openFullscreenDialog() {

    ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
    mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));

    mExoPlayerFullscreen = true;
    mFullScreenDialog.show();
  }
  private void closeFullscreenDialog() {
    ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
    mFrameLayout.addView(mExoPlayerView);

    mExoPlayerFullscreen = false;
    mFullScreenDialog.dismiss();
    mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
  }
  private void initFullscreenButton() {
    mFullScreenButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!mExoPlayerFullscreen) {
          openFullscreenDialog();
        }else {
          closeFullscreenDialog();
        }
      }
    });
  }

  private void initExoPlayer() {
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    recipePlayer= ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

    mExoPlayerView.setPlayer(recipePlayer);
    boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
    if (haveResumePosition) {
      recipePlayer.seekTo(mResumeWindow, mResumePosition);
    }
    recipePlayer.prepare(mVideoSource);
   recipePlayer.setPlayWhenReady(true);
  }



 public void initRecipeExoPlayer( String videoUrl, String stepDes){
    stepVideoDescUrl=videoUrl;
    stepsComplDescripton=stepDes;
    stepDescription.setText(stepDes);
if(mExoPlayerView.getPlayer()==null){
  mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
      Util.getUserAgent(getContext(), "BakingX"),
      (TransferListener<? super DataSource>) bandwidthMeter);
  mVideoSource = new  ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(videoUrl));
}

   initExoPlayer();
  }

  @Override
  public void onPause() {

    super.onPause();

    if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
      mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
      mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());

      recipePlayer.release();
    }

    if (mFullScreenDialog != null)
      mFullScreenDialog.dismiss();
  }
}
