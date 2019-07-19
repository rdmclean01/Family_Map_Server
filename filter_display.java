package Model;

/**
 * Created by rdmcl on 4/6/2018.
 */

public class filter_display {
    private String title;
    private String filter_by;

    public filter_display(String title, String filter_by) {
        this.title = title;
        this.filter_by = filter_by;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilter_by() {
        return filter_by;
    }

    public void setFilter_by(String filter_by) {
        this.filter_by = filter_by;
    }
}
