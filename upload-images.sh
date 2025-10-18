docker build -t kicklob/nexo-erp-backend:latest -f backend.dockerfile .
docker push kicklob/nexo-erp-backend

docker build -t kicklob/nexo-erp-frontend:latest -f frontend.dockerfile .
docker push kicklob/nexo-erp-frontend

docker build -t kicklob/nexo-erp-portal:latest -f portal.dockerfile .
docker push kicklob/nexo-erp-portal