targetScope = 'subscription'

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

// TODO - Application insights

// All infra needed due to the function app
module functionApp './modules/functionapp.bicep' = {
  name: uniqueString('functionAppResources')
  scope: resourceGroup
  params: {
    location: location
    commonTags: commonTags
  }
}

// TODO - Database



// TODO - Alerts
