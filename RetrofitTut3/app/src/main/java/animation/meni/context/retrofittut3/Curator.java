package animation.meni.context.retrofittut3;

import java.util.List;

/**
 * Created by CrossoOverNepal on 4/6/2016.
 */
public class Curator {
    public String title;
    public List<Dataset> dataset;

    public class Dataset {
        String curator_title;
        String curator_tagline;
    }
}
