# Integrate the Oddworks SDK

Our SDK is hosted on [Bintray](https://bintray.com/oddnetworks/maven/device-sdk/view).

Include our oddworks repository in the `repositories` section of your app module's `build.gradle` file:

```groovy
// app/build.gradle

repositories {
    // ...
    maven {
        url  "http://oddnetworks.bintray.com/maven" 
    }
}
```

Now in the  `dependencies` section of your app module's `build.gradle` file add the Oddworks SDK:

```groovy
// app/build.gradle

dependencies {
    // ...
    compile "io.oddworks:device-sdk:beta-1.0.0"
}
```

There's one more _optional_ thing you can do to your application's `build.gradle` file that will be used in the next step. The SDK expects an app version when it's being initialized. One option would be to use your app module's `BuildConfig.VERSION_NAME`. Another might be to send your git revision.

```groovy
// app/build.gradle

android {
    // ...
    defaultConfig {
        // ...
        resValue "string", "git_revision", gitRevision()
    }
    // ...
}

// ...

def gitRevision() {
    def cmd = "git rev-parse --short HEAD"
    return cmd.execute().text.trim()
}
```

Sync your project with Gradle files and you will have the OddworksDeviceSDK added to your External Libraries.


# Configure Your Access Token

The SDK also will need an access token to use when accessing the API. For instructions on how to receive your own access token check out our [API Guide](http://TODO.com).  Create a file in `app/res/values/` called `sdk_strings.xml`.

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string name="x_access_token">your-access-token-here</string>
    </resources>
```

_For this guide we'll use a sample access token, which accesses NASA content._

To receive this access token, [sign up for our beta program here](https://www.oddnetworks.com/).

We strongly recommend adding any file that contains sensitive information to your project's `.gitignore` file. This access token should be kept private and not checked in to version control. Anyone who has your access token will be able to fetch your data.
