# Concert Ticket Booking System

The Concert Ticket Booking System is a Java application enabling users to book concert tickets. The application uses a PostgreSQL database to manage data. Follow the instructions below to set up the application on your local machine.

## Prerequisites

- Java JDK 11 or later
- PostgreSQL installed and running on your localhost
- Git

## Configuration

Ensure that you have PostgreSQL installed on your local host with the following configuration :

## PostgreSQL Local Setup

Ensure that you have PostgreSQL installed on your local host with the following configuration:

|      Access Information      |           |
|-----------------------------|-----------|
| **Host**                     | localhost |
| **Port**                     | 5432      |
| **Database**                 | postgres  |
| **Username**                 | postgres  |
| **Password**                 | postgres  |

## Project Details

- The project runs on port 8081.
- Access the documentation at [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## Endpoints

### Get Concert by ID

Retrieve concert details by providing their ID.

| Method   | Endpoint              | Description                            |
|----------|-----------------------|----------------------------------------|
| GET      | /api/concerts/{id}    | Retrieve concert details by ID.        |

**Parameters:**
- `id`: ID of the concert

### Get All Concerts and Search

Retrieve a list of all concerts and optionally search by name and venue.

| Method   | Endpoint          | Description                            |
|----------|-------------------|----------------------------------------|
| GET      | /api/concerts     | Retrieve all concerts with optional search filters. |

**Query Parameters:**
- `name` (optional): Search concerts by name
- `venue` (optional): Search concerts by venue
- `page` (optional): Page number for pagination
- `size` (optional): Number of items per page

**Example Usage:**
To search for concerts with the name "Concert z" and venue "Venue x", while paginating the results with 10 items per page, use the following endpoint:

```bash
GET /api/concerts?name=Concert A&venue=Venue A&page=1&size=10
```

### Book Ticket

Retrieve concert details by providing their ID.

| Method   | Endpoint              | Description                            |
|----------|-----------------------|----------------------------------------|
| POST      | /api/book    | Book a ticket for a concert.        |

**Request Body Example:**
```json
{
    "concert_id": "5d700f63-7e12-4a50-a9d7-70adad1d423a",
    "type": "VIP"
}
```

## Sample Data for Testing

Your project automatically generates sample data when it is running, which you can use for testing purposes. Below is the sample data that gets generated:

### Concerts Table

| id                                   | name      | date       | venue    |
|--------------------------------------|-----------|------------|----------|
| 5d700f63-7e12-4a50-a9d7-70adad1d423a | Concert A | 2023-08-12 | Venue X  |
| 5d700f63-7e12-4a50-a9d7-70adad1d423b | Concert B | 2023-08-11 | Venue Y  |
| 5d700f63-7e12-4a50-a9d7-70adad1d423c | Concert C | 2023-08-13 | Venue Z  |
| 5d700f63-7e12-4a50-a9d7-70adad1d423d | Concert D | 2023-08-14 | Venue A  |
| 5d700f63-7e12-4a50-a9d7-70adad1d423e | Concert E | 2023-08-15 | Venue B  |

### Tickets Table

| id                                   | concert_id                            | type     | price | available_qty |
|--------------------------------------|---------------------------------------|----------|-------|---------------|
| 1d700f63-7e12-4a50-a9d7-70adad1d423a | 5d700f63-7e12-4a50-a9d7-70adad1d423a | VIP      | 100.0 | 50            |
| 1d700f63-7e12-4a50-a9d7-70adad1d423b | 5d700f63-7e12-4a50-a9d7-70adad1d423a | Regular  | 50.0  | 100           |
| 1d700f63-7e12-4a50-a9d7-70adad1d423c | 5d700f63-7e12-4a50-a9d7-70adad1d423b | VIP      | 120.0 | 30            |
| 1d700f63-7e12-4a50-a9d7-70adad1d423d | 5d700f63-7e12-4a50-a9d7-70adad1d423b | Regular  | 60.0  | 80            |
| 1d700f63-7e12-4a50-a9d7-70adad1d423e | 5d700f63-7e12-4a50-a9d7-70adad1d423c | VIP      | 90.0  | 20            |
| 1d700f63-7e12-4a50-a9d7-70adad1d423f | 5d700f63-7e12-4a50-a9d7-70adad1d423c | Regular  | 45.0  | 70            |
| 1d700f63-7e12-4a50-a9d7-70adad1d4240 | 5d700f63-7e12-4a50-a9d7-70adad1d423d | VIP      | 110.0 | 25            |
| 1d700f63-7e12-4a50-a9d7-70adad1d4241 | 5d700f63-7e12-4a50-a9d7-70adad1d423d | Regular  | 55.0  | 60            |
| 1d700f63-7e12-4a50-a9d7-70adad1d4242 | 5d700f63-7e12-4a50-a9d7-70adad1d423e | VIP      | 80.0  | 15            |
| 1d700f63-7e12-4a50-a9d7-70adad1d4243 | 5d700f63-7e12-4a50-a9d7-70adad1d423e | Regular  | 40.0  | 50            |

