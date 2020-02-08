pipeline {
    agent {
        docker {
            label 'docker'
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                echo 'Test'
                sh 'printenv'
                echo 'Pulling...' + env.BRANCH_NAME
                sh 'mvn -B -DskipTests clean install'
            }
        }
        stage('Install') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
        stage('Deploy') {
            steps {
                configFileProvider([configFile(fileId: 'afe25550-309e-40c1-80ad-59da7989fb4e', variable: 'MAVEN_GLOBAL_SETTINGS')]) {
                    sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS deploy'
                }
            }
        }
    }
}
