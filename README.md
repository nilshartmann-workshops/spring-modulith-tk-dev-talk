# Spring Modulith Example Application

This repository contains the code of my talk "Modularisierung pragmatisch: Ein praktischer Deep Dive in Spring Modulith"

![Roses](./roses.png)

## Branches

- This repository contains four branches with different states of the application:
    - `main`: initial code base used for live coding
    - [`schritte`](https://github.com/nilshartmann-workshops/spring-modulith-tk-dev-talk/tree/prepare): this contains all code that I intendend to show. In the [commit list of that branch](https://github.com/nilshartmann-workshops/spring-modulith-tk-dev-talk/commits/schritte/) you find single commits each with one (sub)topic we discussed. **Probably the best way to "get started" with this application and the talk.**
    [`live_coding`](https://github.com/nilshartmann-workshops/spring-modulith-tk-dev-talk/tree/live_coding): that is the code that I wrote during the talk


# Examples

In this repository you'll find examples for:
- application modules
- named interfaces (`care.suggestion`)
- open application modules (`shared`)
- moments API (`InvoiceGenerator`)
- modularized flyway migration scripts (`resources/db/migration`)
- async event handling (`CareService`, `UsageTracker`)
- externalized events (published to Kafka, `InvoiceGenerator`, `InvoiceGeneratedEvent`)
- module tests

# Getting started

As this example has no frontend (only some HTTP endpoints), best is to run the test cases in the `test` folder on the `prepare` branch.

Otherwise, you can run the backend, by starting the `PlantifyApplication` class. Make sure, the required postgres DB and Kafka are running (see `compose.yaml`).

You can trigger the processing of new plants (including async event handling) by running an HTTP call for example with curl:

```bash
curl -X POST --location "http://127.0.0.1:8080/api/plants" \
    -H "Content-Type: application/json" \
    -d '{
          "location": "Balkon",
          "name": "Cannabis (natürlich nur für medizinische Zwecke)",
          "ownerId": "ee3829e4-fe2b-4d03-b2a1-70f1425d8c1c",
          "plantType": "SUMMER_FLOWERS"
        }'
```




# Observation

Spring Modulith records tracing to Micrometer. The example application is configured to use Zipkin as Frontend. You can start Zipkin using docker:
`docker run -d -p 9411:9411 openzipkin/zipkin`
Now, when running the application an registering a new plant, you will see traces in Zipkin.


# Contact

If you have questions, comments or feedback, do not hesitate to contact me. You can find my [contact data here](https://nilshartmann.net/contact).