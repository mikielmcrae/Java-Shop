# Java-Shop
Coffee shop ordering station UI in JavaFX and CSS.

Welcome to Java Shop, a coffee shop ordering station UI _written in_ Java!

Java Shop implements object-oriented programming, tasks, file reading and custom styling to give you, the customer, the coffee-ordering experience you only thought possible in your wildest dreams!

~ Table of Contents ~

I. Installation instructions

II. Feature preview

____________________________________________________________________________________________________________________________________________________________

I. INSTALLATION INSTRUCTIONS

If you don't have a JavaFX environment set up and would like to run TalkBox from your own machine, follow these steps.

(If you have a JavaFX environment set up already, you can skip to step _vi_).

i. If you do not have the Java JDK installed, you can install it for your device [here](https://www.oracle.com/java/technologies/downloads/).

ii. If you do not have Eclipse IDE installed, it can be found [here](https://www.eclipse.org/downloads/).

iii. Follow the instructions [here](https://openjfx.io/openjfx-docs/#IDE-Eclipse) under the **Non-Modular IDE** section.

iv. [Download the latest release of JavaFX](https://gluonhq.com/products/javafx/). **Ensure your download is of type SDK!**
this will download as a zip. Open the zip folder and take note of where you keep this folder.

v. Now, download the JavaFX Plugin for Eclipse by opening Eclipse Marketplace in Eclipse. Simply type 'FX' into the search bar, and download "e(fx)clipse
3.7.0" (or the latest version). After reading and agreeing to the terms, Eclipse may restart.

vi. In Eclipse, open a new **Project** (this is not a Java Project, but just a "Project"!)

<img width="199" alt="An image of a dropdown list of project types, with 'Java Project' highlighted" src="https://user-images.githubusercontent.com/86265908/174652319-c320637f-637c-4aa5-85e9-4d579ce00571.png">

From here, open a new JavaFX Project.

<img width="323" alt="A list of project types in Eclipse, with 'JavaFX Project' highlighted" src="https://user-images.githubusercontent.com/86265908/174652354-97250a0e-c614-4dff-9540-47e4cf04d519.png">

Upon clicking the "Next" Button at the bottom of the window, you will be able to name your project -- I recommend naming your project after whatever project you're downloading (In this case, TalkBox). This can be done later if desired.

Once this is done, click the **Next** button instead of the **Finish** button.

<img width="799" alt="A window in Eclipse allowing users to set a name for their project and continue" src="https://user-images.githubusercontent.com/86265908/174652620-c48252cf-e358-41fd-8f49-7ea0d3e55bd9.png">

vii. Once you have clicked the **Next** button, you will be brought to a window wherein you can further customise the setup for your Project:

<img width="796" alt="A window with further customisation options for the user's new Project, including the 'Libraries' Pane" src="https://user-images.githubusercontent.com/86265908/174652941-2878aeac-0596-4a81-82a5-a9735211acfd.png">

From here, click **📚Libraries**: 

<img width="775" alt="The 'Libraries' section of the Project setup process, where users can add libraries to the module path or class path" src="https://user-images.githubusercontent.com/86265908/174653123-072c1c66-9787-455a-89e5-2c05c342a3f2.png">

With **Classpath** selected, click "Add Library" from the list of buttons on the right. You will need to add two libraries: The JavaFX SDK and the JavaFX User Library.

To add the **JavaFX SDK**: Simply select the JavaFX SDK from the list of Libraries and click "Finish".

<img width="418" alt="The 'Add Libraries' window shown with the JavaFX SDK selected" src="https://user-images.githubusercontent.com/86265908/174653875-b6a0f04d-f0a1-4fe7-9f9d-5bfb36596b4a.png">

To add the **JavaFX User Library**: Reopen the "Add Library" window, select User Library and click **Next**. Check the JavaFx User Library box and click "Finish".

<img width="315" alt="The User Library section of the 'Add Libraries' window shown with the JavaFX User Library checkbox selected" src="https://user-images.githubusercontent.com/86265908/174653893-cf6aec9c-a01e-4df6-a519-30bb94ad6c47.png">

Remember to add **both** of these libraries to your project.

viii. Your project is set up!

If you see the following Red Xs, delete the module-info.java section by right clicking it and deleting.

<img width="257" alt="The user's project files shown with red error indicators from the module-info file" src="https://user-images.githubusercontent.com/86265908/174654089-be3715a7-26fc-471e-803a-b6836330dc76.png">


ix. Download the Java Shop code as a zip from the [TalkBox repository](https://github.com/mikielmcrae/Java-Shop) as a ZIP.
Drag "JavaShop.java" from this folder into the applicaton folder in your Eclipse project. You may delete Main.java if you wish.

x. Now, navigate to JavaShop.java and run it. The console should display the following error:

"Error: JavaFX runtime components are missing, and are required to run this application"

To fix this, right click on JavaShop.java in your project. Navigate down to "Run as...", and select "Run configurations". 

<img width="488" alt="a dropdown menu including an option for user to set run configurations for their project" src="https://user-images.githubusercontent.com/86265908/174654550-db628cb9-713e-485b-be92-b40f41ed179a.png">

**Note** your Main file will not show up in this pane until you've attempted to run it at least once.

Once here, navigate to the "Arguments" tab. In the VM Arguments textarea, enter the following arguments:

**For Linux/Mac**: --module-path /path/to/javafx-sdk-17/lib --addmodules javafx.controls,javafx.fxml

**For Windows**:  --module-path "\path\to\javafx-sdk-17\lib" --addmodules javafx.controls,javafx.fxml 

where the "path to" the JavaFX SDK is the location in which you saved the opened ZIP in step IV.

**Uncheck** the checkbox under this textarea, "Use the -XstartOnFirstThread
argument when launching with SWT". 

Apply the changes. If successful, running JavaShop.java will now result in a window opening. There should be all the main components of Java Shop present, but they won't be styled. 

Now, delete "application.css" from the main folder.

xi. From the Java Shop folder you downloaded from this repository, select "application.css", "javashop-logo.png" and "coffeemenu.csv". Drag and drop or simply paste into the main folder under your JavaShop project in Eclipse. (This may be the src folder or simply the project folder itself. Whatever it is, just ensure these files go into the same folder that **application** is in -- don't put them in **application**!)

<img width="196" alt="An image of the css file, the logo, and the menu for Java Shop selected within a folder" src="https://user-images.githubusercontent.com/86265908/174855299-df9a00e0-90ec-4128-bf21-64878b0c3b03.png">

(If they do not drag directly in, you can drag them into the folder first and them move them into src).

Your project should look something like this if everything is in the right location:

<img width="289" alt="A screenshot of the project setup in JavaFX, showing JavaShop.java in the application folder, while the assets for the app are in the main project folder" src="https://user-images.githubusercontent.com/86265908/174855523-28305d46-da6f-43af-9424-bd7c4b6a9825.png">

Note that your assets might be in the src folder which is fine -- as long as they're in the same folder where application is located.

_You might not have Main.java anymore if you have deleted it. It is inconsequential whether you choose to keep or remove it._


If the rest of TalkBox's assets have been moved successfully, re-running the file should result in the final JavaShop UI:


<img width="593" alt="An image of JavaShop's final UI" src="https://user-images.githubusercontent.com/86265908/174855979-847ddd3a-ae2b-45e7-ba4a-3a8dc65f661d.png">


From here, you can peruse all functionality of the UI! You can even edit the .csv file you like to include more drinks on the menu. Just be sure to include their prices in the same format as the current csv!

Thank you!!!
-Mikiel


