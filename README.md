# blog
A small web blog to learn about Java backend development.

Supports creating, updating and removing user accounts. Additionally, users can create and remove posts and everybody can comment on posts. The data is stored in a database. 

## Hosted Version
The blog is running and you can try it out: 
- https://hody-blog.azurewebsites.net/blog/api/v1/swagger-ui/index.html
- https://hody-blog.azurewebsites.net/blog/api/v1/posts

The blog contains posts from a publicly available data store (see below for more information on this). I will reset the data base every now and then. 

## Running the Application

### IDE
You can import the project contained into a modern IDE and easily start the application locally from there (tested in VS Code and Intellij).

1. Import project from `<repository_root>/demo`
1. Run maven install
1. Set the environment variable `absoluteDBPath` to point to the data base contained in the repository using an absolute path. That is set `absoluteDBPath=<repository_root>/blog.db` 
1. Run com.example.demo.DemoApplication. 

### Command Line

Only tested with Ubuntu. Make sure you have at least Java JDK 17 and Maven installed.

1. From `<repository_root>/demo` run `mvn install`
1. Execute `env absoluteDBPath=<repository_root>/blog.db java -jar <repository_root>/demo/target/demo-0.0.1-SNAPSHOT.jar` to start the server.

## Interacting with the Application
After starting up the application it will be accessible under http://localhost:8080/. A first simple request might be to access http://localhost:8080/users.

The swagger generated API documentation can be found under http://localhost:8080/swagger-ui/index.html or in json format under http://localhost:8080/v3/api-docs.

## Overview
The configuration is done in `application.properties` and `persistence.properties` and `Config.java`.

The application is as is common structured into three layers: controller layer, service layer and repository layer. For persistence a Sqlite database is used.

The main application resides in `DemoApplication.java`. Here, the Spring application is started. From here via the `CommandLineRunner` interface 
- In `SeedData.java` an example data store is parsed and loaded into the database (see below for more). 
- In `DBPreparation.java` we do some minor preparations of the database.

## Loading Example data store
To fill the blog with a sizable number (~100 authors, ~20000 posts, ~750000 comments) of blog entries you can do the following:

1. Download the [data store](https://www.kaggle.com/datasets/lakritidis/identifying-influential-bloggers-techcrunch)
1. Extract the archive to `DATA_STORE_PATH`
1. Set the following four enviromental variables (additionally to the variables to run the application): 
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
- Fix the Links in the Swagger documentation
- Add some more description to documentation
