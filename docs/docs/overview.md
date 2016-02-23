# About Oddworks

Oddworks is, at its core, a Video Management System (VMS). It provides a central location to organize media and configure the devices that serve it.

## Oddworks Entities

Oddworks has a handful of specific entity types that are used in concert to construct media applications. No matter which Online Video Provider (OVP) is used to provide media to Oddworks, the data will be massaged into one of these standardized objects. Our thought is that consumer applications should not need to change when adding a new OVP or changing where a video is stored.

### config

The `config` entity will contain device-specific configuration data.

### video and liveStream

The `video` and `liveStream` entities are the media objects that are the meat and potatoes of Odd Networks. If the media being served is a linear or live stream, it will be of type `liveStream`. If the media is an on-demand stream then it will be of type `video`.

### promotion

The `promotion` entity is an object that is essentially the same as a `video` or `liveStream` without the stream.

### collection

A `collection` entity is an object that contains other entities.

### view

A `view` entity is an object that can contain other entities through user-defined custom relationships.


For a more in-depth look at all entities that you can interact with through the SDK and their properties, [look here](/data_models).