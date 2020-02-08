String VERSION = "UNDEFINED"
String BRANCH = "UNDEFINED"
pipeline {
    agent {
        docker {
            label 'docker'
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Read informations') {
            steps {
                script {
                    VERSION = readMavenPom().getVersion()
                    String branchFullName = env.GIT_BRANCH
                    String[] branchSplit = branchFullName.split("/")
                    BRANCH = branchSplit[1]
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
                    if(BRANCH.equalsIgnoreCase("development")) {
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
                sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                    sh "git add ."
                    sh "git commit -m \"Jenkins version change\"\n"
                    sh "git push origin development"
                }
                /*withCredentials([sshUserPrivateKey(credentialsId: '1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae', keyFileVariable: 'SSH_KEY')]) {
                    sh "git add ."
                    sh "git commit -m \"Jenkins version change\"\n"
                    sh "git push origin development"
                    //sh "git push origin <local-branch>:<remote-branch>"
                }*/
            }
        }
    }
}
