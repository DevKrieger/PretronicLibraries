def VERSION = "UNDEFINED";
pipeline {
    agent {
        docker {
            label 'docker'
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Read version') {
            steps {
                script {
                    VERSION = readMavenPom().getVersion();
                }
            }
        }
        stage('Mark as Snapshot') {
           steps {
               script {
                   if(!VERSION.endsWith("-SNAPSHOT")) {
                       VERSION = VERSION+'-SNAPSHOT'
                   }
               }
               sh "mvn versions:set -DnewVersion=${VERSION}"
           }
       }
        stage('DEBUG') {
            steps {
                echo 'Test DEV2'
                sh 'printenv'
                echo 'Pulling...' + env.GIT_BRANCH
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
            }
        }
        stage('Deploy') {
            steps {
                configFileProvider([configFile(fileId: 'afe25550-309e-40c1-80ad-59da7989fb4e', variable: 'MAVEN_GLOBAL_SETTINGS')]) {
                    sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS deploy'
                }
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
        stage('Increment PatchVersion') {
            steps {
                script {
                    String[] versionSplit = VERSION.split("[-.]")
                    String major = versionSplit[0]
                    String minor = versionSplit[1]
                    String patch = versionSplit[2]
                    int patchVersion = patch.toInteger()
                    patchVersion++;
                    VERSION = major+"."+minor+"."+patchVersion+"-SNAPSHOT"
                }
                sh "mvn versions:set -DnewVersion=${VERSION}"
            }
        }
    }
}
