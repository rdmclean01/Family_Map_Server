package com.example.rdmcl.client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import Model.Model;
import Model.Utils;
import Model.filter_display;

/**
 * Handles the filter screen
 */
public class FilterActivity extends AppCompatActivity {
    private filter_display[] filter_list;

    RecyclerView recycle_list;
    private FilterAdapter mFilterAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // I don't need any parameters from the intent
        recycle_list = (RecyclerView) findViewById(R.id.recycle_event_types);
        recycle_list.setLayoutManager(new LinearLayoutManager(this));

        filter_list = buildFilterItems();
        Model.singleton.getFilter().printTypeData();

        mFilterAdapter = new FilterAdapter(filter_list, getApplicationContext());
        recycle_list.setAdapter(mFilterAdapter);
    }

    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private TextView title;
        private TextView filter_by;
        private Switch mSwitch;
        private filter_display mData;

        public FilterHolder(View view, Context context) {
            super(view);
            mContext = context;
            itemView.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.filter_title);
            filter_by = (TextView) view.findViewById(R.id.filter_by_specifier);
            mSwitch = (Switch) view.findViewById(R.id.filter_switch);

            //mSwitch.setChecked(true);
            // todo How do I get the switches to keep their state
        }

        public void bind(filter_display current, final int position) {
            mData = current;
            title.setText(mData.getTitle());
            filter_by.setText(mData.getFilter_by());
            mSwitch.setChecked(Model.singleton.getFilter().getFilterSettings()[position]);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Model.singleton.getFilter().updateFilter(position, b);
                    if(Model.singleton.getFilter().getTypeData()[position].equals("male") || Model.singleton.getFilter().getTypeData()[position].equals("female")){
                        Model.singleton.getSettings().setDisplaySpouseLine(b);
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {
        private filter_display[] mData;
        Context mContext;

        public FilterAdapter(filter_display[] datas, Context context) {
            mData = datas;
            mContext = context;
        }

        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new FilterHolder(layoutInflater.inflate(R.layout.list_filter_item, parent, false), mContext);
        }

        @Override
        public void onBindViewHolder(FilterHolder holder, int position) {
            filter_display current = mData[position];
            holder.bind(current, position);
        }

        @Override
        public int getItemCount() {
            return mData.length;
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

    public filter_display[] buildFilterItems() {
        // Get the right size for our current data
        // 4 is the number of options that are always present
        final int DEFAULT_OPTIONS = 4;
        filter_list = new filter_display[Model.singleton.getEventTypes().size() + DEFAULT_OPTIONS];
        Model.singleton.setFilter(filter_list.length);
        Model.singleton.getFilter().initTypeData(Model.singleton.getEventTypes().size() + DEFAULT_OPTIONS);
        int array_position = 0;
        String title = "";
        String filter_by = "";
        for (String cur : Model.singleton.getEventTypes()) {
            title = cur + " Events";
            filter_by = "FILTER BY " + title.toUpperCase();
            filter_list[array_position] = new filter_display(title, filter_by);
            Model.singleton.getFilter().setTypeToPosition(cur, array_position);
            array_position++;
        }

        // Set up Father's side
        filter_list[array_position] = new filter_display("Father's Side", "FILTER BY FATHER'S SIDE OF FAMILY");
        Model.singleton.getFilter().setTypeToPosition("paternal", array_position);
        // Set up Mother's side
        filter_list[array_position + 1] = new filter_display("Mother's Side", "FILTER BY MOTHER'S SIDE OF FAMILY");
        Model.singleton.getFilter().setTypeToPosition("maternal", array_position + 1);

        // Set up Male Events
        filter_list[array_position + 2] = new filter_display("Male Events", "FILTER EVENTS BASED ON GENDER");
        Model.singleton.getFilter().setTypeToPosition("male", array_position + 2);

        // Set up Female Events
        filter_list[array_position + 3] = new filter_display("Female Events", "FILTER EVENTS BASED ON GENDER");
        Model.singleton.getFilter().setTypeToPosition("female", array_position + 3);


        return filter_list; // Returns only for testing

    }
}
