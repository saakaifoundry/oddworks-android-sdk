Android SDK for Oddworks
========================

[![slack.oddnetworks.com](http://slack.oddnetworks.com/badge.svg)](http://slack.oddnetworks.com)

An [Oddworks](https://github.com/oddnetworks/oddworks) device client SDK for Android Mobile, Tablet, Amazon Fire TV, and more. Check out [Odd Networks](https://www.oddnetworks.com/) for more information.

## Download

[![Download](https://api.bintray.com/packages/oddnetworks/maven/device-sdk/images/download.svg)](https://bintray.com/oddnetworks/maven/device-sdk/_latestVersion) or grab via Maven:

```xml
<dependency>
  <groupId>io.oddworks</groupId>
  <artifactId>device-sdk</artifactId>
  <version>{version}</version>
</dependency>
```

or Gradle:

```
repositories {
    maven {
        url 'http://oddnetworks.bintray.org/maven'
    }
}

dependencies {
    compile 'io.oddworks:device-sdk:{version}'
}
```

## Overview

At a high level, this SDK wraps the Oddworks API with an OkHttp client, parsing the [JSON API format](http://jsonapi.org/format/) into helpful `OddResource` objects.

### Configuring device-sdk

You will need to configure a few pieces of application meta-data in `AndroidManifest.xml` to get started.

First, you will need to specify the device-specific JSON Web Token (JWT) given by Oddworks.

```xml
<application>
    <meta-data
        android:name="io.oddworks.configJWT"
        android:value="the-device-specific-jwt-given-by-the-oddworks-server" />
</application>
```

Then, if you are using the enterprise Oddworks content service, you will need to add the `io.oddworks.apiBaseURL` meta-data. If you leave this out, the default endpoint will be used. See `Oddworks.DEFAULT_API_BASE_URL`.

```xml
<application>
    <meta-data
        android:name="io.oddworks.configJWT"
        android:value="the-device-specific-jwt-given-by-the-oddworks-server" />
    <meta-data
        android:name="io.oddworks.apiBaseURL"
        android:value="https://path-to-your-oddworks.com/version" />
</application>
```

Finally, if you are using the enterprise Oddworks analytics service, you will need to add the `io.oddworks.analyticsApiBaseURL` meta-data. If you leave this out, the default endpoint will be used. See `Oddworks.DEFAULT_ANALYTICS_API_BASE_URL`.

```xml
<application>
    <meta-data
        android:name="io.oddworks.configJWT"
        android:value="the-device-specific-jwt-given-by-the-oddworks-server" />
    <meta-data
        android:name="io.oddworks.apiBaseURL"
        android:value="https://path-to-your-oddworks-content-service.com/version" />
    <meta-data
        android:name="io.oddworks.analyticsApiBaseURL"
        android:value="https://path-to-your-oddworks-analytics-service.com" />
</application>
```


### Requesting Data

We need to build an `OddRequest` and enqueue it, passing along an `OddCallback`.

`OddRequest.Builder` can be used to do this. Minimally, pass the current application context and an `OddResourceType`

```java
// Create the OddCallback instance
OddCallback<OddConfig> configCallback = new OddCallback<OddConfig>() {
    @Override
    public void onSuccess(OddConfig resource) {
        // do what you need to do with the OddConfig resource
        Log.d(TAG, "request successful - id: " + resource.getIdentifier().getId());
    }

    @Override
    public void onFailure(@NotNull Exception exception) {
        Log.e(TAG, "request failed", exception);

        // if non 200 status code, Exception will be a BadResponseCodeException
        if (exception instanceof BadResponseCodeException) {
            // Useful for determining cause of bad response
            LinkedHashSet<OddError> errors = ((BadResponseCodeException) exception).oddErrors;
            Log.d(TAG, "parsed server errors: " + errors);
        }
    }
};

// Create the OddRequest instance
OddRequest request = new OddRequest.Builder(context, OddResourceType.CONFIG).build();

// Build and enqueue the OkHttp.Call using OddRequest and OddCallback
request.enqueueRequest(configCallback);
```

You can also wrap this in an `RxOddCall` if you are into [RxJava](https://github.com/ReactiveX/RxJava)

```java
RxOddCall
    .observableFrom(new Action1<OddCallback<OddConfig>>() {
        @Override
        public void call(OddCallback<OddConfig> oddCallback) {
            new OddRequest.Builder(context, OddResourceType.CONFIG)
                    .build()
                    .enqueueRequest(oddCallback);
        }
    })
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Action1<OddConfig>() {
        @Override
        public void call(OddConfig oddConfig) {
            // do what you need to do with the OddConfig resource
            Log.d(TAG, "request successful - id: " + resource.getIdentifier().getId());
        }
    }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Log.e(TAG, "request failed", throwable);

            // if non 200 status code, Throwable will be a BadResponseCodeException
            if (throwable instanceof BadResponseCodeException) {
                // Useful for determining cause of bad response
                LinkedHashSet<OddError> errors = ((BadResponseCodeException) throwable).oddErrors;
                Log.d(TAG, "parsed server errors: " + errors);
            }
        }
    });
```

### Authentication

This SDK offers an `OddAuthenticator` service that can be configured to handle authentication via Oddworks.

An authentication workflow can also be manually created via `OddRequest.Builder`.

#### OddAuthenticatorService

__Step 1:__ `AndroidManifest.xml`

To utilize OddAuthenticatorService, you must first declare it within the `<application>` block of `AndroidManifest.xml`

```xml
<application>
  <activity android:name="io.oddworks.device.authentication.OddAuthenticationActivity" 
              android:label="@string/login_label" />
  <service android:name="io.oddworks.device.authentication.OddAuthenticatorService">
      <intent-filter>
          <action android:name="android.accounts.AccountAuthenticator" />
      </intent-filter>
      <meta-data android:name="android.accounts.AccountAuthenticator"
                       android:resource="@xml/oddworks_authenticator" />
  </service>
</application>
```

There are also some permissions needed. Depending upon the SDK level you are targeting, you may need to request these permissions at runtime.

```xml
<uses-permission android:name="android.permission.USE_CREDENTIALS"/>
<uses-permission android:name="android.permission.GET_ACCOUNTS"/>
<uses-permission android:name="android.permission.MANAGE_ACCOUNTS"/>

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS" />
```

__Step 2:__ `res/strings.xml`

Next, you should override a few strings in your application's `res/strings.xml` file.

- `@string/oddworks_account_type` - This should be a string that is unique to your application and will distinguish your Accounts from others on the device.
- `@string/oddworks_account_label` - This will be the label displayed when listing the device's Accounts.

!["OddSample"](http://oddworks-android-sdk.s3.amazonaws.com/android-device-account-list.png)

__Step 3:__ `res/mipmap/ic_launcher.png|xml`

Be sure that you have an `ic_launcher` icon set in your application's `res/mipmap` directory(ies). This will be the icon displayed when listing the device's Accounts.

#### OddAuthenticationActivity

When creating a new Account with OddAuthenticationService, an OddAuthenticationActivity is started, which you will need to declare this activity within your `AndroidManifest.xml`:

![OddAuthenticationActivity](http://oddworks-android-sdk.s3.amazonaws.com/android-device-odd-authentication-activity.png)

There are several customizable strings within this view.

```xml
<AutoCompleteTextView
    android:id="@+id/account_email"
    ...
    android:hint="@string/prompt_email"
    />

<EditText
    android:id="@+id/account_password"
    ...
    android:hint="@string/prompt_password"
    android:imeActionLabel="@string/action_sign_in_short"
    />

<Button
    android:id="@+id/email_sign_in_button"
    ...
    android:text="@string/action_sign_in"
    />

<TextView
    android:id="@+id/account_message"
    ...
    android:text="@string/account_message"
    />
```

In addition to the strings, the view's style can be overridden via `@style/AppTheme`

#### Handling Accounts

Ultimately, how you choose to handle your Account(s) is up to you.

Authenticating an OddRequest with an Account can be done using the `OddRequest.Builder` function `account(Account)`.

If the Account's `authToken` is valid, it will be used to make the request.

If the Account's `authToken` is invalid or missing, the user will be prompted to reauthenticate.

If the Authenticated request responds with a `401` code, the Account's `authToken` is invalidated.

### Enabling Analytics

There are two ways of sending analytics metrics.

#### Sending OddMetric requests manually

Note: Be careful about making requests on the main thread.

```java
// from within your activity

OddCallback<OddMetric> metricCallback = new OddCallback<OddMetric>() {
    @Override
    public void onSuccess(OddMetric resource) {
        Log.d(TAG, "handleOddMetric: SUCCESS $resource}")
    }

    @Override
    public void onFailure(@NotNull Exception exception) {
        Log.d(TAG, "handleOddMetric: FAILURE $exception")
        if (exception is BadResponseCodeException) {
            Log.d(TAG, "handleOddMetric code: ${exception.code} errors: ${exception.oddErrors}")
        }
    }
};

OddRequest.Builder(context, OddResourceType.EVENT)
                        .event(event)
                        .build()
                        .enqueueRequest(oddMetricCallback)
```

#### Sending OddMetric requests via the OddRxBus

You will first need to enable the OddMetricHandler. A good place to do this is in your Application class.

```java

public class YourApp extends Application {
  @Override
  public void onCreate() {
      super.onCreate();

      // Enable handling of published analytics events
      OddMetricHandler.enable(this);
  }
}
```

Using the OddMetricHandler ensures the POST requests are sent on an IO Scheduler thread.

Next, when you are ready to enqueue an OddMetric request, simply use the `OddRxBus.publish()` function.

```java

// from within an activity
OddAppInitMetric metric = new OddAppInitMetric();

OddRxBus.publish(metric);
```

## Contributing

If you would like to contribute code you can do so through GitHub by forking the repository and sending a pull request.

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

## License

Apache 2.0 © [Odd Networks](http://oddnetworks.com)
