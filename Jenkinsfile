#!/usr/bin/env groovy

pipeline {
    agent any
    environment {
    	NOME_PROJETO='indexacao-radioagencia'
        CANAL_CHATOPS = 'sepor'
        EMAIL_SECAO = 'sepor.cenin@camara.leg.br'
    }

    tools {
        maven 'Maven 3.3.9'
    }

    stages {
        stage('Git checkout') {
            steps {
                checkout scm
                script {
                	VERSAO_POM = readMavenPom().getVersion()
                }
            }
        }
        stage('Compilação e testes') {
            steps {
                sh "mvn -B -U -e -V clean package"
            }
        }
        stage('Arquivar') {
            steps {
            	junit '**/target/surefire-reports/TEST-*.xml'
                archive "target/${NOME_PROJETO}-${VERSAO_POM}.jar"
            }
        }
        stage('Git tag e Deploy no Nexus') {
            when {
                branch 'master'
                expression {
                    return ! VERSAO_POM.endsWith('SNAPSHOT')
                }
            }
            environment {
                NOME_TAG = "v${VERSAO_POM}"
            }
            steps {
               sh "mvn deploy"
               
               sshagent(['ssh_git']) {
                    sh "git config user.email \"${env.EMAIL_SECAO}\""
                    sh "git config user.name \"Jenkins\""
                    sh "git tag -a ${NOME_TAG} -m \"Tag criada pelo Jenkins\""
                    sh('git push --tags')
	           }
            }
        }
        stage('Atualização da versão no pom.xml') {
             when {
                branch 'master'
                expression {
                    return ! VERSAO_POM.endsWith('SNAPSHOT')
                }
            }
            steps {
                script {
                    def indiceUltimoPonto = VERSAO_POM.lastIndexOf('.')
                    def versaoMinor = VERSAO_POM.substring(indiceUltimoPonto + 1) as Integer
                    def proximaVersaoMinor = versaoMinor + 1
                    PROXIMA_VERSAO = VERSAO_POM.substring(0, indiceUltimoPonto + 1) + proximaVersaoMinor + '-SNAPSHOT'
                }

                echo "Atualizando pom.xml para versão ${PROXIMA_VERSAO}"
                sshagent(['ssh_git']) {
					sh "git checkout master"
                    sh "git pull"
                    sh "mvn org.codehaus.mojo:versions-maven-plugin:2.5:set -DnewVersion=${PROXIMA_VERSAO} -DgenerateBackupPoms=false"
                    sh "git add pom.xml && git commit -m \"Versão do pom atualizada para próximo SNAPSHOT\""
                    sh "git push origin master"
                }
            }
        }
    }

    post {
        changed {
            mail to: "${env.EMAIL_SECAO}",
                subject: "Job ${env.JOB_NAME}-${env.BUILD_DISPLAY_NAME} - ${currentBuild.currentResult}",
                body: """
                    O job ${env.JOB_NAME} no branch ${env.BRANCH_NAME} mudou de status para: ${currentBuild.currentResult}

                    ${currentBuild.absoluteUrl}"""
        }
    }
}
