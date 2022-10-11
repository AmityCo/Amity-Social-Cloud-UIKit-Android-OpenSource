# Amity-Social-Cloud-UIKit-Android

## Noom Fork setup
To make the sample buildable, several changes were made:
- created a dummy [Firebase project](https://console.firebase.google.com/project/amity-android-sample/overview "Firebase project"), added `google-services.json` to the project and removed it from `.gitignore`
- moved Jitpack repository to the bottom which fixes an issue where dependency that was moved from Jitpack to MavenCentral cannot be found. Jitpack is deprecated dependency repository and it makes sense to search for dependencies there last
- updated applicationId from the sample to match the applicationId from the `google-services.json`

The above changes made the sample buildable which enables us to verify that things work as we're developing them, without having to integrate the library with the main project.

## General
Our UI Kits include user interfaces to enable fast integration of standard
Amity Chat and Amity Social features into new or existing applications.

<img width="928" alt="Screen Shot 2564-11-22 at 08 29 57" src="https://user-images.githubusercontent.com/9884138/142821262-aab24859-68a6-45fe-a94f-3cd3a679b0ee.png">
<img width="897" alt="Screen Shot 2564-11-22 at 08 30 03" src="https://user-images.githubusercontent.com/9884138/142821272-cf46e2c6-9963-4b90-85ed-274ccc820756.png">
<img width="969" alt="Screen Shot 2564-11-22 at 08 30 19" src="https://user-images.githubusercontent.com/9884138/142821280-aebff2ab-6c94-42ff-a16d-c4d17f4a9d74.png">
