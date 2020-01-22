# voc5-backend
Run by executing `./gradlew run`

# Enviroment
You need to have a postgres db running with the needed tables
E.g. `docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres`

And insert the needed tables https://github.com/axel1200/voc5-resources/blob/master/schema.sql
