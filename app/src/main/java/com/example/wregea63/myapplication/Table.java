package com.example.wregea63.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Table.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Table#} factory method to
 * create an instance of this fragment.
 */
public class Table extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Table() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int currCard;
        ImageView cardView;

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Object tag = getActivity().findViewById(R.id.fieldCard1).getTag();
        if (tag != null) {
            editor.putInt("FIELDCARD1", (int) tag);
        }
        tag = getActivity().findViewById(R.id.fieldCard2).getTag();
        if (tag != null) {
            editor.putInt("FIELDCARD2", (int) tag);
        }

        editor.commit();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        int currCard;
        ImageView cardView;

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


        currCard = sharedPref.getInt("FIELDCARD1", -1);
        cardView = ((ImageView)getActivity().findViewById(R.id.fieldCard1));
        if (currCard != -1) {
            cardView.setImageResource(currCard);
            cardView.setTag(currCard);
        }
        currCard = sharedPref.getInt("FIELDCARD2", -1);
        cardView = ((ImageView)getActivity().findViewById(R.id.fieldCard2));
        if (currCard != -1) {
            cardView.setImageResource(currCard);
            cardView.setTag(currCard);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setPlayerCard(int cardId);
        void setOpponentCard(int cardId);
    }
}
