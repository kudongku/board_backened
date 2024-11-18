## Kubernetes

### update kubernetes 에 반영하기
```bash
./gradlew bootJar # jar 파일 빌드하기
docker build -t kudongku/board-back-image . # docker image 로 빌드하기
docker push kudongku/board-back-image:latest # docker hub 에 push 하기
kubectl set image deployment/board-back board-back=kudongku/board-back-image:latest # 어플리케이션 새 버전의 이미지 적용
kubectl port-forward service/board-service 8080:8080 # port-forward 로 로컬에서 확인
```