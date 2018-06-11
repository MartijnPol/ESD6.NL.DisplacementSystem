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
        stage('Build image'){
            when{
                anyOf{
                    branch 'master'
                    branch 'release'
                    branch 'DockerTest'
                }
            }
            steps{
                sh 'mvn clean package -B'
				sh 'docker build -t displacementsystem .'
				sh 'docker tag displacementsystem:latest esd6nl/ds'

                archiveArtifacts artifacts: 'target/DisplacementSystem.war', fingerprint: true
            }

        }
		stage('Push image'){
            when{
                anyOf{
                    branch 'master'
                    branch 'release'
                    branch 'DockerTest'

                }
            }
			steps{
				withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'dockerPassword', usernameVariable: 'dockerUser')]) {
					sh "docker login -u ${env.dockerUser} -p ${env.dockerPassword}"
					sh 'docker push esd6nl/ds'
			}
			}
		}
        stage('Test sonarqube'){
            steps{
                sh 'mvn clean test -B'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://192.168.25.126:9000 -Dsonar.login=74881713522900d0ec5dc5a0ad9e303480b307a8'

            }
        }
        stage('Deploy master'){
            when{
                branch 'master'
            }
            steps{
                sh 'mvn clean package -B'
            }
        }
        stage('Deploy release'){
            when{
                branch 'release'
            }
            steps{
                sh 'mvn clean package -B'
            }
        }
    }
}