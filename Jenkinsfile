node 
{
  stage('Checkout')
  {
    checkout scm
  }
  
  stage('Compile')
  {
    sh 'mvn clean package -Dmaven.test.skip=true'
  }
  
  stage('Unit-Tests')
  {
    sh 'mvn test'
  }
  
  stage('Release')
  {
    sh 'mvn docker:build -DpushImageTag'
  }
}