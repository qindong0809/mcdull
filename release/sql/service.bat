
start cmd /k "java -jar ../mcdull-support/mcdull-gateway/target/mcdull-gateway-1.0-SNAPSHOT.jar  --spring.profiles.active=dev "

start cmd /k "java -jar ../mcdull-support/mcdull-mdc/mcdull-mdc-provider/target/mcdull-mdc-provider-1.0-SNAPSHOT.jar  --spring.profiles.active=dev  "

start cmd /k "java -jar ../mcdull-support/mcdull-uac/mcdull-uac-provider/target/mcdull-uac-provider-1.0-SNAPSHOT.jar  --spring.profiles.active=dev "
