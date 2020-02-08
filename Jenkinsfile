String VERSION = "UNDEFINED"
String BRANCH = "UNDEFINED"
pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'Java8'
    }
    stages {
        stage('Read informations') {
            steps {
                script {
                    VERSION = readMavenPom().getVersion()
                    BRANCH = env.GIT_BRANCH
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
        stage('Manage Versions') {
            steps {
                script {
                    if(BRANCH.equalsIgnoreCase("origin/development")) {
                        echo 'DEVELOPMENT BRANCH'
                        String[] versionSplit = VERSION.split("[-.]")
                        String major = versionSplit[0]
                        String minor = versionSplit[1]
                        String patch = versionSplit[2]
                        int patchVersion = patch.toInteger()
                        patchVersion++;
                        VERSION = major+"."+minor+"."+patchVersion+"-SNAPSHOT"

                    } else if(BRANCH.equalsIgnoreCase("master")) {

                    }
                }
                sh "mvn versions:set -DnewVersion=${VERSION}"

                /*sh "git config --global user.email 'jenkinsci@pretronic.net'"
                sh "git config --global user.name 'JenkinsCI'"

                sh "git add ."
                sh "git commit -m 'TEST'"
                sh "git push origin origin/development"*/

                withCredentials([sshUserPrivateKey(credentialsId: '1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae', keyFileVariable: 'SSH_KEY')]) {
                    sh "git add ."
                    sh "git commit -m \"Jenkins version change\"\n"
                    sh "git push origin origin/development"
                    //sh "git push origin <local-branch>:<remote-branch>"
                }
            }
        }
    }
}
