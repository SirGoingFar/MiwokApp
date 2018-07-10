package com.eemf.miwokapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add onClickListener to @NumbersTextView to display @link NumbersActivity
        TextView numbers = (TextView) findViewById(R.id.numbers);
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent numbersActivitiyIntent = new Intent(view.getContext(), NumbersActivity.class);
                startActivity(numbersActivitiyIntent);
            }
        });

        //add onClickListener to @FamilyMembersTextView to display @link FamilyMembersActivity
        TextView familyMembers = (TextView) findViewById(R.id.familyMembers);
        familyMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent familyMembersActivitiyIntent = new Intent(view.getContext(), FamilyMembersActivity.class);
                startActivity(familyMembersActivitiyIntent);
            }
        });

        //add onClickListener to @ColorsTextView to display @link ColorsActivity
        TextView colors = (TextView) findViewById(R.id.colors);
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent colorsActivitiyIntent = new Intent(view.getContext(), ColorsActivity.class);
                startActivity(colorsActivitiyIntent);
            }
        });

        //add onClickListener to @FamilyMembersTextView to display @link FamilyMembersActivity
        TextView phrases = (TextView) findViewById(R.id.phrases);
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phrasesActivitiyIntent = new Intent(view.getContext(), PhrasesActivity.class);
                startActivity(phrasesActivitiyIntent);
            }
        });
    }
}