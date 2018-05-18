package elmajdma.bakingx.videoPlayer;


import static elmajdma.bakingx.RecipeDetailsActivity.STEP_DESCRIPTION_KEY;
import static elmajdma.bakingx.RecipeDetailsActivity.VIDEO_URL_KEY;

import android.media.MediaDataSource;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import elmajdma.bakingx.R;

//public class RecipeStepsVideoPlayerFragment extends
 //   Fragment implements Player.EventListener {
  public class RecipeStepsVideoPlayerFragment extends
      Fragment  {
  @BindView(R.id.recipe_playerView)
  PlayerView mPlayerView;
  @BindView(R.id.tv_steps_description)
  TextView stepDescription;
  // Saved instance state keys.
  private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
  private static final String KEY_WINDOW = "window";
  private static final String KEY_POSITION = "position";
  private static final String KEY_AUTO_PLAY = "auto_play";

  private static final String TAG = RecipeStepsVideoPlayerFragment.class.getSimpleName();
  private SimpleExoPlayer mExoPlayer;
  private MediaSessionCompat mMediaSession;
  private PlaybackStateCompat.Builder mStateBuilder;

  private boolean startAutoPlay;
  private int startWindow;
  private Long startPosition;
  private boolean playWhenReady;

  private DataSource.Factory mediaDataSourceFactory;
  private MediaSource mediaSource;
  private DefaultTrackSelector trackSelector;
  private DefaultTrackSelector.Parameters trackSelectorParameters;
  private DebugTextViewHelper debugViewHelper;
  private String videoUrl;







  public RecipeStepsVideoPlayerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_recipe_steps_video_player, container, false);
    ButterKnife.bind(this, view);
    Bundle bundle=getArguments();
    if(bundle!=null){
      videoUrl=bundle.getString(VIDEO_URL_KEY);
      }
    initializePlayer(videoUrl);
    stepDescription.setText(bundle.getString(STEP_DESCRIPTION_KEY));
  return view;
  }
  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
      startWindow = savedInstanceState.getInt(KEY_WINDOW);
      startPosition = savedInstanceState.getLong(KEY_POSITION);
      Toast.makeText(getContext(),startPosition.toString(), Toast.LENGTH_LONG).show();
    }
  }
  @Override
  public void onSaveInstanceState(Bundle outState) {
releasePlayer();
    outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
    outState.putInt(KEY_WINDOW, startWindow);
    outState.putLong(KEY_POSITION, startPosition);
  }


  public void initializePlayer(String mediaUri) {
           if (mExoPlayer == null) {
             BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
             TrackSelection.Factory videoTrackSelectionFactory =
                 new AdaptiveTrackSelection.Factory(bandwidthMeter);
             trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                 mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                 mPlayerView.setPlayer(mExoPlayer);
              mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                 Util.getUserAgent(getContext(), "BakingX"),
                 (TransferListener<? super DataSource>) bandwidthMeter);

                    mediaSource = new  ExtractorMediaSource
                        .Factory(mediaDataSourceFactory)
                        .createMediaSource(Uri.parse(mediaUri));
             if (startPosition!=null) {
               mExoPlayer.seekTo(startWindow,startPosition);
             }

                 mExoPlayer.prepare(mediaSource);
                 mExoPlayer.setPlayWhenReady(playWhenReady);

            }

     }

 private void releasePlayer() {
   if (mExoPlayer!= null) {
     startPosition = mExoPlayer.getCurrentPosition();
     startWindow = mExoPlayer.getCurrentWindowIndex();
     playWhenReady = mExoPlayer.getPlayWhenReady();
     mExoPlayer.release();
     mExoPlayer = null;
   }
 }


  private void updateStartPosition() {
    if (mExoPlayer != null) {
      startAutoPlay = mExoPlayer.getPlayWhenReady();
      startWindow = mExoPlayer.getCurrentWindowIndex();
      startPosition = Math.max(0, mExoPlayer.getContentPosition());
    }
  }

  @Override
  public void onStart() {
    super.onStart();
   if (Util.SDK_INT > 23) {
      initializePlayer(videoUrl);
   }
  }
  @Override
  public void onResume() {
    super.onResume();
  if (Util.SDK_INT <= 23 || mExoPlayer == null) {
      initializePlayer(videoUrl);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    updateStartPosition();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
   // releasePlayer();
   if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    releasePlayer();
  }

}
