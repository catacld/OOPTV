
# OOP TV

An implementation of the backend of a streaming service, 
written in java. The commands executed by the user are given inside
a JSON file. The user can access multiple pages, and on each page
execute two types of actions: either "on page", or "change page".

## Pages

### **Unauthenticated Homepage**

The starting page of the service, singleton since it is the same
for any unauthenticated user accessing the platform.

##### **Possible actions**
##### **(Change page) login** - Go to the login page.
##### **(Change page) register** - Go to the register page.

### **Login**

The login page, where the user enters the credentials to log in,
singleton since it is the same for any unauthenticated user trying 
to login.

##### **Possible actions**
##### **(On page) login** - Try to execute the login command with the credentials given as a parameter. If the credentials are bad, the command fails.

### **Register**

The register page, where the user creates a new account, singleton
since it is the same for any user trying to register.

##### **Possible actions**
##### **(On page) register** - Try to execute the register command with the credentials given as a parameter. If the username entered is already taken, the command fails.

### **Authenticated Homepage**

The homepage for an authenticated user.

##### **Possible actions**
##### **(Change page) movies** - Navigate to the page of movies.
##### **(Change page) upgrades** - Navigate to the "upgrades" page, from where a user can upgrade their account or buy more tokens.
##### **(Change page) logout** - Navigate to the "logout" page and
log out from the current account.

### **Movies**

The movies available to the user are displayed on this page.

##### **Possible actions**
##### **(Change page) authenticated homepage** - Navigate to the home page.
##### **(Change page) see details** - Navigate to a movie's page.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(On page) search** - Search for a specific movie from the list.
##### **(On page) filter** - Filter the list of movies by multiple criteria.

### **Upgrades**

On this page, the user can upgrade their account or buy more tokens.

##### **Possible actions**
##### **(Change page) movies** - Navigate to the page of movies.
##### **(Change page) authenticated homepage** - Navigate to the home page.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(On page) buy tokens** - Buy more tokens.
##### **(On page) buy premium account** - upgrade the account's status from standard to premium.

### **Logout**

Log the current user out and navigate to the unauthenticated home page.

### **See details**

More details about a specific movie are displayed on this page.

##### **Possible actions**
##### **(Change page) authenticated homepage** - Navigate to the home page.
##### **(Change page) movies** - Navigate to the movies page.
##### **(Change page) upgrades** - Navigate to the "upgrades" page, from where a user can upgrade his account or buy more tokens.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(On page) purchase** - Purchase a movie for the user.
##### **(On page) watch** - Watch a movie (the movie must have been purchased first).
##### **(On page) like** - Like a movie (the movie must have been watched first).
##### **(On page) rate** - Rate a movie (the movie must have been watched first, and the rating must be between 1 and 5).


