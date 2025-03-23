### Running the Backend Locally:

1. Ensure **Maven** and **Java 21** are installed on your system:

   - [Install Maven](https://maven.apache.org/install.html)
   - [Install Java 21](https://adoptopenjdk.net/)

2. Build the project using Maven:

   ```bash
   mvn clean install
   ```

3. Start the backend server:

   ```bash
   mvn spring-boot:run
   ```

4. Make requests to: http://localhost:8080

### Configuring Tesseract OCR:

1. Ensure **Tesseract** is installed on your system:

   - [Install Tesseract](https://github.com/UB-Mannheim/tesseract?tab=readme-ov-file#installing-tesseract)

2. Add the tessdata folder to your TESSDATA_PREFIX environment variable:

   - For Windows:
     - Open Environment Variables settings
     - Add a new user variable called TESSDATA_PREFIX
     - Set its value to C:\Program Files\Tesseract-OCR\tessdata\ (or the location where Tesseract is installed)

3. Optionally, add Tesseract to your system PATH:

   - For Windows:
     - Open Environment Variables settings
     - Select the PATH environment variable
     - Add C:\Program Files\Tesseract-OCR\ (or the location where Tesseract is installed)
