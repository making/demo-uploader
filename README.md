```
./mvnw clean package -DskipTests=true

cf push demo-uploader -p target/demo-uploader-0.0.1-SNAPSHOT.jar --no-start
cf create-service nfs Existing demo-nfs -c '{"share":"10.0.8.230/export/vol1"}'
cf bind-service demo-uploader demo-nfs -c '{"uid":"1000","gid":"1000"}'
cf start demo-uploader
```