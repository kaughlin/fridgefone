package codepath.kaughlinpractice.fridgefone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import codepath.kaughlinpractice.fridgefone.model.Item;

public class ParseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Item.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fridgefoneID")
                .clientKey("unclekaka217c")
                .server("http://fridgefone.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
