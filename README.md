# The Cuddly Throwable Application Server, and Bluemix IoT

![Photo of Throwable Application Server](spherephoto.png)

# About this sample

This sample shows two different ways of making sensor readings from a device available in a web page: 

* Run an application server on the device itself
* Run an application server in the cloud, and publish sensor readings to it by messages

The second is the more traditional model, but both are interesting for different reasons. The same [web application](sphere-war) can be run in the cloud and on a low-cost single board computer. The application
 uses a range of Java EE programming models: 

* Websockets
* JPA
* EJB startup singleton and timer beans 
* Servlets
* JSPs

In the case where the application server is running in the cloud, a [small Java application](pcduino-sensors) regularly publishes messages to a configured MQTT broker.

This application is designed to run on a [pcDuino](http://www.linksprite.com/linksprite-pcduino/) (and optionally [Bluemix](http://bluemix.net)), and runs on WebSphere Liberty](http://wasdev.net). 

**[License information](LICENSE.txt)** 

# Limitations 

This project has not been optimised for efficiency. It is designed to demonstrate usage of a range of Java EE programming models, and show how a modular runtime can make the Java EE programming models a great option, even on resource-constrained environments like a pcDuino or Raspberry Pi. However, that doesn't mean that using ~every~ Java EE programming model in the same resource-constrained application is a great idea. 

## Getting started 

### Eclipse integration 

To set up Eclipse projects, run 

    gradle clean
    gradle eclipse

### Running the server locally (from the command line) 

Navigate to the `sphere-war` folder and run

    gradle runServer

The application should be available on http://localhost:9080. By default it will run in emulation mode, so it will simulate pcDuino pin readings. You can control this by unsetting the `EMULATE_PINS` system property in the jvm.options file](liberty-usr/servers/pollServer/jvm.options).

### Deploying to a single board computer 

To create a zip with the application and all dependencies (including the server), run 

    gradle packageServer


### Deploying to Bluemix 

To create a war, run `gradle` from the sphere-war folder.

This can then be pushed to Bluemix with 

    cf push -p build/libs/sphere-war.war

### Building and testing the MQTT agent 

If the web application runs in the cloud, you will need to deploy an MQTT agent to the device. To build the MQTT agent, run `gradle` in the pcduino-sensors folder. 


#### Testing the client without pcDuino hardware

To run the MQTT client on a non-pcDuino machine (with emulated pin readings), run `gradle emulate`. This will use the quickstart MQTT broker, so you'll be able to see how the client works, but your Bluemix application will not have access to readings, or be able to send messages.

To use a configured IoT Platform service, navigate from your Bluemix dashboard to the IoT Platform dashboard (by clicking on the service) and either:

* create a device with type pcduino and id 'emulated', make a note of the org and access token, and run 

    gradle emulate -DiotConfig="<org> <accesstoken>"
    
* create a config file from the device configuration provided, using the format in [example.conf](pcduino-sensors/example.conf),  and run 

    gradle emulate -DiotConfigFile="<pathtoconfigfile>"

You will then be able to see simulated values for this device in the IoT dashboard for your new device. 

See the [Internet of Things Platform device samples](https://github.com/ibm-messaging/iot-device-samples) for more information on using MQTT with Bluemix.

# Dependencies 

This sample uses [WebSphere Liberty](http://wasdev.net), the [Internet of Things Platform Java client helper libraries](https://github.com/ibm-watson-iot/iot-java), [Eclipse Paho MQTT client](http://www.eclipse.org/paho/), Java EE interfaces, and the [webjars] bundles of the [Bootstrap UI framework](http://getbootstrap.com) and [C3 graphing library](http://c3js.org). 

# More information 

Slideshare: <iframe src="//www.slideshare.net/slideshow/embed_code/key/k6zLBYj0DgXu4k" width="595" height="485" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> <strong> <a href="//www.slideshare.net/HollyCummins/an-arduino-an-application-server-and-me" title="An Arduino, an application, server, and me" target="_blank">An Arduino, an application, server, and me</a> </strong> by <strong><a target="_blank" href="//www.slideshare.net/HollyCummins">Holly Cummins</a></strong> </div>


![Drawing of Throwable Application Server](drawnsphere.png)
