# Amity-Social-Cloud-UIKit-Android

## Noom Fork setup
To make the sample buildable, several changes were made:
- created a dummy [Firebase project](https://console.firebase.google.com/project/amity-android-sample/overview "Firebase project"), added `google-services.json` to the project and removed it from `.gitignore`
- moved Jitpack repository to the bottom which fixes an issue where dependency that was moved from Jitpack to MavenCentral cannot be found. Jitpack is deprecated dependency repository and it makes sense to search for dependencies there last
- updated applicationId from the sample to match the applicationId from the `google-services.json`

The above changes made the sample buildable which enables us to verify that things work as we're developing them, without having to integrate the library with the main project.

## Github packages setup
This repo is published and consumed via [Github Packages](https://github.com/features/packages "Github Packages"). Created packages can be found [here](https://github.com/orgs/noom/packages?repo_name=Amity-Social-Cloud-UIKit-Android-OpenSource "here").

The publishing script is defined in [publish.gradle](/publish.gradle) script in the root of the project and is greatly inspired by [this blog](https://apiumhub.com/tech-blog-barcelona/publishing-multi-module-android-libraries/#Publishing_multi_module_Android_libraries).
The script generates and publishes a Github package for each module in the project, while excluding the "android application" module (`sample`). It covers both java and android library modules while also attaching the source code. This allows the consumer of the library to be able to browse the source code. The script can be executed by running the associated `publish` task from the IDE GUI or with `./gradlew publish` from the terminal.

For authentication we use the existing setup that's already part of [Android Build Setup](https://github.com/noom/mobile-handbook/blob/master/android/build-setup.md#setup-github-packages "Android Build Setup") in the mobile handbook. This means that every Android engineer should already have authentication set up.
**Note:** The linked setup is focused on the consumer so the token that you create with it will only have the scope `read:packages`. This token will work just fine when consuming the fork from the main Android project.  If you also want to publish new packages, you will need to extend the scope to `write:packages`.

### Git setup
Note that there are two different git remotes: `fork` and `origin`.
- `origin` points to the original Github repo
- `fork` points to this Github repo

The `fork` remote is set as default so executing `git pull` will attempt to pull the changes from the `fork` remote.
When we want to pull the changes from `origin` we can do so with `git pull origin`.

## General
Our UI Kits include user interfaces to enable fast integration of standard
Amity Chat and Amity Social features into new or existing applications.

<img width="928" alt="Screen Shot 2564-11-22 at 08 29 57" src="https://user-images.githubusercontent.com/9884138/142821262-aab24859-68a6-45fe-a94f-3cd3a679b0ee.png">
<img width="897" alt="Screen Shot 2564-11-22 at 08 30 03" src="https://user-images.githubusercontent.com/9884138/142821272-cf46e2c6-9963-4b90-85ed-274ccc820756.png">
<img width="969" alt="Screen Shot 2564-11-22 at 08 30 19" src="https://user-images.githubusercontent.com/9884138/142821280-aebff2ab-6c94-42ff-a16d-c4d17f4a9d74.png">
