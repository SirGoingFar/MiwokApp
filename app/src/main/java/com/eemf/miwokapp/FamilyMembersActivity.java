package com.eemf.miwokapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyMembersActivity extends AppCompatActivity {

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
        final ArrayList<Word> familyWord = new ArrayList<>();
        familyWord.add(new Word("әpә","father", R.drawable.family_father, R.raw.family_father));
        familyWord.add(new Word("әṭa","mother", R.drawable.family_mother, R.raw.family_mother));
        familyWord.add(new Word("angsi","son", R.drawable.family_son, R.raw.family_son));
        familyWord.add(new Word("tune","daughter", R.drawable.family_daughter, R.raw.family_daughter));
        familyWord.add(new Word("taachi","older brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        familyWord.add(new Word("chalitti","younger brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyWord.add(new Word("teṭe","older sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        familyWord.add(new Word("kolliti","younger sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyWord.add(new Word("ama","grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));
        familyWord.add(new Word("paapa","grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));

        //create the {@link WordAdapter} using the numberWord {@link ArrayList}
        WordAdapter adapter = new WordAdapter(this, familyWord, R.color.colorCategoryFamilyMembers);

        //get the {@link ListView} of the Numbers {"link Activity}
        ListView rootView = (ListView) findViewById(R.id.numbersRootView);

        //attach the {@link ArrayAdapter} to the {@link ListView}
        rootView.setAdapter(adapter);

        //attach an onItemClickListener to the list item
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //store the clicked list item view's vocabulary content {@Link Word} in a Word object type variable
                Word currentWord = familyWord.get(i);

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
                    mMediaPlayer = MediaPlayer.create(FamilyMembersActivity.this, currentWord.getmSoundId());

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
