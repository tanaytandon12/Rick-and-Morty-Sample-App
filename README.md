* [Overview](#Overview)
* [Project Archiecture](#Project-Architecture)
* [Libraries Used](#Libraries)

### Overview

This project is an Android application that displays a list of all characters in the show Rick and Morty.
Clicking on a character takes the user to the character details page.
The API used to fetch the data is the [Rick And Morty API](https://rickandmortyapi.com/)
Download the [Sample APK](app-prod-debug.apk) or checkout the [Screenshots folder](screenshots/)

### Project-Architecture

The project is divided into three layers

  1. Model layer
  2. Repository layer
  3. Feature layer

**Model Layer** has the object classes used to represent the characters and the episodes.
 There are two types of models:

   1)    Local models. Represent the way in which data is persisted locally.

2) Network models. Represent the network API data.
These are further divided into:

        a) Network Response Body
        b) Network Request Body


**Repository layer** is responsible for fetching the data and supplying it to the feature layer. The repository layer is
internally dependent on:

   1. Network Layer
   2. Local database layer.


**Feature layer** is the User Interface layer. The repository layer provides data to the feature layer


### Libraries

The application uses the following libraries:

1) [Retrofit](https://github.com/square/retrofit) is used for network operations. [Character list endpoint](https://rickandmortyapi.com/api/character/?page=1)
    is used to fetch the data. The endpoint returns a list of 20 characters at one time.

2) [Room](https://developer.android.com/topic/libraries/architecture/room) is used to persist the data locally.

3) [Paging library](https://developer.android.com/topic/libraries/architecture/paging) is used to fetch the information for the character list page in a paginated manner.

4) [Glide](https://github.com/bumptech/glide) is used for fetching and caching images.

5) [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) is used to hold the data associated with the UI.

6) [Espresso](https://developer.android.com/training/testing/espresso) is used for UI unit testing