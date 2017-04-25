node {
	def mvnHome = tool 'maven-3'

	stage('Checkout') { 
        checkout scm
    }
    
    stage('Build') { 
        sh "${mvnHome}/bin/mvn -f pom.xml clean package -U -DskipTests"
    }
    
    stage('Test'){
        sh "${mvnHome}/bin/mvn -f pom.xml test"
        junit '**/target/surefire-reports/TEST-*.xml'
    }
    
    stage('Deploy'){
    	sh "scp target/*.jar docker@192.168.1.43:/tmp"
    }
}


