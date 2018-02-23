package com.thrillbox.thrillboxsdktest;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.widget.Toast;

import com.thrillbox.frameworktb.MDSettings;
import com.thrillbox.frameworktb.MDVRLibrary;
import com.thrillbox.frameworktb.common.TBApplicationType;
import com.thrillbox.frameworktb.jobs.TBQueue;
import com.thrillbox.frameworktb.model.TBVideoAsset;
import com.thrillbox.frameworktb.services.TBGPSTracker;
import com.thrillbox.frameworktb.strategy.TBOperationList;

public class MainActivity extends AppCompatActivity {

    private MDVRLibrary mVRLibrary;
    private TBGPSTracker mTBGPSTracker;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TBVideoAsset tbVideoAsset = getVideoAsset();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.init(this);
        mMediaPlayer.openRemoteFile(tbVideoAsset.getUrl());

        mVRLibrary = MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_MONO)
                .appType(TBApplicationType.STANDALONE)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .packageName(getPackageName())
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayer.getPlayer().setVideoSurface(surface);
                    }
                })
                .player(mMediaPlayer.getPlayer())
                .projectionMode(tbVideoAsset.getUrl().contains("m3u8") ? MDVRLibrary.PROJECTION_MODE_PLANE_FULL
                        : MDVRLibrary.PROJECTION_MODE_SPHERE)
                .motionDelay(SensorManager.SENSOR_DELAY_GAME)
                .videoAsset(tbVideoAsset)
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        if (mode == MDVRLibrary.INTERACTIVE_MODE_MOTION) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "This phone does not have gyroscope", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .build(R.id.surface_view);

        if (MDSettings.isLocationEnabled(this))
            mTBGPSTracker = new TBGPSTracker(this);

        mMediaPlayer.play();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mVRLibrary.onDestroy();
        if (mTBGPSTracker != null)
            mTBGPSTracker.stopUsingGPS();
        super.onDestroy();
    }

    private TBVideoAsset getVideoAsset() {
        TBVideoAsset tbVideoAsset = new TBVideoAsset();
        tbVideoAsset.setId("5a6560c646e6dafad67b473d");
        tbVideoAsset.setOrganizationId("59b5dc0e7321b22e99e1e3fb");
        tbVideoAsset.setUrl("https://s3.amazonaws.com/thrillbox-ci/assets/sipsip/SipSip.mp4");
        tbVideoAsset.setName("Sip Sip - Muscle Shuffle");
        tbVideoAsset.setOrder(3);
        tbVideoAsset.setDescription("Thrillbox Secret Sessions 2015");
        tbVideoAsset.setDownload(false);
        tbVideoAsset.setDownloading(false);
        tbVideoAsset.setPaused(false);
        tbVideoAsset.setWatched(false);
        tbVideoAsset.setThumbnail("");
        return tbVideoAsset;
    }
}
