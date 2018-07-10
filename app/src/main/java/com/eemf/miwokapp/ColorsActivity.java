package com.eemf.miwokapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    //declare the {@link onCompletionListener} object to avoid creating new listener object every time to save memory space
    private MediaPlayer.OnCompletionListener mMediaPlayerCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releasemMediaPlayer();
        }
    };

    //declare the {@link onAudioFocusChangeListener} implemented Interface object that defines the onFocusChange callback method
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT){
                mMediaPlayer.start();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                mMediaPlayer.stop();
                releasemMediaPlayer();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //call this method to populate the rootView LinearLayout
        populateRootView();

        //initialize the {@link AudioManager} instance
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * this method is called to populate the view with the words
     */
    private void populateRootView() {

        //creating an ArrayList of numbers' English words
        final ArrayList<Word> colorWord = new ArrayList<>();
        colorWord.add(new Word("weṭeṭṭi","red", R.drawable.color_red, R.raw.color_red));
        colorWord.add(new Word("chokokki","green", R.drawable.color_green, R.raw.color_green));
        colorWord.add(new Word("ṭakaakki","brown", R.drawable.color_brown, R.raw.color_brown));
        colorWord.add(new Word("ṭopoppi","gray", R.drawable.color_gray, R.raw.color_gray));
        colorWord.add(new Word("kululli","black", R.drawable.color_black, R.raw.color_black));
        colorWord.add(new Word("kelelli","white", R.drawable.color_white, R.raw.color_white));
        colorWord.add(new Word("ṭopiisә","dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorWord.add(new Word("chiwiiṭә","mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        //create the {@link WordAdapter} using the numberWord {@link ArrayList}
        WordAdapter adapter = new WordAdapter(this, colorWord, R.color.colorCategoryColors);

        //get the {@link ListView} of the Numbers {"link Activity}
        ListView rootView = (ListView) findViewById(R.id.numbersRootView);

        //attach the {@link ArrayAdapter} to the {@link ListView}
        rootView.setAdapter(adapter);

        //attach an onItemClickListener to the list item
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //store the clicked list item view's vocabulary content {@Link Word} in a Word object type variable
                Word currentWord = colorWord.get(i);

                //release resources from the Media Player should another Media Player object is initialized before the completion
                //of the current sound being played - thereby causing the onClickListener callback not to be executed
                releasemMediaPlayer();

                int requestStatus = mAudioManager.requestAudioFocus(
                        //OnAudioFocusChangeListener Interface implemented object that implements the onAudioFocusChange callback method
                        // that is called when the AudioFocus changes
                        mOnAudioFocusChangeListener,

                        //the main audio STREAM_TYPE affected by the AudioFocus request
                        AudioManager.STREAM_MUSIC,

                        //the duration of the AudioFocus usage/holding
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(requestStatus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //initialize the MediaPlayer object variable
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, currentWord.getmSoundId());

                    //start the playback
                    mMediaPlayer.start();

                    //release resources from the MediaPlayer after the current playback sound has successfully completed
                    //@mMediaPlayerCompletion variable is used to avoid creating new anonymous class whenever the resources need be released
                    mMediaPlayer.setOnCompletionListener(mMediaPlayerCompletion);
                }
            }
        });
    }

    /**
     * releasemMediaPlayer() frees the no-longer-needed memory resource used by the MediaPlayer
     */
    public void releasemMediaPlayer(){
        //check if the object variable is initialized
        if(mMediaPlayer != null){
            //release the audio file resource
            mMediaPlayer.release();

            //release the object variable resource
            mMediaPlayer = null;

            //abandon the AudioFocus
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }

    }

    /**
     * overridden to release Media Resources before hiding the activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        releasemMediaPlayer();
    }
}
