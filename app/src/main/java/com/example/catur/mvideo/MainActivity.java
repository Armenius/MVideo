package com.example.catur.mvideo;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{

    private static String API_KEY = "46007872";
    private static String SESSION_ID = "1_MX40NjAwNzg3Mn5-MTUxMjI5MzY5ODE0MX5MSTVHTStmZHNPN1FkU29KYnAzR2R6N2l-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjAwNzg3MiZzaWc9ODFiODU0ZGNiMTAyMDkzMTQ3YjI4YjE0Y2Q0YTFkMjg5NjU3ZjQwYTpzZXNzaW9uX2lkPTFfTVg0ME5qQXdOemczTW41LU1UVXhNakk1TXpZNU9ERTBNWDVNU1RWSFRTdG1aSE5QTjFGa1UyOUtZbkF6UjJSNk4ybC1mZyZjcmVhdGVfdGltZT0xNTEyMjkzNzI0Jm5vbmNlPTAuMjAyNjUyMDAwMDgxMjk0MzYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTUxNDg4NTcyMSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session session;

    private FrameLayout PublisherContainer;
    private FrameLayout SubscriberContainer;

    private Publisher publisher;
    private Subscriber subscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        PublisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        SubscriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_SETTINGS_SCREEN_PERM)
    private void requestPermissions(){
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perm)){

            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }
        else{
            EasyPermissions.requestPermissions(this, "This app needs to access to your camera and mic", RC_SETTINGS_SCREEN_PERM, perm);
        }
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if(subscriber == null){
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);

            SubscriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if(subscriber != null){
            subscriber = null;
            SubscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
