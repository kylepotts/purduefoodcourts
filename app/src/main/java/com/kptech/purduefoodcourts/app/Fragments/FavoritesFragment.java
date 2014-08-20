package com.kptech.purduefoodcourts.app.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kptech.purduefoodcourts.app.Activities.SettingsActivity;
import com.kptech.purduefoodcourts.app.Adapters.MenuPagerAdapater;
import com.kptech.purduefoodcourts.app.R;

import java.util.ArrayList;
import java.util.Set;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by kyle on 7/28/14.
 */
public class FavoritesFragment extends FragmentActivity implements CardHeader.OnClickCardHeaderPopupMenuListener, Card.OnSwipeListener, Card.OnUndoSwipeListListener {
    private CardArrayAdapter mCardArrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_fragment);
        initCards();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void initCards() {
        SharedPreferences prefs = getSharedPreferences("fav", 0);
        Set<String> favorites = prefs.getStringSet("favorites", null);
        ArrayList<Card> cards = new ArrayList<Card>();
        if (favorites == null) {
            Card noFavsCard = new Card(this);
            CardHeader cardHeader = new CardHeader(this);
            cardHeader.setTitle("No Favorites");
            noFavsCard.addCardHeader(cardHeader);
            cards.add(noFavsCard);

        } else {
            for (String s : favorites) {
                Card c = new Card(this);
                c.setSwipeable(true);
                c.setId(s);
                c.setOnSwipeListener(this);
                c.setOnUndoSwipeListListener(this);
                CardHeader cardHeader = new CardHeader(this);
                cardHeader.setTitle(s);
                cardHeader.setPopupMenu(R.menu.favorites_card_menu, this);
                c.addCardHeader(cardHeader);
                cards.add(c);


            }
        }
         mCardArrayAdapter = new CardArrayAdapter(this, cards);
        mCardArrayAdapter.setEnableUndo(true);


        CardListView listView = (CardListView) findViewById(R.id.favoritesList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onMenuItemClick(BaseCard baseCard, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_favorites_delete:
                Log.d("delete", "d");
                mCardArrayAdapter.remove((Card)baseCard);
                deleteFavorite(baseCard.getId());

        }
    }

    @Override
    public void onSwipe(Card card) {
        deleteFavorite(card.getId());


    }

    @Override
    public void onUndoSwipe(Card card) {
        undoDeleteFavorites(card.getId());

    }


    public void deleteFavorite(String fav) {
        SharedPreferences prefs = getSharedPreferences("fav", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> favorites = prefs.getStringSet("favorites", null);
        String favInSet = null;
        for(String s: favorites){
            if(s.equals(fav)){
                favInSet = s;
            }
        }
        favorites.remove(favInSet);
        editor.putStringSet("favorites",favorites);
        editor.commit();
    }

    public void undoDeleteFavorites(String fav){
        SharedPreferences prefs = getSharedPreferences("fav", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> favorites = prefs.getStringSet("favorites", null);
        favorites.add(fav);
        editor.putStringSet("favorites",favorites);
        editor.commit();

    }


}
