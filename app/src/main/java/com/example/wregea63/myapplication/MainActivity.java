package com.example.wregea63.myapplication;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView selectedCard;
    private int tableSpot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currLay;
        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape);
            currLay = R.layout.activity_landscape;
            /*Button button = (Button) findViewById(R.id.chatSend);
            final TextView chatLog = (TextView) findViewById(R.id.chatLog);
            final EditText chatMessage = (EditText) findViewById(R.id.chatMessage);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String text =  chatMessage.getText() + "/n" + chatLog.getText();
                    chatLog.setText(text);
                }
            }); */
        } else{
            setContentView(R.layout.activity_portrait);
            currLay = R.layout.activity_portrait;
        }

        TypedArray images = getResources().obtainTypedArray(R.array.cards);
        TypedArray handArray = getResources().obtainTypedArray(R.array.hand);
        (findViewById(R.id.mathWarSubmit)).setEnabled(false);

        for (int i = 0; i < getResources().getInteger(R.integer.handSize); i++) {
            int choice = (int) (Math.random() * images.length());
            int cardChoice = images.getResourceId(choice, R.drawable.c1);
            ImageView cardView = ((ImageView)findViewById(handArray.getResourceId(i, R.id.card1)));
            cardView.setImageResource(cardChoice);
            cardView.setTag(cardChoice);
            /*cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add your code in here!
                    if (v.isSelected()) {
                        if (findViewById(R.id.fieldCard1).getTag() != null) {
                            ((ImageView)findViewById(R.id.fieldCard1)).setImageResource((int)v.getTag());
                            (findViewById(R.id.fieldCard1)).setTag(v.getTag());
                            v.setVisibility(View.GONE);
                        }
                        else if (findViewById(R.id.fieldCard2).getTag() != null) {
                            ((ImageView)findViewById(R.id.fieldCard2)).setImageResource((int)v.getTag());
                            (findViewById(R.id.fieldCard2)).setTag(v.getTag());
                            v.setVisibility(View.GONE);
                        }
                    }
                }
            });*/
        }

        //images.recycle();
        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    public void sendChat(View v){
        String value = ((EditText)findViewById(R.id.chatMessage)).getText().toString();
        ((TextView)findViewById(R.id.chatLog)).setText("Name: " + value+ "\n" +((TextView)findViewById(R.id.chatLog)).getText());
        ((EditText)findViewById(R.id.chatMessage)).setText("");
    }

    public void selectCard(View v){
        if(selectedCard==null){
            ((ImageView)v).setColorFilter(Color.argb(100, 0, 255, 255));   //setBackgroundColor(Color.CYAN);
            selectedCard = (ImageView)v;
        } else {
            if((ImageView)v == selectedCard){
                ((ImageView)v).setColorFilter(Color.argb(0, 255, 255, 255));
                selectedCard = null;
                TypedArray table = getResources().obtainTypedArray(R.array.fieldSpots);
                ((ImageView)findViewById(table.getResourceId(tableSpot, 0))).setImageDrawable(((ImageView) v).getDrawable());
                ((LinearLayout)v.getParent()).removeView(v);
                tableSpot++;
            } else{
                selectedCard.setColorFilter(Color.argb(0, 255, 255, 255));
                ((ImageView)v).setColorFilter(Color.argb(100, 0, 255, 255));
                selectedCard = (ImageView)v;
            }
        }
    }
}
