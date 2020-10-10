chay lenh
    docker-compose up: de chay mysql port 3307
    import data vao sql:
        docker exec -i user-service-mysql-container mysql -uroot -proot < data.sql


xem swagger: http://localhost:9600/swagger-ui.html
