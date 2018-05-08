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
				sh 'mvn clean package -B'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.25.126:9000 -Dsonar.login=74881713522900d0ec5dc5a0ad9e303480b307a8'
            }
        }
    }
}