package com.ngeartstudio.kamus.kamusku;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


import java.util.List;

public class DefinitionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Boolean isFabOpen = false;
    private FloatingActionButton fabshare,fabadd;
    private FloatingActionMenu fabmenu;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    public List<DictionaryModel> data;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DefinitionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DefinitionFragment newInstance(String param1, String param2) {
        DefinitionFragment fragment = new DefinitionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_definition, container, false);
        final DatabaseHelper db = new DatabaseHelper(getContext());
        db.openDatabase();
        //FloatingActionButton share = (FloatingActionButton) view.findViewById(R.id.share);
        Toolbar toolbar = view.findViewById(R.id.toolbardefinisi);
        toolbar.setNavigationIcon(R.drawable.leftarrow);
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, new HomeFragment()).commit();
            }
            }

        );

        fabmenu = view.findViewById(R.id.fab_menu);
        fabadd = view.findViewById(R.id.fabadd);
        fabshare = view.findViewById(R.id.fabshare);

        //handling menu status (open or close)
        fabmenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    //showToast("Menu is opened");
                } else {
                    //showToast("Menu is closed");
                }
            }
        });


        fabmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabmenu.isOpened()) {
                    fabmenu.close(true);
                }
            }
        });

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_foward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);

        Bundle bundle = getArguments();
        if (bundle == null){
            Toast.makeText(view.getContext(), "DATA NULL", Toast.LENGTH_SHORT).show();
        }
        else {
            //String kata = getActivity().getIntent().getStringExtra("KATA");
            //String pengertian = getActivity().getIntent().getStringExtra("PENGERTIAN");
            String kata = getArguments().getString("KATA");
            String pengertian = getArguments().getString("PENGERTIAN");
            String gambar = getArguments().getString("GAMBAR");
            final String fav = getArguments().getString("FAVORITE");
            final String id = getArguments().getString("ID");
            String url = "http://ngeartstudio.com/kamusku/";
            final TextView kataText = view.findViewById(R.id.kataText);
            final TextView pengertianText = view.findViewById(R.id.pengertianText);
            ImageView gambarPengertian = view.findViewById(R.id.imageDef);

            //Toast.makeText(view.getContext(), "TEST", Toast.LENGTH_SHORT).show();
            if (gambar.equals("NO IMAGE")){
                //gambarPengertian.setBackgroundResource(R.drawable.noimage);
                Log.i("No image","Tidak Ada Gambar");
            } else {
                Picasso.with(getContext())
                        .load(url + gambar)
                        //.placeholder(R.drawable.placeholder)   // optional
                        .placeholder(R.drawable.noimage).error(R.drawable.noimage).fit()      // optional
                        //.resize(400,400)                        // optional
                        .into(gambarPengertian);
            }

            kataText.setText(kata);
            pengertianText.setText(pengertian);
//            share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(view.getContext(), "Share ya",Toast.LENGTH_LONG).show();
//                    String share = kataText.getText().toString() + " adalah " + pengertianText.getText().toString();
//                    Intent sendIntent = new Intent();
//                   sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT,share);
//                    sendIntent.setType("text/plain");
//                    Intent.createChooser(sendIntent,"Share via");
//                    startActivity(sendIntent);
//
//                }
////            });
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    animateFAB();
//                }
//            });
            fabshare.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String share = kataText.getText().toString() + " adalah " + pengertianText.getText().toString();
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT,share);
                                            sendIntent.setType("text/plain");
                                            Intent.createChooser(sendIntent,"Share via");
                                            startActivity(sendIntent);

                                        }
                                    }

            );
            fabadd.setOnClickListener(new View.OnClickListener() {
                //int position;
                //DictionaryModel dictionaryModel = data.get(getId());
                                        @Override
                                        public void onClick(View view) {
                                                //dictionaryModel.setFavorite("TRUE");
                                                db.updateFav(id);
                                                Snackbar.make(view,"Menambahkan ke Kata Favorit",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }

            );
        }
        getActivity().setTitle("Definisi");
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
            Toast.makeText(context, "About Bos",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabadd) {
                    //showToast("Button Add clicked");
                } else if (view == fabshare) {
                    //showToast("Button Delete clicked");
                }
                fabmenu.close(true);
            }
        };
    }

//    public void animateFAB(){
//
//        if(isFabOpen){
//
//            fab.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);
//            isFabOpen = false;
//            Log.d("Raj", "close");
//
//        } else {
//
//            fab.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);
//            isFabOpen = true;
//            Log.d("Raj","open");
//
//        }
//    }
}
