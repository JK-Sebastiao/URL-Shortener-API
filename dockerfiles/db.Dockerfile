FROM mysql:8

ENV MYSQL_DATABASE=urldb \
    MYSQL_ROOT_PASSWORD=p@ssw0rd

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306