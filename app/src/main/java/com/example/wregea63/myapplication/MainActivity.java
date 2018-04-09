package com.example.wregea63.myapplication;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
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
    }

    public void selectCard(View v){
        if (fragmentManager.findFragmentByTag("war") == null ) {
            if (selectedCard == null) {
                ((ImageView) v).setColorFilter(Color.argb(100, 0, 255, 255));   //setBackgroundColor(Color.CYAN);
                selectedCard = (ImageView) v;
            } else {
                if ((ImageView) v == selectedCard) {
                    ((ImageView) v).setColorFilter(Color.argb(0, 255, 255, 255));
                    selectedCard = null;
                    TypedArray table = getResources().obtainTypedArray(R.array.fieldSpots);
                    ((ImageView) findViewById(table.getResourceId(tableSpot, 0))).setImageDrawable(((ImageView) v).getDrawable());
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
