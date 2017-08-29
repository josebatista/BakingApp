package com.example.jpereira.bakingapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jpereira.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.jpereira.bakingapp.domain.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * Created by jpereira on 23/08/17.
 */

public class RecipeStepDetailFragment extends Fragment {

    private static final String STEP = "step";
    private static final String POSITION_VIDEO = "position_video";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String CURRENT_WINDOW = "current_window";

    private View viewRoot;
    private Step mStep;
    private FragmentStepDetailBinding mBinding;
    private SimpleExoPlayer mPlayer;
    private boolean mPlayWhenReady = true;
    private int mCurrentWindow;
    private long mCurrentVideoPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (viewRoot == null) {
            viewRoot = inflater.inflate(R.layout.fragment_step_detail, container, false);
        }
        mBinding = DataBindingUtil.bind(viewRoot);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STEP)) {
                mStep = savedInstanceState.getParcelable(STEP);
            }
            if (savedInstanceState.containsKey(CURRENT_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            }
            if (savedInstanceState.containsKey(POSITION_VIDEO)) {
                mCurrentVideoPosition = savedInstanceState.getLong(POSITION_VIDEO);
            }
            if (savedInstanceState.containsKey(PLAY_WHEN_READY)) {
                mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            }
        }

        if (mStep != null) {
            mBinding.fragmentStepDetailTitle.setText(mStep.getShortDescription());
            mBinding.fragmentStepDetailDescription.setText(mStep.getDescription());
            if (mStep.getVideoURL().isEmpty()) {
                mBinding.fragmentStepDetailPlayerView.setVisibility(View.GONE);
            }
            if (!mStep.getThumbnailURL().isEmpty()) {
                Picasso.with(getContext()).load(mStep.getThumbnailURL())
                        .into(mBinding.fragmentStepDetailIv);
            } else {
                mBinding.fragmentStepDetailIv.setVisibility(View.GONE);
            }
        }
        return viewRoot;
    }

    public void setStep(Step mStep) {
        this.mStep = mStep;
    }

    private void initializePlayer(String url) {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mBinding.fragmentStepDetailPlayerView.setPlayer(mPlayer);

            mPlayer.setPlayWhenReady(mPlayWhenReady);

            Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(uri);
            boolean haveResumePosition = mCurrentWindow != C.INDEX_UNSET;
            mPlayer.prepare(mediaSource, !haveResumePosition, false);
            if (haveResumePosition) {
                mPlayer.seekTo(mCurrentWindow, mCurrentVideoPosition);
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getContext(), getString(R.string.app_name)),
                new DefaultExtractorsFactory(), null, null);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBinding.fragmentStepDetailIv.setVisibility(View.GONE);
            mBinding.fragmentStepDetailTitle.setVisibility(View.GONE);
            mBinding.fragmentStepDetailDescription.setVisibility(View.GONE);

            mBinding.fragmentStepDetailPlayerView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mCurrentVideoPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP, mStep);
        outState.putLong(POSITION_VIDEO, Math.max(0, mCurrentVideoPosition));
        outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (!mStep.getVideoURL().isEmpty()) {
                //init player
                initializePlayer(mStep.getVideoURL());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            if (!mStep.getVideoURL().isEmpty()) {
                //init player
                initializePlayer(mStep.getVideoURL());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
