# Diary App
This is a simple and minimalistic diary app developed with Java that keeps basic journal entries secure.
Designed to run in Android Studio Bumblebee 2021.1.1 emulated within Pixel_3a_API_30_x86.

## About
The app starts with a loading screen featuring a notebook emoji and the text ‘diary’ which prompts a sign-in/sign-up window. 
The user can then fill out the needed information and hit submit to access the diary dashboard screen. 
From here a user can see their recent entries, create a new entry, or search for previous entries. 
The user can also select each entry to view or modify the contents. 
Selecting ‘New Entry’ will prompt the user to create a new entry by providing a title/subject and the entry contents. 
Clicking the trash icon will reset the text boxes while clicking the back button will save the entry if all needed information is provided. 
Clicking on an existing entry will allow a user to edit the title/subject and contents of said entry. 
Pressing the trash icon now will delete the entry. Selecting the back button will update the entry if anything was changed. 
The search bar will update the entry list based on if the user provided search term was found in any entry (either in the subject or the content).

## Design
The app uses an SQLite database to manage entries created by the user. 
These entries are displayed using a custom entry adapter for the list view. 
Entries can be selected by way of an OnItemClickListener attached to the list view. 
The search bar incorporates a TextChangedListener to update the entry list based on a user’s search term. 
This entry list is what the list view and adapter reference to display the entries. 
The login screen is handled through a SharedPreferences object and displayed the corresponding Dialog box based on whether a password exists or not. 
A custom DBHelper (Database Helper) class handles the internal workings of processing the SQLite database and calling queries based on the needed information. 

## Screenshots
Basic loading screen
<img src="screenshots/loading_screen.png", alt="Loading screen", width="540", height="1110" />
![Loading screen](screenshots/loading_screen.png?raw=true)

Upon first start, a password creation popup is displayed
![Registration screen](screenshots/register_screen.png?raw=true)

Otherwise, a password login popup is displayed
![Login screen](screenshots/login_screen.png?raw=true)

Once logged in, a dashboard appears
![Dashboard screen](screenshots/dashboard_screen.png?raw=true)

From here, one can create entries
![Entry screen](screenshots/entry_screen.png?raw=true)

Entries are shown in order of creation
![Entries screen](screenshots/entries_screen.png?raw=true)
And most recently updated
![Updated screen](screenshots/updated_screen.png?raw=true)

A user can also search within entries
![Search screen](screenshots/search_screen.png?raw=true)
