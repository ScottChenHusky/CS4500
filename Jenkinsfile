pipeline {
   agent {
       docker {
           image 'maven:3-alpine'
           args '-v /root/.m2:/root/.m2'
       }
   }

   stages {
       stage('Build') {
           steps {
               echo "Building"
               sh 'mvn -f cs4500-spring2018-team34/pom.xml compile'
               sh 'mvn -f cs4500-spring2018-team34/pom.xml package'
           }
       }
       stage('Test'){
           steps {
               echo "Testing"
               sh 'mvn -f cs4500-spring2018-team34/pom.xml test'
           }
       }
       stage('SonarQube') {
           steps {
               withSonarQubeEnv('SonarQube') {
                   sh 'mvn -f cs4500-spring2018-team34/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true'
                   sh 'mvn -f cs4500-spring2018-team34/pom.xml sonar:sonar'
               }
           }
       }
       stage('Quality') {
           steps {
               sh 'sleep 30'
               timeout(time: 10, unit: 'SECONDS') {
                   retry(5) {
                       script {
                           def qg = waitForQualityGate()
                           if (qg.status == 'OK') {
                               error "Pipeline aborted due to quality gate failure: ${qg.status}"
                           }
                       }
                   }
               }
           }
       }
   }
}

