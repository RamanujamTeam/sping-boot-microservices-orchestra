# Project description
Full project requirements are available [here](https://3monthjunior.slack.com/files/lozenko/F1GMKRFGB/Deep_Server-Side_Translator). To sum it up:

**Provided:** 2 input files: first one contains information about people (id, name, email...) and second contains bitcons corresponding for those people(id, bitcoin).

**Expected result:** The data from both files should be combined (person info + his bitcoin) and persisted in MongoDB.

**Implementation requirements:** 
At first data from redis-data.xml should be persisted to Redis and the data from elastic-data.json to ElasticSearch. Then the data should be retrieved from the both datastores, aggregated and persisted in MongoDB.
Redis and ElasticSearch should run in Docker containers that should be started and stopped with Java Docker client.

# Project structure
Translator Service project consists of five microservices:

1. Docker Service
1. Redis Filler
1. ElasticSearch Filler 
1. Redis Aggregator
1. ElasticSearch Aggregator

### Docker Service
Docker Service is responsible for starting Docker containers (Redis, ElasticSearch, RabbitMQ) and stopping them when the job is finished. It is listening for RabbitMQ events from Redis and ElasticSearch Aggregators to define when it is time to stop containers.
### Redis Filler
Redis Filler periodically reads batches of data from input file *( redis-data.xml )* and persists it into Redis (running in Docker container). After proccessing all  input data, Redis Filler sets "Finished" flag in Redis signaling to Redis Aggregator that its job is done.
### ElasticSearch Filler 
Similar to Redis Filler, ElasticSearch Filler is reading input data from file *( elastic-data.json )* and persists it in ElasticSearch. When all input data is proccessed it sets "Finished" flag in ElasticSearch for ElasticSearch Aggregator to check.
### Redis Aggregator
Redis Aggregator periodically reads persisted data from Redis and writes it to MongoDB. When finished, it sends RabbitMQ message to Docker Service to stop Redis container - it is not needed anymore. 

### ElasticSearch Aggregator
Similar to Redis Aggregator, it reads data from ElasticSearch and writes to MongoDB. When finished, it sends message to Docker Service to stop ElasticSearch container.

# Technologies used
* Spring Boot
* Docker
* Redis
* ElasticSearch
* RabbitMQ

# Next steps
* Test coverage
* Integrating ZooKeeper to avoid duplicating properties files
