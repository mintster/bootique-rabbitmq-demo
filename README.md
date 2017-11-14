NixMash Bootique RabbitMQ Demo
============

Demo using the Bootique RabbitMQ Module. Also demonstrates POJO -> JSON Message conversion with Jackson.

## Setup

RabbitMQ Server must be running. It is assumed RabbitMQ *guest/guest* access is enabled. The Demo App creates **bqMessages** and **bqReservations** Exchanges and Queues automatically.

## Running the App

The demo app includes both SENDER and RECEIVER modules, both of which share a COMMON Module. 

To Build with Maven:

```bash
$ mvn clean install
```
To send messages

```bash
$ java -jar send/target/mqsend.jar
```
To receive messages, in a 2nd Terminal Window use the following:

```bash
$ java -jar send/target/mqreceive.jar
```

The demo handles both text messages and `Reservation` objects. To send a `Reservation` wrap the string in {curly brackets} in the Sender application when prompted.

## NixMash Posts

- [A Bootique RabbitMQ Demo Application](http://nixmash.com/post/a-bootique-rabbitmq-demo)
- [Passing POJOs in RabbitMQ](http://nixmash.com/post/passing-pojos-in-rabbitmq)


***Last Updated** 11-14-2017*
