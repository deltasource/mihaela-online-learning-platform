FROM node:23-alpine

WORKDIR /app

COPY . .

EXPOSE 5173

ENTRYPOINT ["npm", "run", "dev", "--", "--port=5173", "--host=0.0.0.0"]
