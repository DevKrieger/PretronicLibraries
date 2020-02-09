import hudson.model.*

String CI_NAME = "PretronicCI"
String CI_EMAIL = "ci@pretronic.net"

String VERSION = "UNDEFINED"
String BRANCH = "UNDEFINED"

final String BRANCH_DEVELOPMENT = "origin/development"
final String BRANCH_MASTER = "origin/master"

boolean SKIP = false

pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'Java8'
    }
    options {
        buildDiscarder logRotator(numToKeepStr: '10')
    }
    stages {
        stage('CI Check') {
            steps {
                script {
                    String name = sh script: 'git log -1 --pretty=format:\"%an\"', returnStdout: true
                    String email = sh script: 'git log -1 --pretty=format:\"%ae\"', returnStdout: true
                    if (name == CI_NAME && email == CI_EMAIL) {
                        SKIP = true;
                    }
                }
            }
        }
        stage('Read information') {
            when { equals expected: false, actual: SKIP }
            steps {
                script {
                    VERSION = readMavenPom().getVersion()
                    BRANCH = env.GIT_BRANCH
                }
            }
        }
        stage('Snapshot') {
            when { equals expected: false, actual: SKIP }
            steps {
                script {
                    if (BRANCH.equalsIgnoreCase(BRANCH_DEVELOPMENT)) {
                        if (!VERSION.endsWith("-SNAPSHOT")) {
                            VERSION = VERSION + '-SNAPSHOT'
                        }
                        sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=${VERSION}"
                    }
                }
            }
        }
        stage('Build') {
            when { equals expected: false, actual: SKIP }
            steps {
                sh 'mvn -B clean install'
            }
        }
        stage('Deploy') {
            when { equals expected: false, actual: SKIP }
            steps {
                configFileProvider([configFile(fileId: 'afe25550-309e-40c1-80ad-59da7989fb4e', variable: 'MAVEN_GLOBAL_SETTINGS')]) {
                    //sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS deploy'
                }
            }
        }
        stage('Archive') {
            when { equals expected: false, actual: SKIP }
            steps {
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
    }
    post {
        success {
            script {
                if(!SKIP) {
                    sh "git config --global user.name '$CI_NAME' -v"
                    sh "git config --global user.email '$CI_EMAIL' -v"


                    String[] versionSplit = VERSION.split("[-.]")

                    String major = versionSplit[0]
                    int minorVersion = versionSplit[1].toInteger()
                    int patchVersion = versionSplit[2].toInteger()

                    if (BRANCH == BRANCH_DEVELOPMENT) {
                        patchVersion++

                        String version = major + "." + minorVersion + "." + patchVersion + "-SNAPSHOT"
                        sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version"
                        sh "git add . -v"
                        sh "git commit -m 'Jenkins version change $version' -v"

                        sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                            sh "git push origin HEAD:development -v"
                        }
                    } else if (BRANCH == BRANCH_MASTER) {
                        //
                        //Folder für development, checkout, pull, änderung, push
                        minorVersion++
                        patchVersion = 0
                        String version = major + "." + minorVersion + "." + patchVersion

                        sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                            sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version"
                            sh "git add . -v"
                            sh "git commit -m 'Jenkins version change $version' -v"
                            sh "git push origin HEAD:master -v"

                            sh """
                            if [ -d "tempDevelopment" ]; then rm -Rf $WORKING_DIR; fi
                            mkdir tempDevelopment
                            cd tempDevelopment/
                            git clone --single-branch --branch development git@github.com:DevKrieger/PrematicLibraries.git
                      
                            cd PrematicLibraries/
                            mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$version-SNAPSHOT

                            git add . -v
                            git commit -m 'Jenkins version change $version-SNAPSHOT' -v
                            git push origin HEAD:development -v
                            """
                        }
                    }
                }
            }
        }
    }
}

