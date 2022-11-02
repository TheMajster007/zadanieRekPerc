# Software required

### MVN
1. Download [Maven](https://maven.apache.org/install.html)
2. Extract Maven archive to C:\Program Files\Maven\
3. Open the Start menu and search for environment variables.
4. Under the Advanced tab in the System Properties window, click Environment Variables.
5. Click the New button under the System variables section to add a new system environment variable
6. Enter MAVEN_HOME as the variable name and the path to the Maven directory as the variable value. Click OK to save the new system variable.
7. Select the Path variable under the System variables section in the Environment Variables window. Click the Edit button to edit the variable.
8. Click the New button in the Edit environment variable window.
9. Enter %MAVEN_HOME%\bin in the new field. Click OK to save changes to the Path variable.
10. Click OK in the Environment Variables window to save the changes to the system variables
11. Verify Maven Installation
```cmd
mvn -version
```

### Docker

[Docker](https://docs.docker.com) is a platform designed to help developers build, share, and run modern applications. We handle the tedious setup, so you can focus on the code.

1. Install [Docker](https://docs.docker.com/get-docker/)

