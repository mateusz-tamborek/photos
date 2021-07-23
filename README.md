# Photo gallery
A simple gallery for the job interview [(recruitment task)](https://github.com/mateusz-tamborek/photos/wiki/Zadanie-rekrutacyjne)

### Prerequisites
docker and docker-compose

## How to run?
 `docker-compose up`
 or
 `./gradlew bootRun` (only if you have a locally running database)
 
## How to use?
1. Go to: http://localhost/swagger-ui.html
2. Create your own user at the endpoint /users or use the default user with credentials: user@domain.com / Password123!
3. Get token under /auth/token
4. Click Authorize button and type: `Bearer {your token}`
5. Enjoy :slightly_smiling_face:
 
## Used technologies
 - Java 8
 - Spring Boot 2
 - PostgreSQL
 - Lombok
 - Swagger
 
 ### TODO:
  - global exception handler
  - tests
  - clean architecture based on java modules
  - refresh token
  - Angular frontend
