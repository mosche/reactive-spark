# Reactive spark streaming example

This is a fully dockerized Spark streaming example using a Kafka queue to explore the new back-pressure feature introduced in Spark 1.5.

* Install docker-machine & docker-compose (require >= 1.5.1)

* Create / start the virtual machine <br>
    ```docker-machine create rspark --driver virtualbox``` or <br>
    ```docker-machine start rspark```

* Update your environment
    ```
    eval "$(docker-machine env rspark)"
    export DOCKER_HOST_IP=`docker-machine ip rspark`
    ```

* Run zookeeper, kafka, kafkamanager, spark master and worker<br>
    ```
    docker-compose up zookeeper kafka kafkamanager sparkmaster sparkworker
    ```

* [Configure kafkamanager](http://DOCKER_HOST_IP:9001/) and add the required topic
    * Zookeeper: `zk:2181`
    * Kafka version: `0.8.2.1`
    * Topic: `numbers`

* Build and submit the spark streaming consumer<br>
  Unfortunately Spark expects jobs to be submitted to `spark://master:7077` (the docker internal hostname), so just map it in your `/etc/hosts`.
    ```
    sbt consumer/assembly consumer/sparkSubmit
    ```

* Build and start the containerized producer
    ```
    sbt producer/docker:publishLocal
    docker-compose up producer
    ```

* Tweak the producer settings in `docker-compose.yml` and observe how your *processing time* / *processing delay* changes.<br>
  Your Spark streaming UI should be [here](http://master:4040/streaming/), but can always be found on the [Spark master UI](http://master:8080/).
    * `PRODUCER_RATE`: the event rate
    * `PRODUCER_MIN_DELAY`: the min. processing weight of the produced messages
    * `PRODUCER_MAX_DELAY`: the max. processing weight of the produced messages

    ```
    docker-compose up producer
    ```

* Tweak the Spark streaming settings in `SparkConsumer.scala` and resubmit the job.
    ```
    sbt consumer/assembly consumer/sparkSubmit
    ```

