package com.example.wregea63.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mathWar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mathWar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mathWar extends Fragment {

    private int answer;
    private String expression;
    private OnFragmentInteractionListener mListener;

    public mathWar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mathWar.
     */
    // TODO: Rename and change types and number of parameters
    public static mathWar newInstance(String param1, String param2) {
        mathWar fragment = new mathWar();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        generateProblem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_math_war, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void submitAnswer(View view) {
        if (mListener != null) {
            String attempt = ((EditText)getActivity().findViewById(R.id.mathWarAnswer)).getText().toString();
            if (testCorrect(attempt)) {
                mListener.sendScore(expression + answer, 1);
                generateProblem();
            }
            ((EditText)getActivity().findViewById(R.id.mathWarAnswer)).setText("");
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
        void sendScore(String Answer, int points);
    }

    private void generateProblem() {

        int questionType = (int) (Math.random() * 3);
        int argument1 = (int)(Math.random() * 16);
        int argument2 = (int)(Math.random() * 16);
        while (argument2 > argument1 && questionType == 1) { //make sure subtractions don't result in negative numbers
            argument2 = (int) (Math.random() * 16);
        }

        if (questionType == 0) {
            answer = argument1 + argument2;
            expression = argument1 + " + " + argument2 + " = ";
        }
        else if (questionType == 1) {
            answer = argument1 - argument2;
            expression = argument1 + " - " + argument2 + " = ";
        }
        else if (questionType == 2) {
            answer = argument1 * argument2;
            expression = argument1 + " * " + argument2 + " = ";
        }
        ((TextView)getActivity().findViewById(R.id.mathWarQuestion)).setText(expression);
    }

    public boolean testCorrect(String attempt) {
        boolean correct = false;
        if (Integer.parseInt(attempt) == answer) {
            correct = true;
        }
        return correct;
    }


}
