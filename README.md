# rest-data
This is a small vignette that demonstrates how to expose entities using Spring Data REST that use ETags for caching

## Setup
### Create a local user
`use users;`
`db.createUser( { user: "zzuser", pwd: "yz3QyDZpcu3fEz", roles: [ "readWrite", "dbAdmin" ] })`
### Load the data
In the `RestDataApplication` class, uncomment the `repositoryPopulator` bean.  This loads the data in `users.json` at startup.  Comment it when you're finished.

## References
| Description | Link |
| ---- | ---- |
| Spring Data REST Reference Page | http://projects.spring.io/spring-data-rest/ |