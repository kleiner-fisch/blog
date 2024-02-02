# blog
A small web blog to learn about Java backend development.

Supports creating, updating and removing user accounts. Additionally, users can create and remove posts and everybody can comment on posts. The data is stored in a database. 

To show how the blog works with a lot of data we the option to add blog data from a public data store (see below).

## Loading Example data store
To fill the blog with a sizable number of blog entries you can do the following:

1. Download the [data store](https://www.kaggle.com/datasets/lakritidis/identifying-influential-bloggers-techcrunch)
1. Extract the archive to `DATA_STORE_PATH`
1. Set the following four enviromental variables: 
      ```
      {
        "dataStorePostsPath" : "<DATA_STORE_PATH>/posts.csv",
        "dataStoreCommentsPath" : "<DATA_STORE_PATH>/comments.csv",
        "dataStoreUsersPath" : "<DATA_STORE_PATH>/authors.csv" ,
        "seedBlogData" : true
      }
      ```
1. Start the server.

## TODO
- how to run & vcopile
- dependecies?
- Write proper documentation
- Ensure this works on other computers 
  - initializing previously not existing DB
- API documentation using Swagger?
- Currently we have passwords.. Should have authentification
- Tests
- add method to get all posts/comments for username/name
  - helps to have more significant tests for repository
- Add proper links in responses to allow discovery of the API by responses