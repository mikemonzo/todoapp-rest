#  Docker file fot the database postgres (without vulnerabilities) with a table todoapp without init script
FROM postgres:alpine
# Set environment variables
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=12345678
ENV POSTGRES_DB=todoapp

# Update and clean up
RUN apk update && apk upgrade --no-cache

# Expose the PostgreSQL port
EXPOSE 5432

# Use non-root user
USER postgres