# Listo

**Listo** is a modern, user-friendly to-do list application designed to help you organize and manage your tasks efficiently. Built with the Android Jetpack Compose framework, Listo leverages cutting-edge technologies to provide seamless functionality and an engaging user experience.

---

## Features

### **Authentication**
- **Anonymous Login**: Access the app without creating an account.
- **Register**: Create a new account with ease.
- **Login**: Sign in to your existing account securely.

### **To-Do Management**
- **View Todos**: Display all your to-do items in a structured list.
  - **Edit**: Modify existing to-do items using the edit button.
  - **Delete**: Remove unwanted to-do items with the delete button.
  - **Complete/Uncomplete**: Mark tasks as complete or incomplete using a checkbox.

### **Create and Edit Todo**
- Add and update to-do items with detailed descriptions.
- **Attach Images**: Enhance tasks by adding images captured directly from your device's camera.

### **Settings**
- Manage your account settings, update profile information, and configure preferences.

---

## Tech Stack

- **Android Jetpack Compose**: The latest UI toolkit for building native Android interfaces.
- **Firebase**:
  - Authentication for identity management.
  - Firestore as a scalable cloud database.
- **Cloudinary**: Image storage solution.
- **Coil**: Image loading library.

---

## Installation

Follow the steps below to set up and run Listo on your local development environment.

### Prerequisites
- Android Studio installed on your machine.
- A Firebase project created with the appropriate configurations.

### Steps

1. **Clone the Repository**  
   Clone this repository to your local machine using:
   ```bash
   git clone https://github.com/harisfi/jetpack-firebase-listo-app.git
   ```

2. **Set Up Firebase Configuration**
    - Locate the `app/google-services.example.json` file in the project directory.
    - Copy this file and rename the copy to `app/google-services.json`.
    - Replace its contents with the configuration JSON file from your Firebase project.

3. **Set Up Cloudinary Configuration**
    - Navigate to `/app/src/main/res/raw/` directory.
    - Locate the file `config_properties.example`.
    - Copy this file and rename it to `config.properties`.
    - Update the file with your Cloudinary credentials.

4. **Build and Run the Application**  
   Open the project in Android Studio, sync the Gradle files, and run the application on an emulator or physical device.

---

## Contribution

Contributions to Listo are welcome! Please feel free to open issues, suggest features, or submit pull requests.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Author

Developed by **Haris F**.  
For inquiries or support, visit [github.com/harisfi](https://github.com/harisfi).
