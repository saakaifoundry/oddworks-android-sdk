# OddObject

The `OddObject` class is a superclass of many other data models in the SDK. It's children include `OddView`, `OddCollection`, `Media`, `Article`, `Event`, `External`, and `Promotion`. It provides many convienence methods that help you sort through data received from the API such as `getIncluded`, `getIncludedByType`, `getIncludedByRelationship`, `getIdentifiersByRelationship`, and `findIncludedByIdentifier`. These getter methods can return any combination of `OddObject`'s subclass objects depending on how you've configured your data on the Oddworks dashboard.


# OddView

The `OddView` class makes it possible to retreive and store large amounts of data from the API with a single call. This view contains "relationships" and "included" pieces of data that can be accessed via `OddObject`'s convienence methods. When constructing applications you may find it helpful to package things such as your navigation and the initial data shown on application load into an `OddView`.

# OddCollection

The `OddCollection` class makes it possible to group together other entities for explicit display. Depending on how you configure your Collection on the Oddworks dashboard it can contain a description, a preview image, a release date, a title, an ID, a related `Identifier` object, relationships, included `OddObject`s, and a type.


# Media

The `Media` class can represent any type of playable media to users. This could be a VOD, HLS Stream, DASH Stream, etc. Depending on how you configure your Media on the Oddworks dashboard it can contain a description, a duration, ad support information, a preview image, an explicitly stated player to use, a release date, sharing information, a title, a URL, an ID, a related `Identifier` object, relationships, included `OddObject`s (such as related videos), and a type.

# Article

The `Article` class represents a news item that can be displayed in the application. Depending on how you configure your Article on the Oddworks dashboard it can contain a description, a title, a preview image, a URL to link to, a category, a source, a created at timestamp, a related `Identifier` object, relationships, included `OddObject`s (such as related articles), and a type.

# Event

The `Event` class is similar to the `Article` class but it is used specifically for informing users of events that will or have occured. Depending on how you configure your Event on the Oddworks dashboard it can contain a description, a title, a preview image, a URL to link to, a category, a source, a created at timestamp, a start time, an end time, a location, a related `Identifier` object, relationships, included `OddObject`s (such as related events), and a type. There is also the convienence methos of `isMultiDayEvent` in case you need to update your UI based on datetime formatting.

# External

The `External` class is used to handle any webpage you'd like to embed in your application. Depending on how you configure your External on the Oddworks dashboard it can contain a description, a title, a preview image, a URL to link to, a related `Identifier` object, relationships, included `OddObject`s (such as related externals), and a type.

# Promotion

The `Promotion` class is used to handle any type of promotion you may want to put in the application. Depending on how you configure your Promotion on the Oddworks dashboard it can contain a description, a title, a preview image, a related `Identifier` object, relationships, included `OddObject`s (such as related externals), and a type.