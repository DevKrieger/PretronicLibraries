rtServer (
    id: 'pretronic-opensource',
    url: 'http://deyna.ch:8081',
    credentialsId: '02bdb066-9ce4-4ef4-8989-5ef34886855d'
    timeout = 300
)
rtUpload (
    serverId: 'pretronic-opensource',
    spec: '''{
          "files": [
            {
              "pattern": "~.m2/net/prematic/libraries/**",
              "target": "pretronic-opensource/"
            }
         ]
    }''',
    failNoOp: true,
)
rtPublishBuildInfo (
    serverId: 'pretronic-opensource',
)
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
        stage('deploy') {
            steps {
               
            }
        }
    }
}
