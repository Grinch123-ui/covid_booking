## FIT3077, MA10 - Working with a Web Service 

#### What is this?
This purpose of this program is to perform a variety of different operations such
as testing site searching and booking creation on a web service.

#### Running the Code
To run the code, all you need to do is to run the WebApplicationDriver class. Once you do,
you are able to perform the functionalities offered by the program.

#### Disclaimers
There are some very important things we would like to note on the program:
1. Before running the program, you need to provide an API key for the web system. You would place this 
under the myApi variable in the HTMLRequestSetup class. 

2. There is minimal input checking for the UI part of our program due to time constraints and our desire
to focus on the core functionalities of the program instead. Therefore, it is essential for correct inputs to be 
added for the functionalities to work properly. 

3. For the booking statuses, we intended for it to hold 3 values: "INITIATED", "COMPLETED", and "CANCELLED". 
The meaning of these are fairly self-explanatory.

##### IMPORTANT NOTE for Assignment 3:
The team decided to give up on implementing the **live notification** functionality because of the limitations of the
front end (a CLI) used in the system. We couldn't think of a way for users to get notified live whenever a change occurs.
We just decided to submit what we have due to upcoming deadlines making it difficult for us to continue working 
extensively on the code. 