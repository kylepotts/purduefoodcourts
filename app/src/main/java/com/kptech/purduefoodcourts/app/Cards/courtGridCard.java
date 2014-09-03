package com.kptech.purduefoodcourts.app.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.kptech.purduefoodcourts.app.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

public class courtGridCard extends Card {

    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected RatingBar mRatingBar;
    public int resourceIdThumbnail = -1;
    protected int count;

    public String headerTitle;
    protected String secondaryTitle;
    protected float rating;

    public courtGridCard(Context context) {
        super(context, R.layout.court_card_inner_content);

    }

    public courtGridCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public void init() {
        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(headerTitle);


        addCardHeader(header);

       courtGridCardThumbnail thumbnail = new courtGridCardThumbnail(getContext());
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

    class courtGridCardThumbnail extends CardThumbnail {

        public courtGridCardThumbnail(Context context) {
            super(context);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View viewImage) {
            //viewImage.getLayoutParams().width = 196;
            //viewImage.getLayoutParams().height = 196;

        }
    }

}


