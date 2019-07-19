package com.example.rdmcl.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.Inflater;

import Model.Event;
import Model.Model;
import Model.Utils;
import Model.Person;
import Model.closeFamily;

/**
 * Displays the information about a specific person that the user clicked on
 */
public class PeopleActivity extends AppCompatActivity {

    //private Context mContext = this;

    Person selected;
    Event[] lifeEvents;
    closeFamily[] familyMembers;
    private EventAdapter mEventAdapter;
    private PersonAdapter mPersonAdapter;
    boolean showEvents = false;
    boolean showPeople = false;
    // Widgets
    TextView selected_first_name;
    TextView selected_last_name;
    TextView selected_gender;
    RecyclerView recycle_life_events;
    RecyclerView recycle_family_members;
    TextView show_events;
    TextView show_people;
    MenuItem action_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String personId = intent.getStringExtra("selected_person");
        selected = Model.singleton.getPeople().get(personId);
        //selected = new Person(); // todo get this person from whoever was clicked
        setUpLifeEvents();
        setUpFamilyMembers();

        selected_first_name = (TextView) findViewById(R.id.selected_first_name);
        selected_first_name.setText(selected.getFirstName());
        selected_last_name = (TextView) findViewById(R.id.selected_last_name);
        selected_last_name.setText(selected.getLastName());
        selected_gender = (TextView) findViewById(R.id.selected_gender);
        if (selected.getGender().toLowerCase().equals("m")) {
            selected_gender.setText("Male");
        } else {
            selected_gender.setText("Female");
        }

        recycle_life_events = (RecyclerView) findViewById(R.id.recycle_life_events);
        recycle_life_events.setLayoutManager(new LinearLayoutManager(this));
        recycle_life_events.setVisibility(View.GONE);
        recycle_family_members = (RecyclerView) findViewById(R.id.recycle_family_members);
        recycle_family_members.setLayoutManager(new LinearLayoutManager(this));
        recycle_family_members.setVisibility(View.GONE);

        mEventAdapter = new EventAdapter(lifeEvents, getApplicationContext());
        recycle_life_events.setAdapter(mEventAdapter);

        mPersonAdapter = new PersonAdapter(familyMembers, getApplicationContext());
        recycle_family_members.setAdapter(mPersonAdapter);

        show_events = (TextView) findViewById(R.id.show_events);
        show_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEvents = !showEvents;
                if(showEvents){
                    recycle_life_events.setVisibility(View.VISIBLE);
                }
                else{
                    recycle_life_events.setVisibility(View.GONE);
                }
            }
        });

        show_people = (TextView) findViewById(R.id.show_people);
        show_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPeople = !showPeople;
                if(showPeople){
                    recycle_family_members.setVisibility(View.VISIBLE);
                }
                else{
                    recycle_family_members.setVisibility(View.GONE);
                }
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
            mEvent = event;
            String name = Model.singleton.getPeople().get(mEvent.getPersonID()).getFirstName() + " " + Model.singleton.getPeople().get(mEvent.getPersonID()).getLastName();
            mEventName.setText(name);
            String info = mEvent.getEventType() + ": " + mEvent.getCity() + ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")";
            mEventInfo.setText(info);
            Drawable marker = new IconDrawable(mContext,FontAwesomeIcons.fa_map_marker).color(R.color.gray).sizeDp(40);
            mImageView.setImageDrawable(marker);

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
        private closeFamily mPerson;
        private Context mContext;

        public PersonHolder(View view, Context context) {
            super(view);
            itemView.setOnClickListener(this);
            mPersonName = (TextView) view.findViewById(R.id.list_person_name);
            mPersonRelation = (TextView) view.findViewById(R.id.list_person_relation);
            mImageView = (ImageView) view.findViewById(R.id.list_person_icon);
            mContext = context;
        }

        public void bind(closeFamily person) {

            mPerson = person;
            mPersonName.setText(mPerson.getName());
            mPersonRelation.setText(mPerson.getRelationship());
            if (mPerson.getPerson().getGender().toLowerCase().equals("m")) {
                Drawable gender = new IconDrawable(mContext, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40);
                mImageView.setImageDrawable(gender);
            } else {
                Drawable gender = new IconDrawable(mContext, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40);
                mImageView.setImageDrawable(gender);
            }

        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PeopleActivity.class);
            String KEY = "selected_person";
            String value = mPerson.getPerson().getPersonID();
            intent.putExtra(KEY, value);
            startActivity(intent);
            //Toast.makeText(mContext,"Call the Person Activity",Toast.LENGTH_SHORT).show();
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private closeFamily[] mFamily;
        Context mContext;

        public PersonAdapter(closeFamily[] family, Context context) {
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
            closeFamily family = mFamily[position];
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
                Utils.startTopActiivty(this, false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpLifeEvents() {
        Map<String, Set<Event>> events = new TreeMap<>();
        int numEvents = 0;
        // Grab all the events for a person
        for (Event current : Model.singleton.getFilteredEvents()) {
            if (current.getPersonID().equals(selected.getPersonID())) { // Event belongs to person
                numEvents++;
                if (!events.containsKey(current.getYear())) {
                    events.put(current.getYear(), new HashSet<Event>());
                }
                events.get(current.getYear()).add(current);
            }
        }
        lifeEvents = new Event[numEvents];  // Create the correct sized array
        int positionInArray = 1;

        for (String year : events.keySet()) {
            for (Event current : events.get(year)) {
                if (current.getEventType().toLowerCase().equals("birth")) {
                    lifeEvents[0] = current;
                } else if (current.getEventType().toLowerCase().equals("death")) {
                    lifeEvents[lifeEvents.length - 1] = current;
                } else {
                    lifeEvents[positionInArray] = current;
                    positionInArray++;
                }
                // todo I don't know if this orders correctly when birth and death don't have a year
            }
        }

    }

    public void setUpFamilyMembers() {
        Set<Person> children = new HashSet<>();
        Person father = null;
        Person mother = null;
        Person spouse = null;
        int numParents = 0;
        int numSpouse = 0;
        int numChildren = 0;
        for (String key : Model.singleton.getPeople().keySet()) {
            Person current = Model.singleton.getPeople().get(key);
            if (current.getFather().equals(selected.getPersonID())) {
                children.add(current);
                numChildren++;
            }
            if (current.getMother().equals(selected.getPersonID())) {
                children.add(current);
                numChildren++;
            }
        }

        if (!selected.getFather().equals("")) {
            father = Model.singleton.getPeople().get(selected.getFather());
            numParents++;
        }
        if (!selected.getMother().equals("")) {
            mother = Model.singleton.getPeople().get(selected.getMother());
            numParents++;
        }
        if (!selected.getSpouse().equals("")) {
            spouse = Model.singleton.getPeople().get(selected.getSpouse());
            numSpouse++;
        }

        familyMembers = new closeFamily[numChildren + numParents + numSpouse];

        int positionInArray = 0;
        if (father != null) {
            familyMembers[positionInArray] = new closeFamily("Father", father);
            positionInArray++;
        }
        if (mother != null) {
            familyMembers[positionInArray] = new closeFamily("Mother", mother);
            positionInArray++;
        }
        if (spouse != null) {
            familyMembers[positionInArray] = new closeFamily("Spouse", spouse);
            positionInArray++;
        }
        for (Person child : children) {
            familyMembers[positionInArray] = new closeFamily("Child", child);
            positionInArray++;
        }


    }
}
