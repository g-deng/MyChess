# MyChess

## Description
MyChess is a simple fully-functional chess game written in Java using the Swing GUI toolkit. This project supports basic gameplay features, move validation, and a user-friendly GUI.
It was originally made for a homework assignment that required the usage of Swing.

![MyChess setup](https://github.com/user-attachments/assets/83dc172b-de8b-477b-b7ca-482ed8bf8d13)

## Features
- Two-player mode
- Show legal moves
- Move validation based on chess rules
- Features on the way:
    - Auto-notation
    - .pgn notation output
    - Custom position set-up
    - Undo

![MyChess en passant](https://github.com/user-attachments/assets/851fff92-a19d-4ca6-a638-96bf8e926ae9)

## Usage
You will need to have JDK 17 or up installed. (See below)

For Windows:
1. Download `MyChess.exe`.
2. Run the executable directly.

For Java:
1. Download `MyChess.jar`.
2. Run the following command:
```bash
   java -jar MyChess.jar
```

## Installing Java JDK 17 or Higher for MyChess

To run MyChess, you need to install Java JDK 17 or a higher version. 
You can check your Java version in terminal or command prompt:
```bash
java -version
```
Follow the instructions below based on your operating system:

### For Windows:
1. Download the JDK from the [Oracle JDK download page](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
2. Run the installer and follow the prompts.
3. Set the `JAVA_HOME` environment variable to the installation path.
4. Update your `Path` variable to include `%JAVA_HOME%\bin`.
5. Verify the installation by running the following command in the Command Prompt:
   ```bash
   java -version
   ```

### For macOS:
1. Download the JDK from the [Oracle JDK download page](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
2. Open the downloaded `.dmg` file and follow the installation instructions.
3. Set the `JAVA_HOME` variable in your terminal configuration file (e.g., `.bash_profile`, `.bashrc`, or `.zshrc`):
   ```bash
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk17.x.x.jdk/Contents/Home
   export PATH=$JAVA_HOME/bin:$PATH
   ```
4. Verify the installation by running the following command in the terminal:
   ```bash
   java -version
   ```

### For Linux:
1. Use your package manager to install OpenJDK 17. For example, for Ubuntu, run:
   ```bash
   sudo apt install openjdk-17-jdk
   ```
2. Verify the installation by running the following command in the terminal:
   ```bash
   java -version
   ```

### Additional Notes
- **Link to Official Documentation**: For more detailed instructions, refer to the [official Java documentation](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
- **Troubleshooting**: If you encounter any issues, ensure the correct paths are set and restart your terminal or command prompt after installation.
