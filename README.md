# Bean Machine Sim

This project demonstrates the central limit theorem using a Bean Machine simulation, also known as a Galton Board. The simulation is implemented in Java using JavaFX for the graphical interface.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 11 or later installed on your system
- JavaFX SDK (if using JDK 11 or later)
- An Integrated Development Environment (IDE) such as IntelliJ IDEA, Eclipse, or NetBeans (optional but recommended)

## Setting Up the Project

1. Clone this repository or download the source files.
2. Create a new Java project in your IDE.
3. Copy `Bean.java` and `BeanMachine.java` into your project's `src` directory, maintaining the package structure (both should be in a package called `machine`).

## Configuring JavaFX

If you're using JDK 11 or later, you need to set up JavaFX separately:

1. Download the JavaFX SDK from [https://openjfx.io/](https://openjfx.io/)
2. Extract the downloaded file to a location on your computer.
3. Add the JavaFX library to your project:

   ### For IntelliJ IDEA:
   - File > Project Structure > Libraries > Add > Java > Select the `lib` directory from the extracted JavaFX SDK

   ### For Eclipse:
   - Right-click on the project > Properties > Java Build Path > Libraries > Add External JARs > Select all JAR files from the `lib` directory of the extracted JavaFX SDK

   ### For Visual Studio Code:
   1. Install the "Extension Pack for Java" if you haven't already.
   2. Open your project folder in VS Code.
   3. Create a new folder in your project root called `lib`.
   4. Copy all the JAR files from the `lib` directory of your JavaFX SDK into this new `lib` folder.
   5. In VS Code, press Ctrl+Shift+P (or Cmd+Shift+P on macOS) to open the command palette.
   6. Type "Java: Configure Classpath" and select it.
   7. In the "Referenced Libraries" section, click on the "+" button.
   8. Navigate to your project's `lib` folder and select all the JavaFX JAR files you copied earlier.
   9. Click "Select JAR files" to add them to your classpath.

   Next, you'll need to modify your `launch.json` file to include the necessary VM arguments:
   1. Go to the Run view (Ctrl+Shift+D or Cmd+Shift+D on macOS).
   2. Click on "create a launch.json file" if you don't have one, or open the existing one.
   3. Add or modify the configuration for your Java application:

   ```json
   {
       "type": "java",
       "name": "Launch BeanMachine",
       "request": "launch",
       "mainClass": "machine.BeanMachine",
       "vmArgs": "--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml",
       "projectName": "YourProjectName"
   }
   ```

   Replace `/path/to/javafx-sdk` with the actual path to your JavaFX SDK, and "YourProjectName" with the name of your Java project.

   If you're using VS Code's Java Project explorer:
   1. Right-click on your project in the "JAVA PROJECTS" explorer.
   2. Select "Configure Classpath".
   3. In the "Referenced Libraries" section, click the "+" button.
   4. Navigate to and select all the JavaFX JAR files in your `lib` folder.

## Understanding VM Arguments (vmArgs)

VM arguments, specified by `vmArgs` in the `launch.json` file, are parameters passed to the Java Virtual Machine (JVM) when it starts up. For JavaFX applications, these arguments are crucial because they tell the JVM where to find the JavaFX modules and which ones to use.

In our `launch.json` configuration, we use two VM arguments:

1. `--module-path /path/to/javafx-sdk/lib`: This specifies the path where the JavaFX modules are located. Replace `/path/to/javafx-sdk` with the actual path to your JavaFX SDK installation.

2. `--add-modules javafx.controls,javafx.fxml`: This tells the JVM which JavaFX modules to load. In this case, we're loading the `controls` module (for UI components) and the `fxml` module (for working with FXML files, if used in your project).

These VM arguments are necessary because, starting from Java 11, JavaFX is no longer included in the standard JDK and needs to be specified explicitly when running JavaFX applications.

Example:
```json
"vmArgs": "--module-path C:/Java/javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml"
```

Make sure to adjust the path according to your JavaFX SDK location.

## Building and Running the Project

### Using an IDE

1. Set the main class to `machine.BeanMachine`.
2. Add the following VM options to your run configuration:
   ```
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
   ```
   Replace `/path/to/javafx-sdk` with the actual path to your JavaFX SDK.
3. Run the `BeanMachine` class.

### Using Command Line

1. Compile the Java files:
   ```
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml machine/*.java
   ```
2. Run the application:
   ```
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml machine.BeanMachine
   ```

Replace `/path/to/javafx-sdk` with the actual path to your JavaFX SDK.

## Using the Bean Machine Simulation

Once the application is running:

1. Click the "Start" button to begin the simulation.
2. Beans will start dropping through the machine, demonstrating the central limit theorem.
3. Use the "Stop" button to pause the simulation.
4. Use the "Clear" button to reset the machine.

## Troubleshooting

- If you encounter `ClassNotFoundException` or `NoClassDefFoundError` related to JavaFX classes, ensure that the JavaFX SDK is correctly added to your project and that the module path is correctly set in your run configuration.
- For any other issues, make sure you're using a compatible JDK version and that all required files are in the correct package structure.

## Contributing

Contributions to improve the simulation or documentation are welcome. Please feel free to submit a pull request or open an issue to discuss potential changes/additions.

## Contact

If you have any questions or feedback, please open an issue in this repository.
