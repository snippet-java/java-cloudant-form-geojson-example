# java-cloudant-form-geojson-example

### Cloudant in BlueMix

This repository is an example Cloudant API enabled application that can be deployed into
Bluemix with only a couple clicks. Try it out for yourself right now by clicking:

[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/snippet-java/java-cloudant-form-geojson-example.git)


### How does this work?

When you click the button, you are taken to Bluemix where you get a pick a name
for your application at which point the platform takes over, grabs the code from
this repository and gets it deployed.

It will automatically create an instance of the Cloudant service, call it
`sample-cloudantNoSQLDB` and bind it to your app. This is where your
app will store its data. If you deploy multiple instances of
app from this repository, they will share the same Cloudant instance.


### Create, Read, Update, Delete (CRUD)

This repository includes examples on the CRUD operations as APIs. It supports GET requests.
You should be able to create database, create documents, list all documents, read single document, 
update single document, and delete single document using the APIs.

If you do clone this repository, make sure you update this `README.md` file to point
the `Deploy to Bluemix` button at your repository.

If you want to change the name of the Cloudant instance that gets created, the memory
allocated to the application or other deploy-time options, have a look in `manifest.yml` and `.bluemix/pipeline.yml`.

The method used in `.bluemix/pipeline.yml` will create an actual pipeline in DevOps.
Where else the method used in the `manifest.yml` is a previous way to create a service. 


### Running locally

If you want to run the app locally, open credential.properties and enter username and password for Cloudant service


### Using Google Map

To properly use Google Map, you will have to include the API key on the script included on the `<head>` section as shown below:

`<script src="https://maps.googleapis.com/maps/api/js?key=<API_KEY>"></script>`

where `<API_KEY>` needs to be obtained from Google. For more info, please refer to the following link: [https://developers.google.com/maps/documentation/javascript/get-api-key](https://developers.google.com/maps/documentation/javascript/get-api-key)



### Page / Form description

The page has 3 sections:
- Item list
- Add / Edit form
- Map

To add an item, fill in the form and use a unique item ID.
To edit / update an item, click the item ID from the list, an modify from the form.

After adding or updating the item, the list will be updated, and the map will be updated as well.


### Available APIs (supports GET methods)

* Create item database:
  * `/cloudant/createdb`
* Create / Insert document:
  * `/cloudant/set?id=3&name=Bob&description=tester&point=101.5000,5.5000`
* List all documents in the database:
  * `/cloudant/list`
* Read 1 document based on ID:
  * `/cloudant/get?id=3`
* Update or insert if not exist, 1 document based on ID:
  * `/cloudant/update?id=3&name=Bob&description=producer`
* Delete 1 document based on ID:
  * `/cloudant/delete?id=3`
* Delete / drop item database:
  * `/cloudant/deletedb`



### Privacy Notice

This package is configured to track deployments to IBM Bluemix and other Cloud Foundry platforms. The following information is sent to a Deployment Tracker service on each deployment:

* Java repository URL
* Application Name (application_name)
* Space ID (space_id)
* Application Version (application_version)
* Application URIs (application_uris)
* Labels of bound services
* Number of instances for each bound service and associated plan information

This data is collected from the Meta-INF --> package.json file in this application and the VCAP_APPLICATION and VCAP_SERVICES environment variables in IBM Bluemix and other Cloud Foundry platforms. This data is used by IBM to track metrics around deployments of this applications to IBM Bluemix to measure the usefulness of our examples, so that we can continuously improve the content we offer to you. Only deployments of sample applications that include code to ping the Deployment Tracker service will be tracked.
