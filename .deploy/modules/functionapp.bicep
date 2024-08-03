param location string
param commonTags object

var env = commonTags.env

var appServicePlanName = 'payment-rules-appserviceplan-${env}'
var functionAppName = 'payment-rules-fapp-${env}'
var storageAccountName  = 'paymentrulessa${env}'


resource storageAcct 'Microsoft.Storage/storageAccounts@2022-09-01' = {
  name: storageAccountName
  location: location
  sku: {
    name: 'Standard_LRS'
  }
  kind: 'StorageV2'
  properties: {
    supportsHttpsTrafficOnly: true
    minimumTlsVersion: 'TLS1_2'
    defaultToOAuthAuthentication: true
  }
  tags: commonTags
}

resource appServicePlan 'Microsoft.Web/serverfarms@2022-09-01' = {
  name: appServicePlanName
  location: location
  tags: commonTags
  kind: 'linux'
  sku: {
    name: 'Y1'
    tier: 'Dynamic'
  }
}

resource functionApp 'Microsoft.Web/sites@2021-02-01' = {
  name: functionAppName
  location: location
  kind: 'functionapp,linux'
  tags: commonTags
  properties: {
    serverFarmId: appServicePlan.id
    enabled: true
    siteConfig: {
      numberOfWorkers: 1
      functionAppScaleLimit: 5

    }
    httpsOnly: true
  }
    
}
