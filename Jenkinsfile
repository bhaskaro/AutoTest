node{
   stage('SCM Checkout'){
     git 'https://github.com/bhaskaro/SpringBootTest'
   }
   stage('Compile-Package'){
    
      def mvnHome =  tool name: 'maven-3', type: 'maven'   
      sh "${mvnHome}/bin/mvn clean compile package"
   }
   stage('Email Notification'){
     // mail bcc: '', body: '''Hi Welcome to jenkins email alerts
      //Thanks
      //Hari''', cc: '', from: '', replyTo: '', subject: 'Jenkins Job', to: 'hari.kammana@gmail.com'
echo "mail sent successfully to bhaskaro@gmai.com"
   }
   stage('deploy to tomcat'){
       echo "deployihng to tomat now"
   }
}
