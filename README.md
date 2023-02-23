# MeetMap
Application that allows you to see the next activities in your city and meet people on them. This project will be made by Almudena Fernandez, Iker Iturralde and Nerea Ramos for the modules: "Desarrollo de Interfaces" and "Programación Multimedia y dispositivos Móviles"

## User manual

#### Welcome to MeetMap, an application that will allow you to meet a lot of people doing really interesting plans in Madrid.

##### The app is simple, the first time that you open it, u will have to choose between login or sing up, since the app has a carousel with the posibilities of the same
![Initial](img/initial.png)


##### The following times that you start the app, you will be automaticaly directed to the map, the principal function of MeetMap
![Map](img/map.png)

In this map, u will see the nearest activities in the zone (actually just in Madrid). You can select any and sing up to do them.
Additionaly, the user will be able to search something in a bar, or filter the activities.
Once the user select one of the activities, he will see something like that:


![Activity](img/activity.png)


As you can see, the activity will show some info, a button for save it and another button which show the people who is already sing up. If you click that button, you will see a list with that people, something like that:


![List](img/List.png)

The user can click in any of this people to chat with them, of course, any user can block his appearence in this list.


Finally, back in the map, we can see:
##### The like button, where you can see the plans that you has selected
##### The chat button, where you can see the chats with other people
##### A slide menu, where the user has a lot of posibilities, like that:
![Slide](img/slide.png)

Also, you can see an example of edit your profile.

## [Figma prototype](https://www.figma.com/file/BnFUxtnABdXq7QyjVwtNXr/MeetMap?node-id=58%3A35&t=M3GuCny9oMMSULwz-1)


![Figma prototype](img/figma_prototype.png)

## [Splash View](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/Splash.kt)


[Splash](https://www.youtube.com/shorts/q-YjV9VqvEQ) view with two animations of two hands coming together.


![Splash Gif](img/splash_gif.gif)


## [Initial View](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/Initial.kt)


[Initial carousel](https://www.youtube.com/shorts/cyRHQYf8o8I) that starts the app with information about the possibilities of the application.


![Initial Gif](img/initial_gif.gif)


## Login and Sign Up


Login and SignUp connected to the database and with verification of the fields. Option to sign up and log in with Google account


[![Login](img/Login.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/LoginScroll.kt) [![SignUp](img/signUp.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/SignUpScroll.kt)


## [Fragments Main View](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/MainAppActivity.kt)


[Main view of the application](https://www.youtube.com/shorts/GnJL-nRWt7w).  A map that through the location allows you to know the plans that are nearby. A bar with buttons that allows navigation between the different possibilities of the app: favorites, chat, profile.


![Fragments Gif](img/fragments_gif.gif)


Connects with device location for geolocation:


[![Map](img/ubicacion.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/MapFragment.kt)

Each marker has information that can be consulted:


[![Map](img/mapApp.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/MapFragment.kt) [![Map Activity](img/mapActivity.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/InfoActivityFragment.kt)


The user's profile is a [scrollable view](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/EditProfileFragment.kt) it has a rounded photo of the person, fields with their personal data and two buttons: cancel and save.


### [Edit profile](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/EditProfileFragment.kt)


![Edit Profile](img/editProfile.png)


The information and the photo can be edited and the changes are observed in that same view and in the drop-down side menu


[![Navigation View first photo ](img/navViewEspFoto1.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/res/layout/activity_main_app.xml)  [![Navigation View photo changed](img/navViewEsp.png)](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/MainAppActivity.kt)

``` kotlin
email = PreferencesManager.getDefaultSharedPreferences(binding.root.context).getEmail()
         emailuser.setText(email)
         fStore.collection("users").document(email).get().addOnSuccessListener {
             username.setText(it.get("name") as String)
             //binding.mail.setText(email)
             Glide.with(this)
                 .load(it.get("img")as String)
                 .circleCrop()
                 .into(imagenav)
         }
```


### [Faqs](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/FaqsFragment.kt)


![Faqs Fragment](img/Faqs.png)


### [ContactUs](https://github.com/leoneliker/MeetMap/blob/master/app/src/main/java/com/ikalne/meetmap/fragments/ConctactUsFragment.kt)


![ContactUsFragment](img/contactEng.png)


## Languages


The app is available in English and Spanish:


![Sign up in English](img/signUp.png)![Registro in Spanish](img/registroEsp.png)


![Navigation View in English](img/navViewEng.png)![Navigation View in Spanish](img/navViewEsp.png)


![Contact us in English](img/contactEng.png)![Contactanos in Spanish](img/contactoEsp.png)



[MeetMap on Youtube](https://www.youtube.com/@meetmap)


**App designed and created by Almudena Fernández Cárdenas, Iker Iturralde Tejido y Nerea Ramos Escobar**

>This repository is licensed under
>[Creativecommons Org Licenses By Sa 4](https://creativecommons.org/licenses/by-nc-sa/4.0/)





