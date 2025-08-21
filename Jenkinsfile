pipeline {
  agent any

  environment {
    APP_NAME       = "wayfair-catalog"
    DOCKER_HUB_REPO= "gattisuresh/${APP_NAME}"
    AWS_REGION     = "ap-south-1"
    AWS_ACCOUNT_ID = "015800952701"
    ECR_REPO       = "${APP_NAME}"
    ECR_URI        = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}"
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test (Maven)') {
      steps {
        dir('app') {
          sh 'mvn -B -q clean package'
        }
      }
      post {
        always { junit 'app/target/surefire-reports/*.xml' }
      }
    }

    stage('SonarQube (optional)') {
      when { expression { return false } } // set true when Sonar configured
      steps {
        withSonarQubeEnv('SonarQubeServer') {
          sh 'sonar-scanner'
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh """
          docker build -t ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER} -t ${DOCKER_HUB_REPO}:latest -f docker/Dockerfile .
        """
      }
    }

    stage('Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub_credentials', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
          sh """
            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
            docker push ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER}
            docker push ${DOCKER_HUB_REPO}:latest
          """
        }
      }
    }

    stage('Push to AWS ECR') {
      steps {
        withAWS(credentials: 'aws_credentials', region: "${AWS_REGION}") {
          sh """
            aws ecr describe-repositories --repository-names ${ECR_REPO} || \
            aws ecr create-repository --repository-name ${ECR_REPO}

            aws ecr get-login-password --region ${AWS_REGION} | \
              docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

            docker tag ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER} ${ECR_URI}:${env.BUILD_NUMBER}
            docker tag ${DOCKER_HUB_REPO}:${env.BUILD_NUMBER} ${ECR_URI}:latest
            docker push ${ECR_URI}:${env.BUILD_NUMBER}
            docker push ${ECR_URI}:latest
          """
        }
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        withKubeConfig(credentialsId: 'kubeconfig_cred') {
          sh """
            kubectl apply -f k8s/namespace.yaml
            kubectl -n wayfair-dev apply -f k8s/deployment.yaml
            kubectl -n wayfair-dev apply -f k8s/service.yaml

            # roll the image to the build tag
            kubectl -n wayfair-dev set image deployment/wayfair-catalog \
              wayfair-catalog=${ECR_URI}:${env.BUILD_NUMBER} --record || true

            kubectl -n wayfair-dev rollout status deployment/wayfair-catalog
          """
        }
      }
    }
  }

  post {
    success { echo "Deployed ${APP_NAME} build #${env.BUILD_NUMBER}" }
    failure { echo "Build failed." }
  }
}
