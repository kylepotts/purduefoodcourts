package com.kptech.purduefoodcourts.app.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kptech.purduefoodcourts.app.R;
import com.kptech.purduefoodcourts.app.cards.CourtGridCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */


public class CourtGridFragment extends BaseFragment {
    public static ProgressDialog progressDialog;

    @Override
    public int getTitleResourceId() {
        return R.string.carddemo_title_grid_gplay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.court_grid_fragment_layout, container, false);

    }





    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCards();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!isNetworkAvailable()){
            showNoNetworkAlert();


        }

    }


    private void initCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 6; i++) {

            // GplayGridCard card = new GplayGridCard(getActivity());
            CourtGridCard card = new CourtGridCard(getActivity());
            final String courtName;

            switch (i) {
                case 0:
                    card.resourceIdThumbnail = R.drawable.ford;
                    card.headerTitle = "Ford";
                    courtName = "Ford";
                    break;
                case 1:
                    card.resourceIdThumbnail = R.drawable.hillenbrand;
                    card.headerTitle = "Hillenbrand";
                    courtName = "Hillenbrand";
                    break;
                case 2:
                    card.resourceIdThumbnail = R.drawable.wiley;
                    card.headerTitle = "Wiley";
                    courtName = "Wiley";
                    break;
                case 3:
                    card.resourceIdThumbnail = R.drawable.windsor;
                    card.headerTitle = "Windsor";
                    courtName = "Windsor";
                    break;
                case 4:
                    card.resourceIdThumbnail = R.drawable.earhart;
                    card.headerTitle = "Earhart";
                    courtName = "Earhart";
                    break;


                case 5:
                    card.resourceIdThumbnail = R.drawable.favs;
                    card.headerTitle = "Favorites";
                    break;



            }
            card.init();
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    if(!(card.getCardHeader().getTitle().equals("Favorites"))) {
                        //Toast.makeText(getActivity(), "You Clicked " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT ).show();
                        Intent i = new Intent(getActivity(), MenuFragment.class);
                        i.putExtra("Location", card.getCardHeader().getTitle());
                        getActivity().startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(),FavoritesFragment.class);
                        startActivity(i);
                    }


                }
            });
            cards.add(card);
        }

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);

        CardGridView listView = (CardGridView) getActivity().findViewById(R.id.court_grid_gridView);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void showNoNetworkAlert(){
        new AlertDialog.Builder(getActivity())
                .setTitle("No Network Connection!")
                .setMessage("This means no menus can be received, this app will now close")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getActivity().finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}