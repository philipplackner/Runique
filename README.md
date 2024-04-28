# Runique

Runique is a multi-module running tracker app for phones and Wear OS devices you can learn to build in the Android Essentials course bundle ([Get it here](https://pl-coding.com/android-essentials-bundle?utm_source=github&utm_medium=readme&utm_campaign=readme_link&utm_id=essentials)).

![Run Feature](https://pl-coding.com/wp-content/uploads/2024/04/run-feature.png)
<table>
  <tr>
    <td>
      <img src="https://pl-coding.com/wp-content/uploads/2024/04/auth-feature.png" alt="Auth Feature" width="500"/>
    </td>
    <td>
      <img src="https://pl-coding.com/wp-content/uploads/2024/04/phone-watch-mockup.png" alt="Phone Watch Mockup" width="300"/>
    </td>
  </tr>
</table>

## What's covered?

In this course bundle, you will learn these concepts/technologies:
- Project planning
- Software architecture theory
- Multi-module architecture
- Gradle for large-scale projects (version catalogs & convention plugins)
- Authentication systems (OAuth / token refresh)
- Offline-first development
- Dynamic feature modules
- Google Maps SDK
- Jetpack Compose in multi-module projects
- Wear OS development (Health services API, data sync, UI building)

## How do you run the project?

In order to run the project on your phone, you'll need to first clone it and then add two API keys for:
1. ... the Runique API (access granted after course purchase)
2. ... Google Maps (needs to be got from Google Cloud Console - instructions in the course)

Then simply include them in `local.properties`:
```
API_KEY=<RUNIQUE_API_KEY>
MAPS_API_KEY=<GOOGLE_MAPS_API_KEY>
```
Afterwards, build the project and you're ready to use it.
