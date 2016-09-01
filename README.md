## Lagom-Snowflake

A distributed id generator for Lagom.

id=base58(murmur3_128(snowflake_64))


## Compilation

`mvn lagom:runAll`

`curl http://localhost:9000/api/idgen`
`curl http://localhost:9000/api/idgen/10`

## IMPORTANT: Security

If you use this project, please make sure to generate a new random seed value. See DistributedIdGenerator.java

## TODO

* Randomize snowflake node id
