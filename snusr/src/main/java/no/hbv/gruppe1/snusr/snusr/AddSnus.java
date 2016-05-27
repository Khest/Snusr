package no.hbv.gruppe1.snusr.snusr;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSnus.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSnus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSnus extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RatingBar rating;
    ImageView snusImage;
    EditText innName, innManu;
    Button btnAdd, btnAddSnusImage;
    Spinner spinSweet, spinSalt, spinTaste, spinStr;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSnus.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSnus newInstance(String param1, String param2) {
        AddSnus fragment = new AddSnus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddSnus() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_add_snus, container, false);
        snusImage = (ImageView) v.findViewById(R.id.addSnus_imgview);
        rating = (RatingBar) v.findViewById(R.id.ratingbar_addsnus);
        innName = (EditText) v.findViewById(R.id.etxt_name);
        innManu = (EditText) v.findViewById(R.id.etxt_manu);
        btnAdd = (Button) v.findViewById(R.id.btn_addsnus);
        btnAddSnusImage = (Button) v.findViewById(R.id.btn_addSnusImage);
        spinSalt = (Spinner) v.findViewById(R.id.spin_salt);
        spinSweet = (Spinner) v.findViewById(R.id.spin_sweet);
        spinStr = (Spinner) v.findViewById(R.id.spin_str);
        spinTaste = (Spinner) v.findViewById(R.id.spin_taste);

        return v;
    }

    public void addSnus(){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
