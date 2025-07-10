package com.example.app;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        // Back Button Setup
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        // Video Setup
        setupHeroVideo();

        // Set the text of the About Us section
        TextView aboutUsTextView = findViewById(R.id.aboutUsTextView);
        String aboutUsContent = "Welcome to LuxeVista Resort, an exclusive beachfront haven where luxury meets tranquility. "
                + "Nestled on the serene coastline, LuxeVista Resort offers guests an unparalleled experience, combining premium "
                + "accommodations, exceptional services, and breathtaking ocean views. Whether you're seeking a relaxing retreat, "
                + "a romantic getaway, or an adventure-filled vacation, our resort is designed to cater to your every need.\n\n"
                + "At LuxeVista, we believe in creating lasting memories by offering a seamless and personalized experience. "
                + "From our elegantly designed rooms and suites with stunning ocean views to our world-class spa and fine dining options, "
                + "every detail is crafted with your comfort and relaxation in mind.\n\n"
                + "Our all-in-one mobile app provides you with a convenient and intuitive platform to enhance your stay. Browse through "
                + "our curated selection of rooms, book luxurious spa treatments, reserve private dining experiences, and explore exciting "
                + "local attractions—all at the touch of a button. With personalized recommendations based on your preferences and booking "
                + "history, LuxeVista's app ensures that every moment of your stay is tailored to your desires.\n\n"
                + "Whether you're unwinding at the pool, indulging in a spa treatment, or enjoying a guided beach tour, LuxeVista Resort "
                + "is dedicated to offering you a premier luxury experience that goes beyond expectations. Come, discover a place where "
                + "every detail matters, and let us make your stay extraordinary.\n\n"
                + "LuxeVista Resort — Your gateway to the perfect beachfront escape.";

        aboutUsTextView.setText(aboutUsContent);
    }

    private void setupHeroVideo() {
        VideoView videoView = findViewById(R.id.heroVideoView);
        View fallbackBackground = findViewById(R.id.heroBackground);

        try {
            // Replace "hotel_video" with your actual video filename (without extension)
            // Make sure your video file is in app/src/main/res/raw/ folder
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hotel_video);
            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true); // Loop the video continuously
                    mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    // Hide fallback background once video is ready
                    fallbackBackground.setVisibility(View.GONE);
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Show fallback background if video fails to load
                    fallbackBackground.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    return true;
                }
            });

            // Start playing the video
            videoView.start();

        } catch (Exception e) {
            // Show fallback background if something goes wrong
            fallbackBackground.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause video when activity is paused
        VideoView videoView = findViewById(R.id.heroVideoView);
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume video when activity is resumed
        VideoView videoView = findViewById(R.id.heroVideoView);
        if (videoView != null) {
            videoView.start();
        }
    }
}