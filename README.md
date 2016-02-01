# Buzzify
Two-man group project for CSCI 6907 - Handheld Devices which spawned from joining 3 separate ideas: 1) Mobile food/drink ordering system; 2) Real-time priority queues for bar/club song requests; and 3) Photo sharing. 

The combination was to create a convenient app to help club/bar patrons to request Spotify songs (ex. from a DJ) and order drinks. We are tacking two problems: 1) Patrons want to influence music choice / DJ may not always know what patrons want; and 2) Convenient drink ordering for these loud environments.

Patrons and staff (DJ / bartender) log into the app using credentials stored on a Parse backend. Patrons may vote for songs using a built-from-scratch Spotify song browser and also order drinks (which involve transmitting a user's photo over to the bartender). Drinks and requested songs are placed into a priority queue which is hosted on Firebase and can be dequeued by a DJ user or bartender user, respectively.

## Technical Features
  1. User profile creation / editing hosted by Parse.
  2. Support for multiple user roles.
  3. Song/drink priority queues maintained on Firebase and each physical location gets its own queues.
  4. Google Places API for location finding.
  5. Consistent user experience across multiple Android versions due to Support Library.
  6. JSON Fetch/Parsing via Volley and Gson.
  7. Extensive use of Fragments for queue viewing and Spotify song searching.
  8. Built-in Spotify song browser. 
  
## Quick Start
Support Android API versions 16 - 23.

Development computers will need to have their SHA1 fingerprints added to the Google Developers Console to be authenticated for the Google-based APIs used by the app. Otherwise the Google Places screen will not function properly.
