package in.mizardar.kisanhubtest;

/**
 * Created by mizardar on 08/03/18.
 */

public class ModelLink {

    private String _href;
    private String _title;

    public ModelLink(String _href, String _title) {
        this._href = _href;
        this._title = _title;
    }

    public String get_href() {
        return _href;
    }

    public String get_title() {
        return _title;
    }
}
