# Application Activity and Android Manifest

We want to extend the `Application` class so that we can handle global state. Create a new Java class in your app's main package naming it whatever you like, we'll call ours `OddApp`. This will be a singleton class so we'll configure it as such:

```java
package sample.oddworks.com.myoddworksapplication;

import android.app.Application;

import io.oddworks.device.model.OddView;
import io.oddworks.device.request.RestServiceProvider;
import io.oddworks.device.service.OddStore;

public class OddApp extends Application {
    private static OddApp singleton;
    public static OddApp getInstance() {
        return singleton;
    }

    private OddView homeView;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        RestServiceProvider.init(getApplicationContext(),
                getString(R.string.x_access_token),
                getString(R.string.git_revision));
        // Alternatively you could use BuildConfig.VERSION_NAME instead of R.string.git_revision
    }

    public OddView getHomeView() {
        return homeView;
    }

    public void setHomeView(OddView homeView) {
        OddStore.getInstance().storeObjects(homeView.getIncluded());
        this.homeView = homeView;
    }
}
```

We've finally written the first code that interacts with the SDK. You'll notice we utilize the `OddView`, `RestServiceProvider`, and `OddStore` classes. The `OddView` class is the main way large amounts of data are passed from the SDK to the app. We have a view that represents all of the content shown on application load called `homeView`. The `RestServiceProvider` initializes everything we need in order to interact with the API. In the setter methods for each view we're utilizing the `OddStore` so that we can minimize API calls if the `homeView` variables get collected from memory.

In our `AndroidManifest` file we'll add this new class by adding it as the name attribute to the opening `application` tag. By placing this attribute in the XML `OddApp` will run when the app first initializes.

```xml
<application
    android:name=".OddApp"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme" >
</application>
```

# Launcher Activity

We need an Activity to handle fetching initial data from the API when the application is first launched. We'll create a new Empty Activity called `SplashActivity` with a corresponding `activity_splash` layout file, making sure to check the "Launcher Activity" option. Make sure to add an attribute to this Activity in the `AndroidManifest` that disables it from history:

```xml
<activity
    android:name=".SplashActivity"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
