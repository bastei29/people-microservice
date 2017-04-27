node {
	def mvnHome = tool 'maven-3.3.9'

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
    
    stage('Dockerize'){
    	sh "${mvnHome}/bin/mvn -f pom.xml docker:build -DpushImage"
    }
    
    stage('Deploy'){
    	def sshPrefix = "ssh docker@dockerhost"
    	sh "${sshPrefix} docker stop ${pomArtifactId()}; ${sshPrefix} docker run --rm --name ${pomArtifactId()} -d -p 8080:8080 dockerhost:8082/${pomArtifactId()}:${pomVersion()}"
    }
}

def pomVersion() {
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}

def pomArtifactId() {
    def matcher = readFile('pom.xml') =~ '<artifactId>(.+)</artifactId>'
    matcher ? matcher[0][1] : null
}


