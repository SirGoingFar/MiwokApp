package com.eemf.miwokapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by OLANREWAJU  E A on 31/01/2018.
 */
public class WordAdapter extends ArrayAdapter<Word> {

    int mColorID;

    public WordAdapter(Activity activity, ArrayList<Word> word, int colorID) {
        super(activity, 0, word);
        mColorID = colorID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View childView = convertView;

        if(childView == null){
            childView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        Word currentWordObject = getItem(position);

        //set Miwok Translation TextView012918554
        TextView miwokTranslationTextView = childView.findViewById(R.id.miwok);
        miwokTranslationTextView.setText(currentWordObject.getMiwokTranslation());

        //set English (Default Translation) TextView
        TextView defaultTranslation = childView.findViewById(R.id.english);
        defaultTranslation.setText(currentWordObject.getDefaultTranslation());

        //set corresponding vocabulary image
        ImageView vocImage = (ImageView) childView.findViewById(R.id.vocImage);

        if(currentWordObject.hasImage()) {
            vocImage.setImageResource(currentWordObject.getmImageResourceId());
            vocImage.setVisibility(View.VISIBLE);
        }else{
            vocImage.setVisibility(View.GONE);
        }

        View textviewBackground = childView.findViewById(R.id.word_textview_background);
        int color = ContextCompat.getColor(getContext(),mColorID);
        textviewBackground.setBackgroundColor(color);


        //return the AdapterView item view
        return childView;
    }

}
