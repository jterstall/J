Peer Review

Names  
Level: 4  
Comments: Consistent use of casing,  Always clear what the function name implies what the function does.  

Headers  
Level: 2  
Comments: Header comments are mostly present but are really short and do not show how to use the code. Most just describe what the class is used for, not how to use it.  

Comments  
Level: 3  
Comments: Sometimes obsolete in places where no comments are really needed, mostly in cases where the function itself also explains the same thing. For example, "Display that information -> displayMovieInformation(movieInformation)". Most of the time however, the comments explain the code in a good way.  

Layout  
Level: 4  
Comments: Positioning/indentation is good across all files and consistent with each other.   

Formatting  
Level: 3   
Comments: Indendation, line breaks, spacing and brackets are consistent within the files and clearly state the hierarchy of the functions.  

Flow  
Level: 3  
Comments: setBottombar is a duplicate in many files. Other than that flow is fine, easily identifiable how the flow of the app is from the code.  

Idiom  
Level: 3  
Comments: Choice of control structure was fine, some code was used across multiple files but not reused from one class/library (setBottomBar).  

Expressions  
Level: 2-3  
Comments: Some unnamed constant strings in dialog windows, good use of constants in RetrieveMovieInformationTask. Data types are appropriate and expressions mostly simple.  
 
Decomposition  
Level: 2  
Comments: Most functions clearly handle separate tasks but could be separated more based on subtask. For example, addToWatchlist(View v) in ShowMovieInformation.java creates a json object, adds to sharedpreference, changes the icon etc. all in the same function.  

Modularization  
Level: 4  
Comments: Each class clearly handles a different task, communication betweem them is limited  
