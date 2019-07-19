package com.example.rdmcl.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import Model.Model;
import Model.Utils;
import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recycle_people;
    RecyclerView recycle_events;
    EditText search_bar;
    private EventAdapter mEventAdapter;
    private PersonAdapter mPersonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recycle_events = (RecyclerView) findViewById(R.id.search_events);
        recycle_events.setLayoutManager(new LinearLayoutManager(this));
        recycle_people = (RecyclerView) findViewById(R.id.search_people);
        recycle_people.setLayoutManager(new LinearLayoutManager(this));


        search_bar = (EditText) findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Model.singleton.getSearch().search(charSequence.toString());

                Event[] events = Model.singleton.getSearch().getEvents();
                Person[] people = Model.singleton.getSearch().getPeople();

                mEventAdapter = new EventAdapter(events, getApplicationContext());
                //mEventAdapter.notifyDataSetChanged();
                recycle_events.setAdapter(mEventAdapter);

                mPersonAdapter = new PersonAdapter(people, getApplicationContext());
                recycle_people.setAdapter(mPersonAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mEventInfo;
        private TextView mEventName;
        private ImageView mImageView;
        private Event mEvent;
        private Context mContext;

        public EventHolder(View view, Context context) {
            //super(inflater.inflate(R.layout.list_event_item, parent, false));
            super(view);
            mContext = context;
            itemView.setOnClickListener(this);
            mEventInfo = (TextView) view.findViewById(R.id.list_event_info);
            mEventName = (TextView) view.findViewById(R.id.list_event_name);
            mImageView = (ImageView) view.findViewById(R.id.list_event_icon);
        }

        public void bind(Event event) {
            if (event != null) {
                mEvent = event;
                String name = Model.singleton.getPeople().get(mEvent.getPersonID()).getFirstName() + " " + Model.singleton.getPeople().get(mEvent.getPersonID()).getLastName();
                mEventName.setText(name);
                String info = mEvent.getEventType() + ": " + mEvent.getCity() + ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")";
                mEventInfo.setText(info);
                Drawable marker = new IconDrawable(mContext, FontAwesomeIcons.fa_map_marker).color(R.color.gray).sizeDp(40);
                mImageView.setImageDrawable(marker);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, EventActivity.class);
            // Put in the extras
            intent.putExtra("hasEvent", true);
            intent.putExtra("eventID", mEvent.getEventID());
            startActivity(intent);
            // Call the other thing
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private Event[] mEvents;
        Context mContext;

        public EventAdapter(Event[] events, Context context) {
            mEvents = events;
            mContext = context;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new EventHolder(layoutInflater.inflate(R.layout.list_event_item, parent, false), mContext);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = mEvents[position];
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return mEvents.length;
        }
    }

    private class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPersonName;
        private TextView mPersonRelation;
        private ImageView mImageView;
        private Person mPerson;
        private Context mContext;

        public PersonHolder(View view, Context context) {
            super(view);
            itemView.setOnClickListener(this);
            mPersonName = (TextView) view.findViewById(R.id.list_person_name);
            mPersonRelation = (TextView) view.findViewById(R.id.list_person_relation);
            mImageView = (ImageView) view.findViewById(R.id.list_person_icon);
            mContext = context;
        }

        public void bind(Person person) {
            if (person != null) {
                mPerson = person;
                mPersonName.setText(mPerson.getFirstName() + " " + mPerson.getLastName());
                mPersonRelation.setText("");
                if (mPerson.getGender().toLowerCase().equals("m")) {
                    Drawable gender = new IconDrawable(mContext, FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon).sizeDp(40);
                    mImageView.setImageDrawable(gender);
                } else {
                    Drawable gender = new IconDrawable(mContext, FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon).sizeDp(40);
                    mImageView.setImageDrawable(gender);
                }
            }

        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PeopleActivity.class);
            String KEY = "selected_person";
            String value = mPerson.getPersonID();
            intent.putExtra(KEY, value);
            startActivity(intent);
            //Toast.makeText(mContext,"Call the Person Activity",Toast.LENGTH_SHORT).show();
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private Person[] mFamily;
        Context mContext;

        public PersonAdapter(Person[] family, Context context) {
            mFamily = family;
            mContext = context;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new PersonHolder(layoutInflater.inflate(R.layout.list_people_info, parent, false), mContext);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person family = mFamily[position];
            holder.bind(family);
        }

        @Override
        public int getItemCount() {
            return mFamily.length;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Model.singleton.getSettings().setPosMapType(mMapSpinner.getSelectedItemPosition());
                Utils.startTopActiivty(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
