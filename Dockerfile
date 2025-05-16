FROM postgres:15-alpine

# Set the default database, user, and password
ENV POSTGRES_DB=pricesurveydb
ENV POSTGRES_USER=pricesurvey
ENV POSTGRES_PASSWORD=password

# Copy any custom initialization scripts if needed
# COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432