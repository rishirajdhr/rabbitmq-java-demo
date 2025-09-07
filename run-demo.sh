#!/bin/bash
./mvnw clean package
docker compose up --build -d
docker compose logs -f producer-01 producer-02 producer-03 consumer-01 consumer-02 consumer-03