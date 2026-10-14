# Knitster Android

Native Android application for keeping track of knitting projects and row counters.

## Features

- Create and manage knitting projects.
- Add notes and choose an icon for each project.
- Add multiple row counters to a project.
- Increment, decrement, reset and reorder row counters.
- Set an optional target for each row counter.
- Light and dark mode.
- Multiple colour themes.
- English and Dutch language support.
- Option to keep the screen awake while using the app.
- Persist projects and settings locally on the device.

## Technical Overview

- Kotlin.
- Jetpack Compose with Material 3.
- MVI architecture using `StateFlow`.
- Room for local database persistence.
- DataStore for application settings.
- Hilt for dependency injection.
- Navigation using Navigation Compose.
- Kotlin Coroutines and Flow for asynchronous and reactive data handling.

## Screenshots

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_home-empty.png?raw=true" alt="Empty project list screenshot" width="300" />

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_home.png?raw=true" alt="Project list screenshot" width="300" />

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_project.png?raw=true" alt="Project screenshot" width="300" />

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_add-counter.png?raw=true" alt="Add row counter screenshot" width="300" />

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_set-icon.png?raw=true" alt="Project icon picker screenshot" width="300" />

<img src="https://github.com/kmartin0/assets/blob/master/knitster-android/knitster-android_settings.png?raw=true" alt="Settings screenshot" width="300" />
