FROM tomcat:9-jdk21

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/online-voting-app.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 7777

CMD ["catalina.sh", "run"]