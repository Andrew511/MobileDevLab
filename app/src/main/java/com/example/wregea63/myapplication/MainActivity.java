package com.example.wregea63.myapplication;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements Table.OnFragmentInteractionListener, mathWar.OnFragmentInteractionListener  {

    private ImageView selectedCard;
    private int tableSpot = 0;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment war = new mathWar();
    Fragment table = new Table();
    String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_landscape);
        } else{
            setContentView(R.layout.activity_portrait);
        }

        Table tableFragment = new Table();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentHolder, tableFragment);
        fragmentTransaction.commit();

        TypedArray images = getResources().obtainTypedArray(R.array.cards);
        TypedArray handArray = getResources().obtainTypedArray(R.array.hand);

        for (int i = 0; i < getResources().getInteger(R.integer.handSize); i++) {
            int choice = (int) (Math.random() * images.length());
            int cardChoice = images.getResourceId(choice, R.drawable.c1);
            ImageView cardView = ((ImageView)findViewById(handArray.getResourceId(i, R.id.card1)));
            cardView.setImageResource(cardChoice);
            cardView.setTag(cardChoice);
        }

        //this leaves the keyboard hidden on load
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        playerName = getIntent().getStringExtra("USERNAME");
        ((TextView)findViewById(R.id.player1Name)).setText(playerName);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Object tag;
        ImageView cardView;

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USERNAME", playerName);
        editor.putString("OPPONENTNAME", ((TextView)findViewById(R.id.player2Name)).getText().toString());
        editor.putString("PREVMATH", ((TextView)findViewById(R.id.completedQuestions)).getText().toString());
        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_PORTRAIT) {
            editor.putString("CHATLOG", ((TextView) findViewById(R.id.chatLog)).getText().toString());
        }
        try {
            tag = findViewById(R.id.card1).getTag();
            if (tag != null) {
                editor.putInt("CARD1", (int) tag);
            }
        }
        catch (NullPointerException e) {

        }
        try {
            tag = findViewById(R.id.card2).getTag();
            if (tag != null) {
                editor.putInt("CARD2", (int) tag);
            }
        }
        catch (NullPointerException e){

        }
        try {
            tag = findViewById(R.id.card3).getTag();
            if (tag != null) {
                editor.putInt("CARD3", (int) tag);
            }
        }
        catch (NullPointerException e) {

        }
        editor.commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currCard;
        ImageView cardView;

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        playerName = sharedPref.getString("USERNAME", "Player1");
        ((TextView) findViewById(R.id.completedQuestions)).setText(sharedPref.getString("PREVMATH", ""));
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            ((TextView) findViewById(R.id.chatLog)).setText(sharedPref.getString("CHATLOG", ""));
        }
        currCard = sharedPref.getInt("CARD1", -1);
        cardView = ((ImageView)findViewById(R.id.card1));
        if (currCard != -1) {
            cardView.setImageResource(currCard);
            cardView.setTag(currCard);
        }
        else {
            ((LinearLayout)cardView.getParent()).removeView(cardView);
        }
        currCard = sharedPref.getInt("CARD2", -1);
        cardView = ((ImageView)findViewById(R.id.card1));
        if (currCard != -1) {
            cardView.setImageResource(currCard);
            cardView.setTag(currCard);
        }
        else {
            ((LinearLayout)cardView.getParent()).removeView(cardView);
        }
        currCard = sharedPref.getInt("CARD3", -1);
        cardView = ((ImageView)findViewById(R.id.card1));
        if (currCard != -1) {
            cardView.setImageResource(currCard);
            cardView.setTag(currCard);
        }
        else {
            ((LinearLayout)cardView.getParent()).removeView(cardView);
        }

    }

    public void setPlayerCard(int cardId) {
        ((ImageView)findViewById(R.id.fieldCard1)).setImageDrawable(getDrawable(cardId));

    }

    public void setOpponentCard(int cardId) {
        ((ImageView)findViewById(R.id.fieldCard2)).setImageDrawable(getDrawable(cardId));

    }

    public void sendChat(View v){
        String value = ((EditText)findViewById(R.id.chatMessage)).getText().toString();
        ((TextView)findViewById(R.id.chatLog)).setText(playerName + value + "\n" + ((TextView)findViewById(R.id.chatLog)).getText());
        ((EditText)findViewById(R.id.chatMessage)).setText("");
        if (value.equals("math") && fragmentManager.findFragmentByTag("war") == null ) {
            android.support.v4.app.FragmentTransaction replaceTableWithWar = fragmentManager.beginTransaction();
            replaceTableWithWar.replace(R.id.fragmentHolder, war, "war");
            replaceTableWithWar.addToBackStack("TableReplaced");
            replaceTableWithWar.commit();
        }
        else if (value.equals("table") && fragmentManager.findFragmentByTag("table") == null ) {
            android.support.v4.app.FragmentTransaction replaceTableWithWar = fragmentManager.beginTransaction();
            replaceTableWithWar.replace(R.id.fragmentHolder, table, "table");
            replaceTableWithWar.addToBackStack("TableReplaced");
            replaceTableWithWar.commit();
        }
    }

    public void selectCard(View v){
        if (fragmentManager.findFragmentByTag("war") == null ) {
            if (selectedCard == null) {
                ((ImageView) v).setColorFilter(Color.argb(100, 0, 255, 255));   //setBackgroundColor(Color.CYAN);
                selectedCard = (ImageView) v;
            } else {
                if ((ImageView) v == selectedCard && tableSpot < 2) {
                    ((ImageView) v).setColorFilter(Color.argb(0, 255, 255, 255));
                    selectedCard = null;
                    TypedArray table = getResources().obtainTypedArray(R.array.fieldSpots);
                    ((ImageView) findViewById(table.getResourceId(tableSpot, 0))).setImageResource((int)v.getTag());
                    ((ImageView) findViewById(table.getResourceId(tableSpot, 0))).setTag(v.getTag());
                    ((ImageView) v).setTag(null);
                    ((LinearLayout) v.getParent()).removeView(v);
                    tableSpot++;
                } else {
                    selectedCard.setColorFilter(Color.argb(0, 255, 255, 255));
                    ((ImageView) v).setColorFilter(Color.argb(100, 0, 255, 255));
                    selectedCard = (ImageView) v;
                }
            }
        }
    }

    public void sendScore(String answer, int score) {
        TextView playerScore = ((TextView)findViewById(R.id.player2Score));
        playerScore.setText((Integer.parseInt((String)playerScore.getText()) + score) + "" );
        ((TextView)findViewById(R.id.completedQuestions)).setText(answer + "\n" + ((TextView)findViewById(R.id.completedQuestions)).getText());
    }

    public void submitAnswer(View v) {
        if (fragmentManager.findFragmentByTag("war") != null) {
            ((mathWar)fragmentManager.findFragmentByTag("war")).submitAnswer(v);
        }
    }
}
