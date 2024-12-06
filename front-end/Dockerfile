FROM node:20.14.0-alpine as build
WORKDIR /tmp

ENV NODE_ENV=testing
ENV NODE_OPTIONS=--max-old-space-size=12288
COPY . /tmp/
RUN npm install --frozen-lockfile
RUN npm run build

FROM nginx:alpine
COPY --from=build /tmp/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]
