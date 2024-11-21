# Airbnb Clone App  

A simple offline Android application built using Kotlin and XML as part of a learning exercise. This app mimics basic functionalities of an Airbnb-like platform but is not intended for practical use. It focuses on implementing key Android development concepts like database management, Google Maps integration, and custom ListView adapters.  

---

## Table of Contents  
- [Features](#features)  
- [Technology Stack](#technology-stack)  
- [Modules](#modules)  
- [Setup and Installation](#setup-and-installation)  
- [License](#license)  

---

## Features  
- Add property details with a name, description, and image.  
- View properties in a list.  
- Mark properties as favorites and view them separately.  
- Display property locations on Google Maps.  
- Offline storage using SQLite.  

---

## Technology Stack  
- **Programming Language:** Kotlin  
- **UI Design:** XML  
- **Database:** SQLite  
- **Location Services:** Google Maps SDK for Android  

---

## Modules  

### MainActivity  
- Displays all properties in a `ListView`.  
- Search functionality for properties.  
- Navigation to the Favorites section.  

### AddProperty Activity  
- Allows users to add a new property with name, description, and image.  
- Saves property details to the SQLite database.  

### FavProperty Activity  
- Displays a list of favorite properties marked by the user.  

### PropDetails Activity  
- Shows detailed information about a selected property.  
- Integrates Google Maps for location display.  

### Utility and Adapter Classes  
- **BitmapUtil:** Handles Bitmap conversion for storing and retrieving images in SQLite.  
- **FavPropAdapter and PropertyAdapter:** Custom adapters for binding property data to `ListView`.  
- **PropsModel:** Data model class for property details.  
- **PropertyDB:** Manages SQLite database operations like insert, fetch, and update.  

---

## Setup and Installation  

1. Clone the repository:  
   ```bash
   git clone https://github.com/mandalabhash/airbnb-clone-app.git
   ```  
2. Open the project in **Android Studio**.  
3. Build and sync the Gradle files.  
4. Generate a Google Maps API Key and update the `AndroidManifest.xml` file.  
5. Run the application on an emulator or a physical device.  

---

## License  
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.  

---

## Acknowledgments  
- **Google Maps SDK** for location integration.  
- Android development documentation and guides.  
- SQLite for lightweight database management.  


---
