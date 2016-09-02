## Lagom-Snowflake

A distributed id generator for Lagom.

id=base58(murmur3_128(snowflake_64))


## Compilation

`mvn compile`

## Running (dev)

* `mvn lagom:runAll`

Test locally

`curl http://localhost:9000/api/idgen`
`curl http://localhost:9000/api/idgen/10`

## Package & Deploy to ConductR

1. `mvn clean package`
2. `sandbox run 1.1.8 --feature visualization --nr-of-containers 3`
3. `conduct load snowflake-impl/target/idgen-v1-c9ea5ac67ebd2abd8976ebe74c69d3d3a5290d46b7880d5056a37007279d304a.zip`
4. `conduct run idgen`

To check status

* `conduct info`
* `conduct logs idgen`

To clean up

* `conduct unload idgen`

## IMPORTANT: Security

If you use this project, please make sure to generate a new random seed value. See DistributedIdGenerator.java

## TODO

* Randomize snowflake node id
