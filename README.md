## Lagom-Snowflake

A distributed id generator for Lagom.

id=base58(murmur3_128(snowflake_64))


## Compilation

`sbt runAll`

`curl http://localhost:9000/api/IdGenerator`

## IMPORTANT: Security

If you use this project, please make sure to generate a new random seed value. See DistributedIdGenerator.java

## TODO

* add parameter to generate more than one id
* randomize snowflake node id
