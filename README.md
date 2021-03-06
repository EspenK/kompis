# Kompis | backend

[![Build Status](https://travis-ci.com/pckv/kompis.svg?branch=master)](https://travis-ci.com/pckv/kompis)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a56c6b3d8d6441ba9ac9a2564e6f00f5)](https://www.codacy.com/manual/pc_3/kompis?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pckv/kompis&amp;utm_campaign=Badge_Grade)

## This is Kompis

The Kompis project aims to make it easy to for everyone to get home after a night out. 
We will provide a platform where users can ask for someone to drive them home, or drivers to advertise when they are available.

## API
### Users endpoints
-   [Create user](#create-user) : `POST /users` → *open endpoint*
-   [Authorize](#authorize) : `POST /users/authorize` → *open endpoint*
-   [Get user](#get-user) : `GET /users/{id:number}`
-   [Get current user](#get-current-user) : `GET /users/current`
-   [Delete current user](#delete-current-user) : `DELETE /users/current`
-   [Send firebase token](#send-firebase-token) : `PUT /users/current/firebase`

### Listings endpoints
-   [Get listings](#get-listings) : `GET /listings`
-   [Create listing](#create-listing) : `POST /listings`
-   [Get listing](#get-listing) : `GET /listings/{id:number}`
-   [Delete listing](#delete-listing) : `DELETE /listings/{id:number}`
-   [Activate listing](#activate-listing) : `GET /listings/{id:number}/activate`
-   [Deactivate listing](#deactivate-listing) : `GET /listings/{id:number}/deactivate`
-   [Assign current user to listing](#assign-current-user-to-listing) : `POST /listings/{id:number}/assign`
-   [Unassign user from listing](#unassign-user-from-listing) : `GET /listings/{id:number}/unassign`

## Documentation
### Users
#### Create user
```http
POST /users
```

Creates a new user that can be logged into.

**Request**

Headers: 
```http
Content-Type: application/json
```
 
Body:
```json
{
  "email": "roger@example.com",
  "password": "password",
  "displayName": "Roger Doger"
}
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

-   `409 CONFLICT` if a user with the given email exists

---
#### Authorize
```http
POST /users/authorize
```

Receive authorization for use with endpoints requiring authorization. If the client supports
firebase cloud messaging, a token should be provided in the body.

**Request**

Headers:
```http
Content-Type: application/json
```
 
Body:
```json
{
  "email": "roger@example.com",
  "password": "password",
  "firebaseToken": "(optional)"
}
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

-   `404 CONFLICT` if a user with the given email does not exist
-   `403 FORBIDDEN` if a user with the given email already exist

---
#### Get user
```http
GET /users/{id:number}
```

Get the user with the given ID.

**Request**

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if user with the given id was not found

---
#### Get current user
```http
GET /users/current
```

Get the current authorized user.

**Request**

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

-   `403 FORBIDDEN` if `Authorization` header is invalid

---
#### Delete current user
```http
DELETE /users/current
```

Delete the current authorized user. All listings where this user is 
the assignee will be unassigned and all listings where this user is 
the owner will be deleted.

The client should get rid of the `Authorization` token manually.

**Request**

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if `Authorization` header is invalid

---
#### Send firebase token
```http
PUT /users/current/firebase
```

Update the firebase token for the current authorized user.

**Request**

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

Parameters:
-   *token* - the firebase token assigned to the client of the current authorized user

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if `Authorization` header is invalid

Body:
```json
{
  "id": 1,
  "displayName": "Roger Doger"
}
```

-   `403 FORBIDDEN` if `Authorization` header is invalid

### Listing
#### Get listings
```http
GET /listings
```

Gets a list of all listings.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
[
  {
    "id": 2,
    "title": "Need pickup at Oslo",
    "driver": false,
    "active": true,
    "owner": {
      "id": 1,
      "displayName": "Roger Doger"
    }, 
    "location": {
      "id": 10,
      "latitude": "98.76",
      "longitude": "54.32",
      "accuracy": "99.1"
      },
    "assignee": {
      "id": 12,
      "user": {
        "id": 9,
        "displayName": "Harry"
      },
      "location": {
        "id": 10,
        "latitude": "12.34",
        "longitude": "23.45",
        "accuracy": "99.1"
      }
    }
  }, ...
]
```
-   `403 FORBIDDEN` if `Authorization` header is invalid

---
#### Create listing
```http
POST /listings
```

Creates a new listing owned by the current logged in user.

**Request**

Headers: 
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```
 
Body:
```json
{
  "title": "Need pickup at Oslo",
  "driver": false, 
  "location": {
    "latitude": "98.76",
    "longitude": "54.32",
    "accuracy": "99.1"
  }
}
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 2,
  "title": "Need pickup at Oslo",
  "driver": false,
  "active": true,
  "owner": {
    "id": 1,
    "displayName": "Roger Doger"
  }, 
  "location": {
    "id": 10,
    "latitude": "98.76",
    "longitude": "54.32",
    "accuracy": "99.1"
  },
  "assignee": null
}
```
-   `403 FORBIDDEN` if `Authorization` header is invalid
---
#### Get listing
```http
GET /listings/{id:number}
```

Gets the listing with the given ID.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Content-Type: application/json
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "id": 2,
  "title": "Need pickup at Oslo",
  "driver": false,
  "active": true,
  "owner": {
    "id": 1,
    "displayName": "Roger Doger"
  }, 
  "location": {
    "id": 10,
    "latitude": "98.76",
    "longitude": "54.32",
    "accuracy": "99.1"
  },
  "assignee": null
}
```

-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found

---
#### Delete listing
```http
DELETE /listings/{id:number}
```

Delete the listing with the given ID if it is owned by the current authorized user.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if the listing is not owned by the current authorized user
-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found

---
#### Activate listing
```http
GET /listings/{id:number}/activate
```

Activate the listing with the given ID if it is owned by the current authorized user.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if the listing is not owned by the current authorized user
-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found

---
#### Deactivate listing
```http
GET /listings/{id:number}/deactivate
```

Deactivate the listing with the given ID if it is owned by the current authorized user.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if the listing is not owned by the current authorized user
-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found

---
#### Assign current user to listing
```http
POST /listings/{id:number}/assign
```

Assign the current logged in user to the listing with the given ID and 
store the assignees location in the listing.

**Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

Body:
```json
{
  "latitude": "12.34",
  "longitude": "23.45",
  "accuracy": "99.1"
}
```

**Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found
-   `409 CONFLICT` if the listing already has an assignee

---
#### Unassign user from listing
```http
GET /listings/{id:number}/unassign
```

If the current authorized user is the owner of the listing with the given ID, the assignee will
be removed from the listing. If the current authorized user is assigned to the listing, they
will remove themselves from the listing.

 **Request**

Headers: 
```http
Authorization: Bearer xxx.yyy.zzz
```

 **Responses**

-   `200 OK` on success

Headers:
```http
Authorization: Bearer xxx.yyy.zzz
```

-   `403 FORBIDDEN` if you either don't own the listing, or are not assigned to the listing
-   `403 FORBIDDEN` if `Authorization` header is invalid
-   `404 NOT FOUND` if a listing with the given ID was not found

---