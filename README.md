# health-checker
Health Checker App

Register services for health
---------------------------

### 1. HTTP Service 
```
curl -X POST \
  http://localhost:8080/api/v1/services \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 4dcd7ec2-653a-6143-a12e-2a330e476fa0' \
  -d '{ 
  "name": "demo-http-service",
  "servicetype": "HTTP",
  "uri" : "http://localhost:8081/env",
  "username" : "user",
  "password" : "2b9697f8-d94a-4cdc-8e48-eb989b66e684"
}
```
### 2. SQL Service
```
curl -X POST \
  http://localhost:8080/api/v1/services \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 8f3098f3-c184-b3af-cc20-74c7dc9488bf' \
  -d '{ 
  "name": "demo-db-service",
  "servicetype": "SQL",
  "uri" : "jdbc:postgresql://localhost/postgres?user=rajaniy&password=raj65241",
  "query" : "select 1"
}'
```
### 3. Once the services are registered, invoke health check by running.
```
curl -X GET \
  http://localhost:8080/api/v1/run \
  -H 'Cache-Control: no-cache' \
  -H 'Postman-Token: 8f88106c-9bd6-8de9-fff0-d45e99548ef5'
```
