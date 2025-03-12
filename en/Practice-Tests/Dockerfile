FROM nginx:alpine

# Set working directory
WORKDIR /usr/share/nginx/html

# Copy website files (index.html, JavaScript, and JSON data)
COPY index.html /usr/share/nginx/html/
COPY questions.json /usr/share/nginx/html/

# Expose port 80 for HTTP traffic
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
