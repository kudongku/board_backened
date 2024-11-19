#!/bin/bash

# env
APP_NAME="board-back"
IMAGE_NAME="kudongku/board-back-image"
IMAGE_TAG="latest"
DEPLOYMENT_NAME="board-back"
SERVICE_NAME="board-service"
PORT="8080"

function print_help() {
    echo "Usage: $0 [option]"
    echo "Options:"
    echo "  init     - create git pre-commit hook"
    echo "  deploy   - apply deployment to kubernetes"
    echo "  update   - apply updates to kubernetes"
}

function init() {
    HOOKS_DIR=".git/hooks"
    PRE_COMMIT_HOOK="${HOOKS_DIR}/pre-commit"

    echo "1. check git init..."
        if [ ! -d "$HOOKS_DIR" ]; then
            echo "Error: no git directory. need git init."
            exit 1
        fi

        cat <<EOT > $PRE_COMMIT_HOOK
#!/bin/bash

echo "Deploy script is running before commit..."

# deploy.sh 스크립트 실행
./deploy.sh update-with-commit || { echo "Deploy script failed! Commit aborted."; exit 1; }

echo "Deploy script completed successfully."
EOT

        chmod +x $PRE_COMMIT_HOOK
        echo "pre-commit Hook 설정 완료."
}

function deploy() {
    echo "1. apply deployment"
    kubectl apply -f back_deployment.yaml || { echo "apply deployment failed!"; exit 1; }

    echo "2. port forwarding"
    kubectl port-forward service/${SERVICE_NAME} ${PORT}:${PORT} || { echo "port forwarding failed!"; exit 1; }
}

function update() {
    # build jar
    echo "1. build jar..."
    ./gradlew bootJar || { echo "build jar failed!"; exit 1; }

    # build docker image
    echo "2. build docker image..."
    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} . || { echo "build docker image failed!"; exit 1; }

    # push docker image to hub
    echo "3. push docker image to hub..."
    docker push ${IMAGE_NAME}:${IMAGE_TAG} || { echo "push docker image to hub failed!"; exit 1; }

    # deploy new image to kubernetes
    echo "4. deploy new image to kubernetes..."
    kubectl set image deployment/${DEPLOYMENT_NAME} ${APP_NAME}=${IMAGE_NAME}:${IMAGE_TAG} || { echo "deploy new image to kubernetes failed!"; exit 1; }

    # check roll out status
    echo "5. check roll out status..."
    kubectl rollout status deployment/${DEPLOYMENT_NAME} || { echo "check roll out status failed!"; exit 1; }

    # port forwarding
    echo "finish. port-forwarding ${PORT}..."
    kubectl port-forward service/${SERVICE_NAME} ${PORT}:${PORT} || { echo "port-forwarding failed!"; exit 1; }
}

function update_without_port_forwarding(){
      # build jar
      echo "1. build jar..."
      ./gradlew bootJar || { echo "build jar failed!"; exit 1; }

      # build docker image
      echo "2. build docker image..."
      docker build -t ${IMAGE_NAME}:${IMAGE_TAG} . || { echo "build docker image failed!"; exit 1; }

      # push docker image to hub
      echo "3. push docker image to hub..."
      docker push ${IMAGE_NAME}:${IMAGE_TAG} || { echo "push docker image to hub failed!"; exit 1; }

      # deploy new image to kubernetes
      echo "4. deploy new image to kubernetes..."
      kubectl set image deployment/${DEPLOYMENT_NAME} ${APP_NAME}=${IMAGE_NAME}:${IMAGE_TAG} || { echo "deploy new image to kubernetes failed!"; exit 1; }

      # check roll out status
      echo "5. check roll out status..."
      kubectl rollout status deployment/${DEPLOYMENT_NAME} || { echo "check roll out status failed!"; exit 1; }
}

# 명령어 처리
case "$1" in
    init)
      init
      ;;
    update)
      update
      ;;
    update-with-commit)
      update_without_port_forwarding
      ;;
    deploy)
      deploy
      ;;
    help | *)
      print_help
      ;;
esac
