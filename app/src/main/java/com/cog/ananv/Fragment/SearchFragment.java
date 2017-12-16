package com.cog.ananv.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cog.ananv.Activity.Search;
import com.cog.ananv.Adapter.AppController;
import com.cog.ananv.Adapter.SearchViewAdapter;
import com.cog.ananv.Model.Feedlist;
import com.cog.ananv.Model.Search_Model;
import com.cog.ananv.Model.RetrofitArrayAPI;
import com.cog.ananv.R;
import com.cog.ananv.Anan_URL.Constants;
import com.doodle.android.chips.model.Contact;
import com.github.ybq.android.spinkit.SpinKitView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cog.ananv.Anan_URL.Constants.ARRAYCATEGORY;
import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment  {

    private RecyclerView post_recycle, mContacts;

    private ListingAdapter mAdapter;

    int count=0;

    String strCategoryId, viewcategory, fname;

    MaterialEditText edusersearch;

    ArrayList<String> list = new ArrayList<String>();

    private ArrayList<Feedlist> movieList = new ArrayList<>();

    View search;

    SearchViewAdapter adapter;

    ImageView searchicon;

    TextView search_category;

    String username;

    SpinKitView spin_kit;

    ChipsInput mChipsView;

    private static final String[] COUNTRIES = new String[] { "Belgium",
            "France", "France_", "Italy", "Germany", "Spain" };

    ChipsInput view_category;

    protected static final String TAG = SearchFragment.class.getSimpleName();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        search = inflater.inflate(R.layout.activity_search, container, false);


        mContacts = (RecyclerView) search.findViewById(R.id.rv_contacts);
        spin_kit = (SpinKitView)search.findViewById(R.id.spin_kit_searchs);
        searchicon = (ImageView) search.findViewById(R.id.search);
        search_category = (TextView) search.findViewById(R.id.search_category);

        //final ChipsInput mChipsView = (ChipsInput) search.findViewById(R.id.cv_contacts);
//        view_category = (ChipsView) search.findViewById(R.id.view_category);
         view_category = (ChipsInput)search.findViewById(R.id.view_categoty);
         mChipsView = (ChipsInput)search. findViewById(R.id.cv_contacts);

        edusersearch = (MaterialEditText) search.findViewById(R.id.edit_usersearch);

        mContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        int numberOfColumns = 3;
        mContacts.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        mContacts.setFilterTouchesWhenObscured(true);

        mContacts.getFilterTouchesWhenObscured();

        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView textView = (AutoCompleteTextView) search
                .findViewById(R.id.editText1);
        textView.setAdapter(adapters);

        mAdapter = new ListingAdapter(this, movieList);



        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        post_recycle = (RecyclerView) search.findViewById(R.id.post_recycle);
        post_recycle.setLayoutManager(horizontalLayoutManager);
        post_recycle.setAdapter(adapter);

        // change EditText config
           mChipsView.getEditText().setCursorVisible(true);
           view_category.getEditText().setCursorVisible(false);



        view_category.addChipsListener(new  ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // chip added
                // newSize is the size of the updated selected chip list
                count++;
                closeKeyboard(getActivity(), view_category.getWindowToken());
               
            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {
                count--;

            }

            @Override
            public void onTextChanged(CharSequence charSequence) {

                closeKeyboard(getActivity(), view_category.getWindowToken());
            }
        });


        mChipsView.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chipInterface, int i) {



            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence) {


            }
        });
//        view_category.setChipsListener(new ChipsView.ChipsListener() {
//            @Override
//            public void onChipAdded(ChipsView.Chip chip) {
//                // chip added
//                closeKeyboard(getActivity(), view_category.getWindowToken());
//            }
//
//            @Override
//            public void onChipDeleted(ChipsView.Chip chip) {
//                // chip deleted
//            }
//
//            @Override
//            public void onTextChanged(CharSequence text) {
//                // text was changed
//               closeKeyboard(getActivity(), view_category.getWindowToken());
//
//            }
//
//            @Override
//            public boolean onInputNotRecognized(String text) {
//                // return true to delete the input
//                return false; // keep the typed text
//            }
//        });




//        mChipsView.setChipsValidator(new ChipsView.ChipValidator() {
//            @Override
//            public boolean isValid(Contact contact) {
//                if (contact.getDisplayName().equals("asd@qwe.de")) {
//                    return false;
//                }
//                return true;
//            }
//        });

//        mChipsView.setChipsListener(new ChipsView.ChipsListener() {
//            @Override
//            public void onChipAdded(ChipsView.Chip chip) {
//                for (ChipsView.Chip chipItem : mChipsView.getChips()) {
//                    Log.d("ChipList", "chip: " + chipItem.toString());
//                }
//            }
//
//            @Override
//            public void onChipDeleted(ChipsView.Chip chip) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence text) {
//                mAdapter.filterItems(text);
//            }
//
//            @Override
//            public boolean onInputNotRecognized(String text) {
//
//               /* try {
//                    FragmentManager fragmentManager = SearchFragment.this.getFragmentManager();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TEXT, text);
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_TITLE, "Title");
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_PLACEHOLDER, "ChipsDialogPlaceholder");
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_CONFIRM, "ChipsDialogConfirm");
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_CANCEL, "ChipsDialogCancel");
//                    bundle.putString(ChipsEmailDialogFragment.EXTRA_STRING_ERROR_MSG, "ChipsDialogErrorMsg");
//
//                    ChipsEmailDialogFragment chipsEmailDialogFragment = new ChipsEmailDialogFragment();
//                    chipsEmailDialogFragment.setArguments(bundle);
//                    chipsEmailDialogFragment.setEmailListener(new ChipsEmailDialogFragment.EmailListener() {
//                        @Override
//                        public void onDialogEmailEntered(String text, String displayName) {
//                            mChipsView.addChip(displayName, null, new Contact(null, null, displayName, text, null), false);
//                            mChipsView.clearText();
//                        }
//                    });
//                    chipsEmailDialogFragment.show(fragmentManager, ChipsEmailDialogFragment.class.getSimpleName());
//                } catch (ClassCastException e) {
//                    Log.e("CHIPS", "Error ClassCast", e);
//                }*/
//                return false;
//            }
//        });

        search_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count > 0) {
                    Intent search = new Intent(getActivity(), Search.class);
                    search.putExtra("category", fname);
                    search.putExtra("search_type", "searchcategory");
                    startActivity(search);
                    //searchpost(fname);

                }

                else
                {
                    Snackbar snackbar = Snackbar
                            .make(getView(),"Add atleast one category", Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        }});

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = edusersearch.getText().toString().trim();
                username = username.replaceAll(" ","%20");
                Intent search = new Intent(getActivity(), Search.class);
                search.putExtra("category_username",username);
                search.putExtra("search_type","searchusername");
                startActivity(search);
            }
        });

        displayListView();

        return search;
    }

    private void searchpost(String fname) {

        movieList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SEARCHURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);
        Call<List<Search_Model>> call = service.searchpost(fname);
        call.enqueue(new Callback<List<Search_Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Search_Model>> call, @NonNull retrofit2.Response<List<Search_Model>> response) {
                List<Search_Model> RequestData = response.body();
                if (RequestData != null) {
                    for (int i = 0; i < RequestData.size(); i++) {
                        Feedlist movie = new Feedlist();
                        movie.setpostid(RequestData.get(i).getPostId());
                        movie.setposttype(RequestData.get(i).getPostType());
                        movie.setcaption(RequestData.get(i).getCaption());
                        movie.seturl(RequestData.get(i).getPostUrl());
                        movieList.add(movie);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Search_Model>> call, @NonNull Throwable t) {
            }
        });
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
        private SearchFragment activity;
        private String[] data = new String[13];

        private List<String> filteredList = new ArrayList<>();

        private ArrayList<Feedlist> movieItems = new ArrayList<Feedlist>();


        public ContactsAdapter() {

            this.activity = activity;
            this.movieItems = movieItems;
            int i = 0;
            for (Map.Entry<String, String> entry : ARRAYCATEGORY.entrySet()) {
                // Categoryspinnar.add(entry.getValue());
                strCategoryId = entry.getKey();
                data[i] = entry.getValue();
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
            //Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
            Contact contact = new Contact(null, null, null, email, null);

            if (selection.isChecked()) {
                //boolean indelibe = Math.random() > 0.8f;
                view_category.addChip(email, valueOf(contact));

                viewcategory = email;


            } else {
                view_category.removeChipById(contact);
            }
        }
    }


    public void displayListView() {

        movieList.clear();
        showDialog();
        final String url = Constants.CATEGORYURL;
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Feedlist movie = new Feedlist();
                                movie.set_category_id(obj.optString("category_id"));
                                movie.set_category_name(obj.optString("category_name"));
                                movieList.add(movie);
                                mContacts.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                        }
                        dismissDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    dismissDialog();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    dismissDialog();


                } else if (error instanceof ServerError) {
                    //TODO
                    dismissDialog();
                } else if (error instanceof NetworkError) {
                    //TODO
                    dismissDialog();
                } else if (error instanceof ParseError) {
                    //TODO
                    dismissDialog();
                }
            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }


    public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.MyViewHolder> {
        private SearchFragment activity;
        private ArrayList<Feedlist> movieItems = new ArrayList<Feedlist>();
        private Feedlist movies = new Feedlist();
        //private String[] data = new String[13];
        private List<String> filteredList = new ArrayList<>();
        View itemView;

        private String[] data = new String[]{
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
        };

        public ListingAdapter(SearchFragment activity, ArrayList<Feedlist> movieItems) {

            this.activity = activity;
            this.movieItems = movieItems;

            Collections.addAll(filteredList, data);
        }

        public void filterItems(CharSequence text) {


        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView listImage;
            TextView listInfo, listReveiew;
            RatingBar listRating;

            MyViewHolder(View itemView) {
                super(itemView);
                listInfo = (TextView) itemView.findViewById(R.id.tv_contact_name);
                final CheckBox check1 = (CheckBox) itemView.findViewById(R.id.cb_contact_selection);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checkable_contact, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Feedlist movies = movieItems.get(position);

            final CheckBox check1 = (CheckBox) itemView.findViewById(R.id.cb_contact_selection);
            final TextView name = (TextView) itemView.findViewById(R.id.tv_contact_name);

            holder.listInfo.setText(movies.get_category_name());


            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });*/
            check1.setOnCheckedChangeListener(null);

            check1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     String email = name.getText().toString();
                    //Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
//                    Contact contact = new Contact(null, null, null, email, imgUrl);
                    Contact contact = new Contact(email, null, null, null, null);

                    if (check1.isChecked()) {

                        list.add(movies.get_category_id());
                        fname = "";
                        for (int i = 0; i < list.size(); i++) {
                            fname = fname + "," + list.get(i);
                        }
                        //boolean indelibe = Math.random() > 0.8f;
                        view_category.addChip(email,null);
                        //viewcategory = email;

                    } else {

                        list.remove(movies.get_category_id());
                        fname = "";
                        for (int i = 0; i < list.size(); i++) {
                            fname = fname + "," + list.get(i);
                        }
                        view_category.removeChipById(contact);
                        view_category.removeChipByInfo(email);
                        view_category.removeChipByLabel(email);
                    }

                }
            });
        }


        @Override
        public int getItemCount() {
            return movieItems.size();
        }
    }


    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }


    public void showDialog(){
        spin_kit.setVisibility(View.VISIBLE);
    }

    public void dismissDialog(){
        spin_kit.setVisibility(View.GONE);
    }
}
