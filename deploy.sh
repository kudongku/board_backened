# env
APP_NAME="board-back"
IMAGE_NAME="kudongku/board-back-image"
IMAGE_TAG="latest"
DEPLOYMENT_NAME="board-back"
SERVICE_NAME="board-service"
PORT="8080"

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