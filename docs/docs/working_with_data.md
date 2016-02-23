# Getting Information from Views

## Configure Data

In our sample application, we've pulled down an `OddView` and called it `homeView`. In order to extract information from it we'll use some convienence methods from the superclass of `OddView`, `OddObject`. Before we do that, we'll need to setup some variables in our `HomeActivity`.

```java
// app/java/sample.oddworks.com.myoddworksapplication/HomeActivity

// ...
public class HomeActivity extends AppCompatActivity {
    // Constants
    private static final String FEATURED_MEDIA = "featuredMedia";
    private static final String FEATURED_COLLECTION = "featuredCollections";
    private static final String FEATURED_ENTITIES = "entities";

    // Data
    private Context context;
    private OddView homeView;
    private Media featuredMedia;
    private OddCollection featuredCollection;
    private List<OddObject> featuredCollectionEntities;

    // ...
```
    
Now, in our `onCreate` method after `setContentView` is called, we need to set our `context`.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    context = this;
}
```

After setting our context we'll call a new method called `configureData`.

```java
// app/java/sample.oddworks.com.myoddworksapplication/HomeActivity

// ...
private void configureData() {
    homeView = OddApp.getInstance().getHomeView();
    List<Identifier> mediaIds = homeView.getIdentifiersByRelationship(FEATURED_MEDIA);
    List<Identifier> collectionIds = homeView.getIdentifiersByRelationship(FEATURED_COLLECTION);

    if (mediaIds != null && !mediaIds.isEmpty()) {
        Identifier featuredMediaId = mediaIds.get(0);
        featuredMedia = (Media) OddStore.getInstance().getObject(featuredMediaId);
    }
    if (collectionIds != null && !collectionIds.isEmpty()) {
        Identifier featuredCollectionId = collectionIds.get(0);
        featuredCollection = (OddCollection) OddStore.getInstance().getObject(featuredCollectionId);
    }
    if (featuredCollection != null) {
        featuredCollectionEntities = featuredCollection.getIncludedByRelationship(FEATURED_ENTITIES);
    }
}
// ...
```

There's a lot going on here, so let's take a minute to walk through the code. We are first getting the `homeView` from our main application class. In the Oddworks dashboard we've set up relationships on this View with the keys of "featuredMedia and "featuredCollections". We use the `getIdentifiersByRelationship` so that we can use the `OddStore`'s `getObject` method to retrieve the featured media and featured collection objects later in the method. We check to make sure that each List of Identifiers is not null or empty and grab the object from the `OddStore`. We take this a step further with the `getIncludedByRelationship` method called on the `featuredCollection`. This will give us a list of `OddObject`s that can be used in a RecyclerView. *Note: We're casting the objects in the `featuredCollectionEntities` to `OddObject`s because it may contain any combination of data types.*

## Apply Data

### Displaying a Preview Image
Now that we've extracted the data we need from the `homeView` we're going to show the user the preview image for the `featuredMedia` object. In order to render an image, we're going to rely on the [Glide](https://github.com/bumptech/glide) library, though there are many other options such as [Picasso](https://github.com/square/picasso), [ImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader), and [Fresco](https://github.com/facebook/fresco). Add the Glide library to your project via Gradle or Maven.


After calling the `configureData` method, we're going to call a new method called `configureView`.

```java
// app/java/sample.oddworks.com.myoddworksapplication/HomeActivity

// ...
private void configureView() {
    ImageView featuredMediaPreviewImage = (ImageView) findViewById(R.id.featuredMediaPreviewImage);
    Glide.with(context)
            .load(featuredMedia.getMediaImage().getAspect16x9())
            .placeholder(R.drawable.preview_tile_16x9)
            .into(featuredMediaPreviewImage);
}
// ...
```

We've created an `ImageView` with an ID of `featuredMediaPreviewImage` in the `activity_home` layout file that is being targeted in the `configureView` method. We want to use an image with an aspect ratio of 16x9, although we do have other options. You'll also notice that we have set a placeholder via a drawable file. This is best practice because it allows for the view layout to have the correct structure on load instead of changing it after Glide has loaded the image from the `featuredMedia` object.

### Creating a Dynamic Recycler View

Since the `featuredCollectionEntities` list can contain different types of `OddObject`s we'll need to create a dynamic `RecyclerView` to handle anything that gets put into it. For this example, we'll handle the case of the List containing `Media` and `OddCollection` objects. In order to make use of the `RecyclerView` class we'll need to add it to our dependencies in our `build.gradle` file:

    dependencies {
        // ...
        compile 'com.android.support:recyclerview-v7:+'
    }

We'll add a `RecyclerView` to our `activity_home` layout file and then add to our `configureView` method:

```java
private void configureView() {
    // ...

    RecyclerView featuredEntitiesRecyclerView = (RecyclerView) findViewById(R.id.featuredEntitiesRecyclerView);
    OddListAdapter oddListAdapter = new OddListAdapter(featuredCollectionEntities);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    featuredEntitiesRecyclerView.setLayoutManager(layoutManager);
    featuredEntitiesRecyclerView.setAdapter(oddListAdapter);
}
```

The `OddListAdapter` doesn't currently exist, so let's create that class.

```java
// app/java/sample.oddworks.com.myoddworksapplication/OddListAdapter

package sample.oddworks.com.myoddworksapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.oddworks.device.model.Media;
import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddObject;

public class OddListAdapter extends RecyclerView.Adapter<OddListAdapter.ListItemViewHolder> {
    private List<OddObject> oddObjects;

    public OddListAdapter(List<OddObject> oddObjects) {
        super();

        this.oddObjects = oddObjects;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        OddObject oddObject = oddObjects.get(position);

        switch (oddObject.getType()) {
            case (OddObject.TYPE_LIVE_STREAM):
            case (OddObject.TYPE_VIDEO):
                Media oddMedia = (Media) oddObject;
                holder.title.setText(oddMedia.getTitle());
                break;
            case (OddObject.TYPE_COLLECTION):
                OddCollection oddCollection = (OddCollection) oddObject;
                holder.title.setText(oddCollection.getTitle());
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (oddObjects == null) {
            return 0;
        } else {
            return oddObjects.size();
        }
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listItemTitle);
        }
    }
}
```

Everything in this adapter is pretty standard. What deserves attention is the switch statement in the `onBindViewHolder`. We use the `getType` method on our `oddObject` and compare it against any type that it could be. Based on what type it is, we cast the `oddObject` to a `Media` or `OddCollection` object.