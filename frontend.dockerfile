# BUILD STAGE
FROM node:20-alpine AS builder

WORKDIR /app

COPY frontend/ ./

RUN npm install pnpm -g

RUN pnpm install
RUN pnpm build

# RUNTIME STAGE
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

COPY nginx.conf.template /etc/nginx/nginx.conf.template
COPY front-end.docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 80

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]