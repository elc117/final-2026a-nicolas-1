#!/usr/bin/env bash
set -e

echo "Starting PostgreSQL service..."
sudo service postgresql start

# Wait for PostgreSQL to be ready
until pg_isready; do
  echo "Waiting for PostgreSQL to be ready..."
  sleep 1
done

echo "Configuring database user and tables..."

# Create database user 'res_user' if it doesn't exist
sudo -u postgres psql -tc "SELECT 1 FROM pg_roles WHERE rolname='res_user'" | grep -q 1 || \
sudo -u postgres psql -c "CREATE ROLE res_user WITH LOGIN PASSWORD 'restaurante' SUPERUSER;"

# Create production database 'restaurant_db' if it doesn't exist
sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw restaurant_db || \
sudo -u postgres createdb -O res_user restaurant_db

# Create test database 'restaurant_test_db' if it doesn't exist
sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -qw restaurant_test_db || \
sudo -u postgres createdb -O res_user restaurant_test_db

echo "PostgreSQL initialization complete!"
