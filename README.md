[![Build Status]( https://travis-ci.com/jenny-wang8/shipping-service.svg?branch=master)](https:// travis-ci.com/jenny-wang8/shipping-service)

# Shipping Service

Do you have a stack of packages you've been meaning to send?
Do you want to know the rates for multiple carriers?  
Do you need want to know how long it will take to get there?

Well this is the service for you!

Enter in an origin and destination addresses as well as some details about the package and you'll get the shipping rates.

# Ingredients

Data
- [Shippo API](https://goshippo.com/docs)
Back end
- [Java](https://www.java.com)
- [Maven](https://maven.apache.org/what-is-maven.html)
- [Spark Java](http://sparkjava.com/)
- [JUnit](https://junit.org/junit4/)
- [Mockito](http://site.mockito.org/)
- [Powermock](https://github.com/powermock/powermock)
Front end
- [AngularJS](https://angularjs.org/)

# How to Run the Service & Client

Prerequisites
Maven installed

Packaging & Deployment
Build the jar:
```
mvn clean package
```
Execute the runnable JAR
```
java -jar shipping-service-0.0.1.jar
```

Open up your favorite browser and enter in http://localhost:8888/

# How to Run Tests
Integration tests
```
mvn clean integration-test
```
Unit tests
```
mvn clean test
```

# Reasoning & Comments
I will be the first to tell you I'm not a front end developer which is why I call it "minimalistic".  I do however enjoy backend development and design.  At first glance the server code is a little more wordy for a simple project but I wanted to show how this service could be built out.  

I worked in e-commerce for a brief stint in my life and one problem that rattled around my head for a while was, can we write a shipping service that removes any knowledge of the rate providers (not the actual carriers but third party apps) from our main service that allows us to provide shipping charges even if the provider is down?  Of course we can!  The problem is that I never got the chance to actually write the service so this what I had in mind for a phased approach.  (FYI, I think of phases as less than a sprint's worth of work but more than a single task.)

Phase 1 (this code)
Write a simple RESTful service that calls out to a shipping rate provider.

Phase 2  - Add a database to save the the rates (use these saved rates in the event of an outage) & add in circuit breaking (like a Hystrix).
Use your favorite database, figure out a schema & include an ORM (or not, your choice).
For the circuit breaking, in the case we can't reach the rate provider, we'd need a class to retrieve the rates.  This is where the ShippingClient interface comes into play.  Create a new class that implements the ShippingClient and it gets its data from the DB.  TADA!  A shipping client doesn't have to be a third party client.  

Phase 3 - Add the purchase of a shipping label
So when someone is ready to actually ship something, you need to buy the shipping label for your package.  We need to make another call back to the third party provider to do so.  This is why the comm classes are separated and so to do this, create a new class that extends the AbstractShippoCommunication class.  Add in the deserialization and the POST/GET.  (See ShippoShipmentCreateComm as an example).

There's a bunch more phases one can do but those are the first couple.  
