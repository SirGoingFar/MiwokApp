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

public class PhrasesActivity extends AppCompatActivity {

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
        final ArrayList<Word> phraseWord = new ArrayList<>();
        phraseWord.add(new Word("minto wuksus","Where are you going?",R.raw.phrase_where_are_you_going));
        phraseWord.add(new Word("tinnә oyaase'nә","What is your name?",R.raw.phrase_what_is_your_name));
        phraseWord.add(new Word("oyaaset...","My name is...",R.raw.phrase_my_name_is));
        phraseWord.add(new Word("michәksәs?","How are you feeling?",R.raw.phrase_how_are_you_feeling));
        phraseWord.add(new Word("kuchi achit","I’m feeling good.",R.raw.phrase_im_feeling_good));
        phraseWord.add(new Word(" әәnәs'aa?","Are you coming?",R.raw.phrase_are_you_coming));
        phraseWord.add(new Word("hәә’ әәnәm","Yes, I’m coming.",R.raw.phrase_yes_im_coming));
        phraseWord.add(new Word("әәnәm","I’m coming.",R.raw.phrase_im_coming));
        phraseWord.add(new Word("yoowutis","Let’s go.",R.raw.phrase_lets_go));
        phraseWord.add(new Word("әnni'nem","Come here.",R.raw.phrase_come_here));

        //create the {@link WordAdapter} using the numberWord {@link ArrayList}
        WordAdapter adapter = new WordAdapter(this, phraseWord, R.color.colorCategoryPhrases);

        //get the {@link ListView} of the Numbers {"link Activity}
        ListView rootView = (ListView) findViewById(R.id.numbersRootView);

        //attach the {@link ArrayAdapter} to the {@link ListView}
        rootView.setAdapter(adapter);

        //attach an onItemClickListener to the list item
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //store the clicked list item view's vocabulary content {@Link Word} in a Word object type variable
                Word currentWord = phraseWord.get(i);

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
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, currentWord.getmSoundId());

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
