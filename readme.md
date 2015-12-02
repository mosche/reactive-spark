# Reactive spark streaming example


## Step by step instructions

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

