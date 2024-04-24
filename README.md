![image](https://github.com/KonraW/PhoneBookApp/assets/55025128/634fe5f3-74d0-4cfc-9618-5a18a7784156)
![image](https://github.com/KonraW/PhoneBookApp/assets/55025128/c2670075-3686-4de2-852a-84a3e848b186)
![image](https://github.com/KonraW/PhoneBookApp/assets/55025128/884e1680-d714-4083-92d3-d674adb81d4c)

![image](https://github.com/KonraW/PhoneBookApp/assets/55025128/3ff411c6-f798-4f98-b470-4e21128fc453)
![image](https://github.com/KonraW/PhoneBookApp/assets/55025128/93b67cdb-bb3e-450d-ab76-2005e8ec96ae)


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <h1>PhoneBookApp</h1>
    <p>PhoneBookApp is a set of tools for managing a phone book on the Android platform. The application allows users to add, edit, and delete contacts, providing an intuitive user interface and rich features.</p>
    <h2>Key Features</h2>
    <ul>
        <li>Adding a new contact: Users can enter personal data such as first name, last name, email address, contact categories, etc. It's also possible to add a contact photo using the option to add a photo using uri.</li>
        <li>Managing phone numbers: The application allows adding different phone numbers for a single contact, and users can specify the type of each number, e.g., home, work, mobile, etc.</li>
        <li>Data validation: There is a data validation mechanism that informs the user about errors when entering data, such as an incorrect phone number format or email address.</li>
        <li>Deleting contacts: Users can delete contacts from the phone book.</li>
        <li>Viewing contacts: The application provides a preview of contacts, allowing users to browse and edit existing entries.</li>
    </ul>
    <h2>Technologies Used</h2>
    <ul>
        <li>ViewModel: ViewModel is part of the Android Jetpack architecture and is used to store and manage data related to the user interface and business logic of the application.</li>
        <li>Compose UI: Jetpack Compose library was used to create the user interface in a declarative and functional programming way.</li>
        <li>Navigation Component: The Android Jetpack navigation component was used to manage navigation between different screens of the application.</li>
        <li>Room Database: Room is a Jetpack library used to work with the SQLite database in a higher level of abstraction.</li>
        <li>Coroutine: Kotlin Coroutines are used for asynchronous execution of network operations and database operations.</li>
        <li>SavedStateHandle: SavedStateHandle is used to pass data between different stages of the application lifecycle.</li>
        <li>URI: URI is utilized for adding photos without accessing file permissions directly, using a flag in the URI.</li>
    </ul>
</body>
</html>
