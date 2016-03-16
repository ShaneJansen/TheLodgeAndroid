# The Lodge Android

### Overview

This is an Android application for
[The Lodge Server](https://github.com/ShaneJansen/TheLodgeServer)
that can be used to turn on and off any
device via this webservice.  This repo also supports Android Wear.

For full project details visit: http://shanejansen.com/android-wear-and-raspberry-pi-controlling-mains-power

### Setup

Create a class in the `data` package named `Secret.java` and add a public member
of type String called `BASE_URL` which contains the location of your web server.
