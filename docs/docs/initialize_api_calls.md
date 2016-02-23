# Get Config Data from API
***Note: We've configured the NASA test organization to send config information down with the use of `OddView`s as the main way of constructing data in the application. This is completely optional. You may format your data however you like on the Oddworks dashboard. For example: you may just want to call all collections or videos directly.***

Now it's time to make our `SplashActivity` actually do something. First we'll set up the member variables...

```java
// app/java/sample.oddworks.com.oddsampleapp/SplashActivity

// ...
public class SplashActivity extends AppCompatActivity {
    // Constants
    private static final String HOME_VIEW_NAME = "homepage";

    // Data
    private RestServiceProvider restServiceProvider = RestServiceProvider.getInstance();
    private ApiCaller apiCaller = restServiceProvider.getApiCaller();
    private Context context;

    // ...
}
```

There's a new class that we're interacting with called `ApiCaller`. This does exactly what it sounds like and is the class used to make calls to the API.

The first thing we'll want to do in our `onCreate` method after setting the content view is set our context.

```java
// app/java/sample.oddworks.com.oddsampleapp/SplashActivity

// ...
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    context = this;
}
// ...
```

The next thing check to see if the application is connected to the internet. This method may be useful in other classes in our application so instead of creating a method for it in our `SplashActivity` we'll create a `Utility` class with a method to check if there is internet connectivity.

```java
// app/java/sample.oddworks.com.oddsampleapp/Utility

package sample.oddworks.com.myoddworksapplication;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utility {
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager con_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
```

In order for this to work, we'll need to add some permissions to our `AndroidManifest` file right before the opening `application` tag.

```xml
// app/manifests/AndroidManifest.xml

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Now we can check to see if the app has connectivity in the `SplashActivity`'s `onCreate` method:

```java
// app/java/sample.oddworks.com.oddsampleapp/SplashActivity

// ...
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    context = this;

    if (Utility.hasInternetConnection(getApplicationContext())) {
        apiCaller.getConfig(new ConfigRequestCallback());
    } else {
        // handle failure by notifying user on UI
        // consider giving them a "retry" button
        // perhaps via a Snackbar
    }
}

private final class ConfigRequestCallback implements OddCallback<Config> {
    @Override
    public void onSuccess(Config config) {
        // handle success
    }

    @Override
    public void onFailure(final Exception exception) {
        // handle failure
    }
}
// ...
```

If the app is connected to the internet we use the `apiCaller` to request configuration from the API and pass it a callback class that implements an `OddCallback`. If there's a failure we should notify the user of this and give them the option to retry. *Warning: if you try to manipulate the UI within this callback you will crash the application since you aren't running on the main thread. You must specify that you'd like to run on the main thread if you'd like to make any UI updates.*


# Getting View Data from API

Assuming the config request returns successful we can use the config object to set up many things about our app. For now, we'll only focus on the views that the config sends us.

```java
// app/java/sample.oddworks.com.oddsampleapp/SplashActivity

// ...
private final class ConfigRequestCallback implements OddCallback<Config> {
    @Override
    public void onSuccess(Config config) {
        Map<String, String> views = config.getViews();
        String homeView = views.get(HOME_VIEW_NAME);

        apiCaller.getView(homeView, new HomeViewRequestCallback(HOME_VIEW_NAME));
    }

    @Override
    public void onFailure(final Exception exception) {
        // handle failure
    }
}

private final class HomeViewRequestCallback implements OddCallback<OddView> {
    
    public HomeViewRequestCallback() {
    
    }

    @Override
    public void onSuccess(OddView view) {
        OddApp oddApp = OddApp.getInstance();

        oddApp.setHomeView(view);
    }

    @Override
    public void onFailure(final Exception exception) {
        // handle failure
    }

    public String getViewType() {
        return mViewType;
    }
}
// ...
```

Here, we're creating a custom `OddCallback` for the view that we're requesting through the `apiCaller`. Remember, the setter methods for each view utilize the `OddStore` so we'll have access to this information throughout the app if the member variables in `OddApp` get collected from memory.

The last thing we need to do in our `SplashActivity` is launch a new Activity that will make use of all the data we've fetched. First we'll need to create a new Activity, we'll call ours `HomeActivity`. Now in the `HomeViewRequestCallback` section of our `SplashActivity` we'll make sure we have all our data and launch a new Intent:

```java
// ...
@Override
public void onSuccess(OddView view) {
    OddApp oddApp = OddApp.getInstance();

    oddApp.setHomeView(view);

    Intent home = new Intent(context, HomeActivity.class);
    startActivity(home);
}
// ...
```
