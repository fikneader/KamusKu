package com.ngeartstudio.kamus.kamusku;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rvWord;
    private WordAdapter wordAdapter;
    private List<DictionaryModel> dictionaryModelList;
    private DatabaseHelper mDBHelper;

    private static final String SELECTED_ITEM = "arg_selected_item";
    private int mSelectedItem;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        rvWord = (RecyclerView) view.findViewById(R.id.hasilcari);
        rvWord.setLayoutManager(new LinearLayoutManager(getContext()));

//        if (bundle == null){
//            Toast.makeText(view.getContext(), "DATA NULL", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(view.getContext(), "DATA NOT NULL", Toast.LENGTH_SHORT).show();
//            String favorite = getArguments().getString("FAV");
//            if (favorite.equals("TRUE")){
//                Button buttonfav = (Button) view.findViewById(R.id.buttonFav);
//                buttonfav.setBackgroundResource(R.drawable.bookmarkred);
//            }
//        }


        mDBHelper = new DatabaseHelper(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DATABASE_NAME);
        if(database.exists() == false){
            mDBHelper.getReadableDatabase();
            if (copyDatabase(getContext())){
                Toast.makeText(getContext(),"Copy Success",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(),"Copy Failed",Toast.LENGTH_LONG).show();
            }
        }

        //mDBHelper.getReadableDatabase();
        //copyDatabase(getContext());
        //Toast.makeText(getApplicationContext(),"Copy Success",Toast.LENGTH_LONG).show();
        rvWord.setHasFixedSize(true);
        rvWord.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL,10));
        rvWord.setItemAnimator(new DefaultItemAnimator());
        dictionaryModelList = mDBHelper.getListWord("");
        wordAdapter = new WordAdapter();
        wordAdapter.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapter);

        SearchView searchView = (SearchView) view.findViewById(R.id.carikata);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchWord(newText);
                return false;
            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
            Toast.makeText(context, "About Bos",Toast.LENGTH_LONG);
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
        void onFragmentInteraction(Uri uri);
    }

    private void searchWord(String wordSearch){
        dictionaryModelList.clear();
        dictionaryModelList = mDBHelper.getListWord(wordSearch);
        wordAdapter.setData(dictionaryModelList);
        rvWord.setAdapter(wordAdapter);

    }

    private boolean copyDatabase(Context context){
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DATABASE_NAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte [] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("database","Copy Success");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
