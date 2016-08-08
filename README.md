# rest-data
ETags, Varnish, Spring Data and MongoDB

## Setup
### Create a local user
`use users;`
`db.createUser( { user: "zzuser", pwd: "yz3QyDZpcu3fEz", roles: [ "readWrite", "dbAdmin" ] })`
### Load the data
In the `RestDataApplication` class, uncomment the `repositoryPopulator` bean.  This loads the data in `users.json` at startup.  Comment it when you're finished.