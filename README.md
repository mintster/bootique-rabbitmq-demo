NixMash Bootique RabbitMQ Demo
============

Demo using the Bootique RabbitMQ Module. Also demonstrates POJO -> JSON Message conversion with Jackson.

## Setup

RabbitMQ Server must be running. It is assumed RabbitMQ *guest/guest* access is enabled. Depending on your RabbitMQ configuration you may have to create the following Exchanges and Queues, which may not be created automatically.

Create Queues listed in Bootique Module `bootique.yaml` files: 

**Exchanges/Queues:** *bqMessages, bqReservations, rpcMessages, rpcReservations* 

***Note:** set _durable_ property to TRUE. Also, any Exchanges created are type TOPIC.*

## Two Demo Options

There are two demo options: 

1. One-Way Send of String Message or Reservation Object. Server acknowledges receipt. Nothing returned to client.
2. RabbitMQ RPC, A) sending a Reservation Object and returning a Message, B) sending a String Message and retrieving a Customer Object

To switch between demo options, comment the appropriate property found in the `Common` Module `/resources/common.properties` file.

```bash
startup.type=rpc
#startup.type=messages
``` 
To send a `Reservation` Object wrap the string in {curly brackets} in the Sender application when prompted.

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

## NixMash Posts

- [A Bootique RabbitMQ Demo Application](http://nixmash.com/post/a-bootique-rabbitmq-demo)
- [Passing POJOs in RabbitMQ](http://nixmash.com/post/passing-pojos-in-rabbitmq)
- [Rabbit RPC Demo with Bootique](https://nixmash.com/post/rabbitmq-rpc-java-demo-with-bootique)
- [Returning an Java Object List in RabbitMQ RPC](https://nixmash.com/post/returning-an-java-object-list-from-rabbitmq-rpc)


***Last Updated** 02-26-2018*
