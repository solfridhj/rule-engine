targetScope = 'subscription'

// TODO - could send in as parameters and allow deploying to different envs easily
var location  = 'norwayeast'
var env  = 'preprod'
var owner  = 'solfrid.hagen.johansen@outlook.com'

var resourceGroupName  = 'payment-engine-rg-${env}'

var commonTags =   {
    env: env
    owner: owner
  }


// Resource Group
resource resourceGroup 'Microsoft.Resources/resourceGroups@2021-04-01' = {
  name: resourceGroupName
  location: location
  tags: commonTags
}

// All infra needed due to the function app
module functionApp './modules/functionapp.bicep' = {
  name: uniqueString('functionAppResources')
  scope: resourceGroup
  params: {
    location: location
    commonTags: commonTags
  }
}

// TODO - Database - Wanted to get the free tier which you can only have one of. Need to see if I will ad the DB here or not... 

// TODO - Could setup alerts for issues or downtime. Could e.g. be alerts if a certain rule is not triggered as well.
