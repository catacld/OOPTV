# OOP TV

An implementation of the backend of a streaming service, written in java. The commands executed by the user are given inside a JSON file. The user can access multiple pages, and on each page execute two types of actions: either **"on page"**, or **"change page"**. A user can also subscribe to a genre of movie, using the **"subscribe"** action, being notified when a new movie with that genre is added to the database. The user can also navigate back between pages, using the **"back"** command. Movies can be added or removed from the database using **"database add"**, respectively **"database delete"**. There is also a recommendation system that recommends a movie to a premium user, based on their preferences.

## Pages

### **Unauthenticated Homepage**

The starting page of the service is singleton since it is the same
for any unauthenticated user accessing the platform.

##### **Possible actions**
##### **(Change page) login** - Go to the login page.
##### **(Change page) register** - Go to the register page.

### **Login**

The login page, where the user enters the credentials to log in,
singleton since it is the same for any unauthenticated user trying 
to login.

##### **Possible actions**
##### **(Change page) login** - Refreshes the page.
##### **(On page) login** - Try to execute the login command with the credentials given as a parameter. If the credentials are bad, the command fails.

### **Register**

The register page, where the user creates a new account, singleton
since it is the same for any user trying to register.

##### **Possible actions**
##### **(Change page) register** - Refreshes the page.
##### **(On page) register** - Try to execute the register command with the credentials given as a parameter. If the username entered is already taken, the command fails.

### **Authenticated Homepage**

The homepage for an authenticated user.

##### **Possible actions**
##### **(Change page) movies** - Navigate to the page of movies.
##### **(Change page) see details** - Refreshes the page.
##### **(Change page) logout** - Navigate to the "logout" page and
log out from the current account.

### **Movies**

The movies available to the user are displayed on this page.

##### **Possible actions**
##### **(Change page) authenticated homepage** - Navigate to the home page.
##### **(Change page) see details** - Navigate to a movie's page.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(Change page) movies** - Refreshes the page.
##### **(On page) search** - Search for a specific movie from the list.
##### **(On page) filter** - Filter the list of movies by multiple criteria.

### **Upgrades**

On this page, the user can upgrade their account or buy more tokens.

##### **Possible actions**
##### **(Change page) movies** - Navigate to the page of movies.
##### **(Change page) authenticated homepage** - Navigate to the home page.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(Change page) upgrades** - Refreshes the page.
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
##### **(Change page) see details** - Refreshes the page.
##### **(Change page) logout** - Navigate to the "logout" page and log out from the current account.
##### **(On page) purchase** - Purchase a movie for the user.
##### **(On page) watch** - Watch a movie (the movie must have been purchased first).
##### **(On page) like** - Like a movie (the movie must have been watched first).
##### **(On page) rate** - Rate a movie (the movie must have been watched first, and the rating must be between 1 and 5).

## **Database commands**

##### **Database add** - Adds a new movie to the database, only if the movie is not already in the database. All the users who are subscribed to at least one of the genres of the movie, and who do not live in a country where the movie is banned, will be notified of the addition of the movie.

##### **Database add** - Deletes a movie from the database, only if the movie exists in the database. All the users who have purchased the movie will be refunded (a standard user will get 2 tokens back, and a premium user will get a free movie back), and the movie will be deleted from all of their lists of movies (purchased movies, watched movies, rated movies, liked movies, etc.)

## **Back command**
##### The user goes back to the previously viewed page, if possible.

## **Subscribe command**
##### The user subscribes to a genre of movies, only if they are not already subscribed. The action can only be performed while on the "see details" page of a movie and only to one of the genres of the movie whose "see details" page the user is on.

## **Recommendation feature**
##### At the end of the given actions, if there is a premium user connected to the streaming service a recommendation of a movie will be made to the user and sent as a notification. The recommendation algorithm sorts the genres of movies descending by the number of likes given to each genre by the user, or lexicographically if the number of likes is equal and then each movie of each genre descending by the number of total likes. Each genre of movie which has at least one like given by the user will be iterated through until a movie that has not been seen by the current user is found. The movie name is then sent as a notification to the user. If no movie is found to be recommended, the movie will get a notification with the message "No recommendation".


## Design patterns used
##### **Singleton** - for pages that are the same for any user accessing the platform, such as unauthenticated homepage, register page, and login page.
#####  **Factory** - when changing the page the user is viewing, after executing a successful "change page" action.
##### **Observer** - when notifying the user of the addition of a new movie to the database.
##### **Builder** - when making a new instance of a notification. Using this pattern makes the implementation easily scalable if new fields will be added to notifications in the future.
##### **Strategy** - when processing the commands given as input inside the JSON file.
