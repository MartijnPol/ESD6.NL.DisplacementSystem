pipeline{
    agent any
    tools {
         maven 'maven'
         jdk 'jdk8'
     }
    stages{
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage('Build project'){
            steps {
                sh 'mvn compile'
                archiveArtifacts artifacts: 'target/', fingerprint: true
            }
        }
        stage('Test sonarqube'){
            steps{
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.25.121:9000 -Dsonar.login=33d2bf0931e4ba870789a1cf8e6276a20de55fe1'

            }
        }
    }
}