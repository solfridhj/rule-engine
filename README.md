# Payment rules API

## Infrastructure
Created in Azure.

1. Manual: Create a new subscription
2. Manual: Create a new resource group


## Local development

Need Azure Functions Core Tools to run and test the function locally. Local running will use the local.settings.json file.
https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java
On macOS:
brew tap azure/functions
brew install azure-functions-core-tools@4

mvn package
mvn azure-functions:run