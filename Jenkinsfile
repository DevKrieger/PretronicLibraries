

pipeline {
    node {
        configFileProvider(
            [configFile(fileId: 'afe25550-309e-40c1-80ad-59da7989fb4e', variable: 'MAVEN_SETTINGS')]) {
            sh 'mvn -s $MAVEN_SETTINGS clean package'
        }

    }
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
                withMaven(mavenSettingsConfig: '02bdb066-9ce4-4ef4-8989-5ef34886855d') {
                sh 'mvn deploy'
             }
            }
        }
    }
}
