node {
	def mvnHome = tool 'maven-3.3.9'
	def sshPrefix = "ssh docker@dockerhost"

	stage('Checkout') { 
        checkout scm
    }

    stage('Build & Test & Docker') { 
        sh "${mvnHome}/bin/mvn -f pom.xml clean verify -U"
    }
    
    stage('Deploy'){
    	sh "${sshPrefix} docker stop ${pomArtifactId()} || true;"
    	sh "${sshPrefix} docker pull dockerhost:8082/${pomArtifactId()}:${pomVersion()}"
    	sh "${sshPrefix} docker run --rm --name ${pomArtifactId()} -d -p 8080:8080 dockerhost:8082/${pomArtifactId()}:${pomVersion()}"
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


