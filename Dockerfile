FROM airhacks/glassfish
COPY ./target/DisplacementSystem.war ${DEPLOYMENT_DIR}
