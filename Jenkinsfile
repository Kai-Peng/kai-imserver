pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v C:/Users/72429/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}