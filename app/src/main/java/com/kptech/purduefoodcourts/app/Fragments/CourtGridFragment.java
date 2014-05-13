package com.kptech.purduefoodcourts.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kptech.purduefoodcourts.app.R;
import com.kptech.purduefoodcourts.app.Cards.courtGridCard;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
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


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kptech.purduefoodcourts.app.R;

import java.util.ArrayList;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * Grid as Google Play example
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class CourtGridFragment extends BaseFragment {

    protected ScrollView mScrollView;

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

    }


    private void initCards() {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 5; i++) {

           // GplayGridCard card = new GplayGridCard(getActivity());
            courtGridCard card = new courtGridCard(getActivity());
            final String courtName;

            switch(i){
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


            }
            card.init();
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getActivity(), "You Clicked " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT ).show();
                    Intent i = new Intent(getActivity(),MenuFragment.class);
                    i.putExtra("Location",card.getCardHeader().getTitle());
                    getActivity().startActivity(i);

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


    public class GplayGridCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail = -1;
        protected int count;

        protected String headerTitle;
        protected String secondaryTitle;
        protected float rating;

        public GplayGridCard(Context context) {
            super(context, R.layout.court_card_inner_content);

        }

        public GplayGridCard(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        private void init() {
            CardHeader header = new CardHeader(getContext());
            header.setButtonOverflowVisible(true);
            header.setTitle(headerTitle);


            addCardHeader(header);

            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
            if (resourceIdThumbnail > -1)
                thumbnail.setDrawableResource(resourceIdThumbnail);
            else
                thumbnail.setDrawableResource(R.drawable.ic_launcher);
            addCardThumbnail(thumbnail);

            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            });
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            // TextView title = (TextView) view.findViewById(R.id.carddemo_gplay_main_inner_title);
            // title.setText("FREE");

        }

        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                //viewImage.getLayoutParams().width = 196;
                //viewImage.getLayoutParams().height = 196;

            }
        }

    }

}
