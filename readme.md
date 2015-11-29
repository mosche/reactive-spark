# Reactive spark streaming example


## Start a command line consumer

´´´
docker exec -i -t reactivespark_kafka_1 bash
cd $KAFKA_HOME/bin
./kafka-console-consumer.sh --zookeeper zk:2181 --topic numbers --from-beginning 
´´´
