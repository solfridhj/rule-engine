# Payment rules Azure Function application

## Application/System Architecture
The rules-engine consists of a Spring Boot application, deployed as an Azure Function app using `spring-cloud-function-adapter-azure`. 

```mermaid
  graph TD;
      A-->B;
      A-->C;
      B-->D;
      C-->D;
```


## Local development

Need Azure Functions Core Tools to run and test the function locally. Local running will use the `local.settings.json` file.
https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java
On macOS:

`
brew tap azure/functions
`

`
brew install azure-functions-core-tools@4
`

`
mvn package
`

`
mvn azure-functions:run
`

Will run the function on your localhost on port 7071.

## Infrastructure
Created in Azure.

1. Manual: Create a new subscription
2. Manual: Create the Azure Function in the consumption tier - could and should be automated
3. Automatic: Commits to main builds and deploys code to the Azure function


In the GitHub actions pipeline, a secret has to be added so we can login to deploy the function:
https://learn.microsoft.com/en-us/azure/developer/github/connect-from-azure?tabs=azure-cli%2Clinux

The pipeline is using the "Use the Azure login action with a service principal secret" approach, and setting
the AZURE_CREDENTIALS secret to allow auth for deployment. The secret is obtained from:
az ad sp create-for-rbac --name "myApp" --role contributor \
--scopes /subscriptions/{{subId}}/resourceGroups/test-group-dev  \
--json-auth