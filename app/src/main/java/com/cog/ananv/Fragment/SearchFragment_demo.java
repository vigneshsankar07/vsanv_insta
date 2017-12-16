package com.cog.ananv.Fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cog.ananv.Others.ChipsEmailDialogFragment;
import com.cog.ananv.Adapter.SearchViewAdapter;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.R;
import com.cog.ananv.Others.WorldPopulation;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.cog.ananv.Anan_URL.Constants.ARRAYCATEGORY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment_demo extends Fragment {

    private RecyclerView mContacts;
    private ContactsAdapter mAdapter;
    private ChipsView mChipsView,view_category;

    String strCategoryId,viewcategory;

    ArrayList<String> lists= new ArrayList<String>();

    private ArrayList<Feedlist> movieList = new ArrayList<>();

    Spinner categorySpinner;
    String[] categories = {"Birthday", "Baby", "Anniversery", "Wedding", "Oktoberfest", "Thank you", "Congratulations"};
    View search;

    ListView list;
    SearchViewAdapter adapter;
    EditText editsearch;
    String[] rank;
    String[] country;
    String[] population;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();

    public SearchFragment_demo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        search = inflater.inflate(R.layout.activity_search, container, false);

        mContacts = (RecyclerView) search.findViewById(R.id.rv_contacts);
        mContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
       /* LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);*/
        int numberOfColumns = 3;
        mContacts.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        //mContacts.setLayoutManager(horizontalLayoutManager);

        mAdapter = new ContactsAdapter(this, movieList);
        mContacts.setAdapter(mAdapter);

        mChipsView = (ChipsView) search.findViewById(R.id.cv_contacts);
//        view_category = (ChipsView) search.findViewById(R.id.view_categoty);

//        mChipsView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/FiraSans-Medium.ttf"));
        // mChipsView.useInitials(14, Typeface.createFromAsset(this.getAssets(), "fonts/FiraSans-Medium.ttf"), Color.RED);

        // change EditText config
        mChipsView.getEditText().setCursorVisible(true);
        view_category.getEditText().setCursorVisible(false);

        view_category.setChipsListener(new ChipsView.ChipsListener() {
            @Override
            public void onChipAdded(ChipsView.Chip chip) {
                // chip added
                closeKeyboard(getActivity(), view_category.getWindowToken());
            }

            @Override
            public void onChipDeleted(ChipsView.Chip chip) {
                // chip deleted
            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text was changed
                closeKeyboard(getActivity(), view_category.getWindowToken());

            }

            @Override
            public boolean onInputNotRecognized(String text) {
                // return true to delete the input
                return false; // keep the typed text
            }
        });
        mChipsView.setChipsValidator(new ChipsView.ChipValidator() {
            @Override
            public boolean isValid(Contact contact) {
                if (contact.getDisplayName().equals("asd@qwe.de")) {
                    return false;
                }
                return true;
            }
        });

        mChipsView.setChipsListener(new ChipsView.ChipsListener() {
            @Override
            public void onChipAdded(ChipsView.Chip chip) {
                for (ChipsView.Chip chipItem : mChipsView.getChips()) {
                    Log.d("ChipList", "chip: " + chipItem.toString());
                }
            }

            @Override
            public void onChipDeleted(ChipsView.Chip chip) {

            }

            @Override
            public void onTextChanged(CharSequence text) {
                mAdapter.filterItems(text);
            }

            @Override
            public boolean onInputNotRecognized(String text) {

                try {
                    FragmentManager fragmentManager = SearchFragment_demo.this.getFragmentManager();

                    Bundle bundle = new Bundle();
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TEXT, text);
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TITLE, "Title");
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_PLACEHOLDER, "ChipsDialogPlaceholder");
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_CONFIRM, "ChipsDialogConfirm");
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_CANCEL, "ChipsDialogCancel");
                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_ERROR_MSG, "ChipsDialogErrorMsg");

                    ChipsEmailDialogFragment chipsEmailDialogFragment = new ChipsEmailDialogFragment();
                    chipsEmailDialogFragment.setArguments(bundle);
                    chipsEmailDialogFragment.setEmailListener(new ChipsEmailDialogFragment.EmailListener() {
                        @Override
                        public void onDialogEmailEntered(String text, String displayName) {
                            mChipsView.addChip(displayName, null, new Contact(null, null, displayName, text, null), false);
                            mChipsView.clearText();
                        }
                    });
                    chipsEmailDialogFragment.show(fragmentManager, ChipsEmailDialogFragment.class.getSimpleName());
                } catch (ClassCastException e) {
                    Log.e("CHIPS", "Error ClassCast", e);
                }
                return false;
            }
        });
        return search;
    }


    public class ContactsAdapter extends RecyclerView.Adapter<CheckableContactViewHolder> {

        /* private String[] data = new String[]{
                 "Hot",
                 "Super Star",
                 "Selfie",
                 "New release",
                 "Personal",
                 "Crazy",
                 "Secrets",
                 "Community",
                 "Life Style",
                 "Wiled",
                 "Gadgets",
                 "Education",
                 "Health"
         };*/

        //*************************** category dynamic array count add in listing *****************
        private SearchFragment_demo activity;
        private String[] data = new String[13];

        private List<String> filteredList = new ArrayList<>();

        private ArrayList<Feedlist> movieItems=new ArrayList<Feedlist>();


        public ContactsAdapter(SearchFragment_demo activity, ArrayList<Feedlist> movieItems) {

            this.activity = activity;
            this.movieItems = movieItems;

            int i =0;
            for (Map.Entry<String, String> entry : ARRAYCATEGORY.entrySet()) {
                // Categoryspinnar.add(entry.getValue());
                    strCategoryId=  entry.getKey();

                data[i]=entry.getValue();
                i++;
            }
            Collections.addAll(filteredList, data);
        }


        @Override
        public CheckableContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_checkable_contact, parent, false);
            return new CheckableContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CheckableContactViewHolder holder, int position) {
            holder.name.setText(filteredList.get(position));
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        public void filterItems(CharSequence text) {
            filteredList.clear();
            if (TextUtils.isEmpty(text)) {
                Collections.addAll(filteredList, data);
            } else {
                for (String s : data) {
                    /*if (s.toLowerCase().contains(text)||s.toUpperCase().contains(text)) {
                        filteredList.add(s);
                    }*/
                    filteredList.add(s);
                }
            }
            notifyDataSetChanged();
        }

       /* @Override
        public int getItemViewType(int position) {
            if(filteredList==null) {
                return Math.abs(0);
            }else{
                return Math.abs(filteredList.get(position).hashCode());
            }
        }*/
    }

    public class CheckableContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView name;
        public final CheckBox selection;

        public CheckableContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_contact_name);
            selection = (CheckBox) itemView.findViewById(R.id.cb_contact_selection);

            selection.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selection.performClick();
                }
            });


        }

        @Override
        public void onClick(View v) {
            String email = name.getText().toString();
            Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
            Contact contact = new Contact(null, null, null, email, imgUrl);

            if (selection.isChecked()) {
                boolean indelibe = Math.random() > 0.8f;
                view_category.addChip(email, imgUrl, contact, indelibe);

                viewcategory = email;


            } else {
                view_category.removeChipBy(contact);

            }
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }


}