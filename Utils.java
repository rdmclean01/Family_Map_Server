package Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.view.MenuItem;

import com.example.rdmcl.client.MainActivity;
import com.example.rdmcl.client.R;
import com.joanzapata.iconify.IconDrawable;

/**
 * Created by rdmcl on 3/29/2018.
 */

public class Utils {

    public static void startTopActiivty(Context context, boolean newInstance) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newInstance) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /*
    public static void setIcon(Context context, MenuItem item, Icon icon){
        item.setIcon(new IconDrawable(context, icon)
                .colorRes(R.color.menu_icon)
                .actionBarSize());
    }*/

    // New intent object

}
