package ch.ihdg.calendarcolor;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import ch.ihdg.calendarcolor.R;

public class ColorPickerActivity extends Activity {

    static final String ARG_NAME = "arg_name";
    static final String ARG_ID = "arg_id";
    static final String ARG_COLOR = "arg_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        int color = getIntent().getIntExtra(ARG_COLOR, 0);
        String name = getIntent().getStringExtra(ARG_NAME);
        final int cal_id = getIntent().getIntExtra(ARG_ID, 0);

        setTitle( name );

        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);

        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        picker.setColor(color);
        picker.setOldCenterColor(color);

        final Button buttoncancel = (Button) findViewById(R.id.buttoncancel);
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button buttonsave = (Button) findViewById(R.id.buttonsave);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                // set the new color for the calendar
                values.put(CalendarContract.Calendars.CALENDAR_COLOR, picker.getColor());
                Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, cal_id);
                getContentResolver().update(updateUri, values, null, null);

                finish();
            }
        });
    }
}
