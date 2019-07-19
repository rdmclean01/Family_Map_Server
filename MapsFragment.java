package com.example.rdmcl.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Model.Person;
import Model.Event;
import Model.Model;
import Model.Utils;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    ImageView genderImageView;
    TextView personName;
    TextView EventInfo;
    MenuItem action_search;
    MenuItem action_filter;
    MenuItem action_settings;
    MenuItem action_home; // todo why were there four?
    LinearLayout person_info_section;
    private static final int REQUEST_CODE_FILTER = 0;
    private static final int REQUEST_CODE_SETTINGS = 1;

    Map<Marker, Event> markersToEvents;
    Event Selected;
    Set<Polyline> lines;
    boolean MainActivity = true;
    LatLng eventPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Selected = Model.singleton.getEvents().get(getArguments().getString("eventID"));
            eventPosition = new LatLng(Double.parseDouble(Selected.getLatitude()), Double.parseDouble(Selected.getLongitude()));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            MainActivity = false;
        } else {
            MainActivity = true;
        }
        setHasOptionsMenu(true); // todo Change here when you do the Event Activity


        // todo Need the Up button
        markersToEvents = new HashMap<>();
        lines = new HashSet<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        //final Context context = this.getContext();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map_fragment);
        mapFragment.getMapAsync(this);


        personName = (TextView) v.findViewById(R.id.personName);
        //personName.setText("Click on a marker to see event details"); // This changes when user clicks on a marker

        EventInfo = (TextView) v.findViewById(R.id.eventInfo);
        EventInfo.setText("");

        genderImageView = (ImageView) v.findViewById(R.id.male_female_icon);

        person_info_section = (LinearLayout) v.findViewById(R.id.maps_person_info);
        person_info_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Selected != null) {
                    Intent intent = new Intent(getActivity(), PeopleActivity.class);
                    String KEY = "selected_person";
                    String value = Selected.getPersonID();
                    intent.putExtra(KEY, value);
                    startActivity(intent);
                } else {
                    // do nothing
                }
            }
        });
        if (!MainActivity) {
            action_home = v.findViewById(android.R.id.home);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (MainActivity) {
            inflater.inflate(R.menu.menu, menu);

            action_search = menu.findItem(R.id.action_search);
            action_filter = menu.findItem(R.id.action_filter);
            action_settings = menu.findItem(R.id.action_settings);
            action_search.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).colorRes(R.color.menu_icon).actionBarSize());
            action_filter.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter).colorRes(R.color.menu_icon).actionBarSize());
            action_settings.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).colorRes(R.color.menu_icon).actionBarSize());
            action_search.setEnabled(true);
            action_filter.setEnabled(true);
            action_settings.setEnabled(true);
        } else {
            action_home = menu.findItem(android.R.id.home);
        }


        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getActivity(),SearchActivity.class));
                Toast.makeText(getActivity(), "Search button active", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_filter:
                startActivity(new Intent(getActivity(), FilterActivity.class));
                //Toast.makeText(getActivity(), "Filter button active", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                //Toast.makeText(getActivity(), "Settings button active", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Utils.startTopActiivty(getActivity(), false);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.setMapType(Model.singleton.getSettings().getMapType());
            populateMap(true);
            if (Selected != null) {
                drawStoryLines();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (!MainActivity) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventPosition));
            personName.setText(Model.singleton.getPeople().get(Selected.getPersonID()).getFirstName() + " " +
                    Model.singleton.getPeople().get(Selected.getPersonID()).getLastName());
            EventInfo.setText(Selected.getEventType() + ": " + Selected.getCity() + ", " +
                    Selected.getCountry() + " (" + Selected.getYear() + ")");
            if (Model.singleton.getPeople().get(Selected.getPersonID()).getGender().equals("m")) {
                Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40);
                genderImageView.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40);
                genderImageView.setImageDrawable(genderIcon);
            }
            drawStoryLines();
        }
        mMap.setMapType(Model.singleton.getSettings().getMapType());
        populateMap(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                // Update the event linked with the marker
                Selected = markersToEvents.get(marker);
                personName.setText(Model.singleton.getPeople().get(Selected.getPersonID()).getFirstName() + " " +
                        Model.singleton.getPeople().get(Selected.getPersonID()).getLastName());
                EventInfo.setText(Selected.getEventType() + ": " + Selected.getCity() + ", " +
                        Selected.getCountry() + " (" + Selected.getYear() + ")");
                if (Model.singleton.getPeople().get(Selected.getPersonID()).getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                            .colorRes(R.color.male_icon).sizeDp(40);
                    genderImageView.setImageDrawable(genderIcon);
                } else {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                            .colorRes(R.color.female_icon).sizeDp(40);
                    genderImageView.setImageDrawable(genderIcon);
                }
                drawStoryLines();
                // Populate the map function
                return false; // todo WHAT???
            }
        });
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_FILTER) {
            if (data == null) {
                return;
            }
            // Does this work???
            populateMap(true);
            drawStoryLines();
        }
        if (requestCode == REQUEST_CODE_SETTINGS) {
            //todo
            populateMap(true);
            drawStoryLines();

        }
    }*/

    public void populateMap(boolean condition) {
        if (condition) {
            mMap.clear();
            markersToEvents.clear();
            lines.clear();
        }
        // Get colors from the model
        Model.singleton.setTypeColor();
        // For all filtered events


        for (Event current : Model.singleton.getFilteredEvents()) {
            LatLng position = new LatLng(Double.parseDouble(current.getLatitude()), Double.parseDouble(current.getLongitude()));
            Marker event = mMap.addMarker(new MarkerOptions().position(position).title(current.getCountry()));
            float hue = Model.singleton.getTypeColor().get(current.getEventType());
            event.setIcon(BitmapDescriptorFactory.defaultMarker(Model.singleton.getTypeColor().get(current.getEventType())));
            markersToEvents.put(event, current);
        }
    }

    public void drawStoryLines() {

        for (Polyline line : lines) {
            line.remove(); // todo this is not working
        }
        lines.clear();
        String selectedPerson = Selected.getPersonID();
        Person spouse = null;

        // Connect to the Spouse
        for (String potentialSpouseID : Model.singleton.getPeople().keySet()) {
            Person potentialSpouse = Model.singleton.getPeople().get(potentialSpouseID);
            if (potentialSpouse.getSpouse().equals(selectedPerson)) {
                spouse = potentialSpouse;
            }
        } // Find an event for the spouse and draw a line to connect them
        if (spouse != null) {
            Event spouseBirthday = findBirth(spouse.getPersonID());
            if (spouseBirthday == null) {
                spouseBirthday = new Event();
                spouseBirthday.setLatitude("0");
                spouseBirthday.setLongitude("0");
            }
            if(Model.singleton.getSettings().isDisplaySpouseLine()) {
                LatLng location_selected = new LatLng(Double.parseDouble(Selected.getLatitude()), Double.parseDouble(Selected.getLongitude()));
                LatLng location_spouse = new LatLng(Double.parseDouble(spouseBirthday.getLatitude()), Double.parseDouble(spouseBirthday.getLongitude()));
                Polyline line = mMap.addPolyline(new PolylineOptions().add(location_selected, location_spouse).color(getContext().getResources().getColor(Model.singleton.getSettings().getSpouseColor())));
                //System.out.println("Spouse line made from " + Model.singleton.getPeople().get(Selected.getPersonID()).getFirstName() + " to " + Model.singleton.getPeople().get(spouseBirthday.getPersonID()).getFirstName());
                //System.out.println("Spouse line made from " + Selected.getCity() + " to " + spouseBirthday.getCity());
                lines.add(line);
            }

        }


        // Connect all the events in the life story
        Map<Integer, Event> lifeStory = new TreeMap<>();
        for (Event current : Model.singleton.getFilteredEvents()) {
            String ID = current.getPersonID();
            if (current.getPersonID().equals(Selected.getPersonID())) {
                lifeStory.put(Integer.parseInt(current.getYear()), current);
            }
        }

        Event[] lifeStorySet = new Event[lifeStory.keySet().size()];
        int place = 0;
        for (Integer it : lifeStory.keySet()) {
            lifeStorySet[place] = lifeStory.get(it);
            place++;
        }

        for (int i = 0; i < lifeStorySet.length - 1; i++) {
            if(Model.singleton.getSettings().isDisplayLifeStory()) {
                LatLng current = new LatLng(Double.parseDouble(lifeStorySet[i].getLatitude()), Double.parseDouble(lifeStorySet[i].getLongitude()));
                LatLng next = new LatLng(Double.parseDouble(lifeStorySet[i + 1].getLatitude()), Double.parseDouble(lifeStorySet[i + 1].getLongitude()));
                Polyline line = mMap.addPolyline(new PolylineOptions().add(current, next).color(getContext().getResources().getColor(Model.singleton.getSettings().getLifeStoryColor())));
                System.out.println("Story Line made from " + lifeStorySet[i].getCountry() + " to " + lifeStorySet[i + 1].getCountry());
                lines.add(line);
            }
        }

        Event event = fillFamilyTree(Model.singleton.getPeople().get(Selected.getPersonID()), 20, Selected);

        System.out.println("PAUSE");
        // Recursive function that connects to the selected's parents, and up and up
        // Width gets smaller each time
        // Pass in the root
        //

    }

    public Event fillFamilyTree(Person p, float width, Event startingEvent) {
        if (p == null) {
            return null;
        }
        Event Father = null;
        Event Mother = null;
        String fatherName = "empty";
        String motherName = "empty";
        Event current = findBirth(p.getPersonID());
        LatLng child;
        if (startingEvent == null) {
            if (current != null) {
                child = new LatLng(Double.parseDouble(current.getLatitude()), Double.parseDouble(current.getLongitude()));
            } else {
                child = new LatLng(0, 0);
            }
        } else {
            if (current != null) {
                child = new LatLng(Double.parseDouble(startingEvent.getLatitude()), Double.parseDouble(startingEvent.getLongitude()));
            } else {
                child = new LatLng(0, 0);
            }
        }

        if (p.getFather() != null) {
            Father = fillFamilyTree(Model.singleton.getPeople().get(p.getFather()), width * (3 / 4), null);
            if (Father != null) {
                if (Model.singleton.getSettings().isDisplayFamilyTree()) {
                    LatLng dad = new LatLng(Double.parseDouble(Father.getLatitude()), Double.parseDouble(Father.getLongitude()));
                    Polyline line = mMap.addPolyline(new PolylineOptions().add(child, dad).color(getContext().getResources().getColor(Model.singleton.getSettings().getFamilyTreeColor())).width(width));
                    lines.add(line);
                    System.out.println("Line drawn from " + child.toString() + " to " + dad.toString());
                }
                fatherName = Model.singleton.getPeople().get(Father.getPersonID()).getFirstName();
            }
        }
        if (p.getMother() != null) {

            Mother = fillFamilyTree(Model.singleton.getPeople().get(p.getMother()), width / 2, null);
            if (Mother != null) {
                if (Model.singleton.getSettings().isDisplayFamilyTree()) {
                    LatLng mom = new LatLng(Double.parseDouble(Mother.getLatitude()), Double.parseDouble(Mother.getLongitude()));
                    Polyline line = mMap.addPolyline(new PolylineOptions().add(child, mom).color(getContext().getResources().getColor(Model.singleton.getSettings().getFamilyTreeColor())).width(width));
                    lines.add(line);
                    System.out.println("Line drawn from " + child.toString() + " to " + mom.toString());
                }
                motherName = Model.singleton.getPeople().get(Mother.getPersonID()).getFirstName();
            }

        }
        //System.out.println(p.getFirstName() + "'s father's name is " + fatherName + " and his mother's name is " + motherName);

        return current;

    }

    /**
     * Although I call this findBirth, it actually just finds the earliest non-filtered event for a person
     *
     * @param personID
     * @return
     */
    public Event findBirth(String personID) {
        String linkType = "birth";
        int earlyYear = 3000; // Number high enough to not cause problems
        Person p = Model.singleton.getPeople().get(personID);
        Event birth = null;

        for (Event current : Model.singleton.getFilteredEvents()) {
            if (current.getPersonID().equals(personID)) {
                if (Integer.parseInt(current.getYear()) < earlyYear) {
                    earlyYear = Integer.parseInt(current.getYear());
                    birth = current;
                }
            }
        }
        return birth;
    }

    public void redrawLines(boolean condition) {
        if (condition) {
            populateMap(condition);
            drawStoryLines();
        }
    }


}



