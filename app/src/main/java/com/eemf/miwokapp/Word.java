package com.eemf.miwokapp;

/**
 * {@link Word} is a vocabulary a learner learns
 * It consists of both th default (English) word and the Miwok translation word
 */

public class Word {
    private String mMiwokTranslation;
    private String mDefaultTranslation;
    private int mSoundId;
    private int mImageResourceId = NO_IMAGE_ID;
    private static final int NO_IMAGE_ID = -1;

    /**
     * constructor method
     * @param miwokTranslation is the Miwok word
     * @param defaultTranslation is the default translation word
     */
    public Word(String miwokTranslation, String defaultTranslation, int soundId){
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mSoundId = soundId;

    }

    /**
     * constructor method
     * @param miwokTranslation is the Miwok word
     * @param defaultTranslation is the default translation word
     * @param imageId is the corresponding image resource Id for the current vocabulary {@link Word} object
     */
    public Word(String miwokTranslation, String defaultTranslation, int imageId, int soundId){
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mImageResourceId = imageId;
        mSoundId = soundId;
    }

    /**
     * @return the Miwok translation word
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * @return the default translation word
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * @return the vocabulary corresponding picture
     */
    public int getmImageResourceId(){
        return mImageResourceId;
    }

    /**
     * @return true if no image is attached to the {@link Word} object at instantiation
     */
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_ID;
    }

    /**
     * @return the vocabulary corresponding pronunciation
     */
    public int getmSoundId(){
        return mSoundId;
    }
}
