# voc5-backend
Run by executing `./gradlew run`

# Enviroment
You need to have a postgres db running with the needed tables
E.g. `docker run --rm  --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres`

And insert the needed tables from the schema.sql in the sql folder.

# SQL connection parameters
Use the following environment variables to configure the connection to the database.
POSTGRES_IP
POSTGRES_PASSWORD
POSTGRES_PORT
POSTGRES_DB
POSTGRES_SCHEMA