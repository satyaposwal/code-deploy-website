pipeline {
    agent any
    stages {
        stage('Pre-Build') {
            steps {
                echo 'Any Pre-requisite steps before the CodeBuild'
            }
        }
        stage('Build on AWS CodeBuild') {
            steps {
                withAWS(region:'us-east-1',credentials:'AWS_poswal09_92@gmail') {
                awsCodeBuild artifactLocationOverride: 'saty-art09', 
                artifactNameOverride: '', 
                artifactNamespaceOverride: 'BUILD_ID', 
                artifactPackagingOverride: 'ZIP', 
                artifactPathOverride: '', 
                artifactTypeOverride: 'S3', 
                credentialsId: 'sanyam', 
                credentialsType: 'keys', 
                downloadArtifacts: 'false', 
                environmentTypeOverride: 'LINUX_CONTAINER', 
                gitCloneDepthOverride: '1', 
                projectName: 'satyendra09', 
                region: 'us-east-1', 
                reportBuildStatusOverride: 'True', 
                sourceControlType: 'project', 
                sourceLocationOverride: 'https://github.com/satyaposwal/code-deploy-website.git', 
                sourceTypeOverride: 'GITHUB'
                }   
            }
        }
        stage('Post_Build') {
            steps {
                publishHTML([allowMissing: false, 
                alwaysLinkToLastBuild: false, 
                keepAll: false, 
                reportDir: '/var/lib/jenkins',
                reportFiles: 'index.html', 
                reportName: 'HTML-Report', 
                reportTitles: ''])
            }
        }
         stage('Upload to S3 Bucket') {
            steps {
                dir("/var/lib/jenkins/jobs/htmlpublisher/htmlreports/HTML-Report") {
                    withAWS(region:'us-east-1',credentials:'AWS_poswal09_92@gmail') {
                        s3Upload(
                            pathStyleAccessEnabled: true,
                            payloadSigningEnabled: true,
                            file: 'index.html',
                            bucket: 'qaautomation-htmlreport-bckt123',
                            path: "html-report"
                        )
                    }
                }
            }    
        }
       
    }
}
