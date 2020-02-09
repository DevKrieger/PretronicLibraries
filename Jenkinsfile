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
                    if(name == CI_NAME && email == CI_EMAIL) {
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

                    String branches = sh script: "git branch -vv", returnStdout: true
                    echo branches
                }
            }
        }
        stage('Checkout') {
            when { equals expected: false, actual: SKIP }
            steps {
                script {
                    sh "git branch | xargs git branch -D "
                    sh "git reset --hard"
                    //if(BRANCH.equalsIgnoreCase(BRANCH_DEVELOPMENT)) {
                    sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                        //change to branch
                        sh "git checkout " + BRANCH_DEVELOPMENT
                        sh "git pull origin " + BRANCH_DEVELOPMENT
                    }
                    //}
                }
            }
        }
        stage('Snapshot') {
            when { equals expected: false, actual: SKIP }
            steps {
                script {
                    if(BRANCH.equalsIgnoreCase(BRANCH_DEVELOPMENT)) {
                        if(!VERSION.endsWith("-SNAPSHOT")) {
                            VERSION = VERSION+'-SNAPSHOT'
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
                    sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS deploy'
                }
            }
        }
        stage('Archive') {
            when { equals expected: false, actual: SKIP }
            steps {
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
        stage('Push Development') {
            when { equals expected: false, actual: SKIP }
            steps {
                script {
                    sh "git config --global user.name '$CI_NAME' -v"
                    sh "git config --global user.email '$CI_EMAIL' -v"

                    String[] versionSplit = VERSION.split("[-.]")

                    String major = versionSplit[0]
                    int minorVersion = versionSplit[1].toInteger()
                    int patchVersion = versionSplit[2].toInteger()

                    if(BRANCH.equalsIgnoreCase(BRANCH_DEVELOPMENT)) {
                        patchVersion++
                    } else if(BRANCH.equalsIgnoreCase(BRANCH_MASTER)) {
                        minorVersion++
                        patchVersion = 0
                    }
                    VERSION = major+"."+minorVersion+"."+patchVersion
                    sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION-SNAPSHOT"

                    sh "git add . -v"
                    sh "git commit -m 'Jenkins version change $VERSION-SNAPSHOT' -v"

                    sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                        sh "git push origin HEAD:development -v"
                    }

                    //MASTER PUSH
                    if(BRANCH.equalsIgnoreCase(BRANCH_MASTER)) {
                        sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                            echo 'CHECKOUT MASTER'

                            //sh "git reset --hard"
                            sh "git checkout " + BRANCH_MASTER
                            sh "git pull origin " + BRANCH_MASTER
                        }
                        sh "mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION"

                        sh "git add . -v"
                        sh "git commit -m 'Jenkins version change $VERSION' -v"

                        sshagent(['1c1bd183-26c9-48aa-94ab-3fe4f0bb39ae']) {
                            sh "git push origin HEAD:master -v"
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                if(SKIP) {
                    //deleteBuild(buildName: env.JOB_NAME)
                    //jobsToDelete = [env.JOB_NAME
                    //Hudson.instance.items.each { item -> if (item.name == "${env.JOB_NAME}") { item.delete() } }
                }
            }
        }
    }
}

