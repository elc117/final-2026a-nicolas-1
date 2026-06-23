FROM mcr.microsoft.com/devcontainers/java:21-bookworm

# Install PostgreSQL server and client
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends postgresql postgresql-contrib \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
