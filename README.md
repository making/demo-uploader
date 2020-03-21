

### Create a SMB file server

This instruction uses [Azure Files](https://docs.microsoft.com/azure/storage/files/) as a SMB File server.

```
export TENANT_ID=....
export CLIENT_ID=....
export CLIENT_SECRET=....
export LOCATION="Japan East"
export RESOURCE_GROUP=azure-files-demo
export STORAGE_ACCOUNT_NAME=makiazurefilesdemo
export SHARE_NAME=demoshare

az login --username ${CLIENT_ID} \
         --password ${CLIENT_SECRET} \
         --service-principal \
         --tenant ${TENANT_ID} 

az group create --name ${RESOURCE_GROUP} \
  --location "${LOCATION}"

az storage account create --name ${STORAGE_ACCOUNT_NAME} \
  --resource-group ${RESOURCE_GROUP} \
  --location "${LOCATION}" \
  --kind StorageV2 \
  --sku Standard_ZRS \
  --enable-large-file-share \
  --output none

# OR
# 
# az storage account create --name ${STORAGE_ACCOUNT_NAME} \
#   --resource-group ${RESOURCE_GROUP} \
#   --location "${LOCATION}" \
#   --kind FileStorage \
#   --sku Premium_LRS \
#   --output none

STORAGE_ACCOUNT_KEY=$(az storage account keys list \
    --resource-group ${RESOURCE_GROUP} \
    --account-name ${STORAGE_ACCOUNT_NAME} \
    --query "[0].value" | tr -d '"')

az storage share create \
    --account-name ${STORAGE_ACCOUNT_NAME} \
    --account-key ${STORAGE_ACCOUNT_KEY} \
    --name ${SHARE_NAME} \
    --quota 1024 \
    --output none     
```

### Deploy the application


```
./mvnw clean package -DskipTests=true

cf push demo-uploader -p target/demo-uploader-0.0.1-SNAPSHOT.jar --no-start
cf create-service smb Existing demo-smb -c "{\"share\": \"//${STORAGE_ACCOUNT_NAME}.file.core.windows.net/${SHARE_NAME}\", \"version\": \"3.0\"}"
cf bind-service demo-uploader demo-smb -c "{\"username\": \"${STORAGE_ACCOUNT_NAME}\", \"password\": \"${STORAGE_ACCOUNT_KEY}\"}"
cf start demo-uploader
```