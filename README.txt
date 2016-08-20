======================
RNSolutions Sample Application (Yalp)
======================

Hi! We're glad you're interested enough in RNSolutions to take a look at a small coding exercise. We'll use this coding exercise to help us 
determine your fit, talent, and skill level for placement in the RNSolutions workforce. This coding example should give you a chance to 
challenge yourself - hopefully for not much more than an hour and possibly much less.  

Lets get started!

====
What you need:
====
* Sun's JDK 1.6 (others should be fine; but, not verified)
* Maven 2.0.10
* A Text Editor
* 1 hour
* Passion!

===
What is this?
===
This project is just a small tech demo. RNSolutions isn't really attempting to create a fun/hip/trendy restaurant and activity location 
indexing service like Yelp; but, for the sake of fun.. lets say we are implementing this service, and it's called Yalp. This little 
bundle of joy could be used as a part of a map-reduce based search engine to help handle the MASSIVE amount of data that such a service 
would be indexing. 

====
What you need to accomplish
====
* Fix the broken unit tests. The original filtering does not support case insensitive searching. Find a solution and alter the filtering, 
don't modify the test.  
* Clear all the FIXMEs from the code (logging, immutable return type for returned collections)
* Add sorting to the output of the results (For now lets do Location name, then city; alphabetically ascending. Don't remove collisions)
* Add a parameter to the application to pass into the filtering functionality
* Unit tests for your sorting code
* Add descriptions to the YalpEntry object and the create some fake data in the csv file; allow the description data to be filtered as 
well. 


====
How to turn in your results
====
When you are done, we want to see all of your code; not just the binary to confirm it works. tar/zip your code and send it back to us. 
