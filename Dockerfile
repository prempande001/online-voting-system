FROM tomcat:9-jdk21

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/online-voting-app.war /usr/local/tomcat/webapps/ROOT.war

RUN groupadd -r tomcat && useradd -r -g tomcat -d /usr/local/tomcat -s /sbin/nologin tomcat \
    && chown -R tomcat:tomcat /usr/local/tomcat

USER tomcat

EXPOSE 7777

CMD ["catalina.sh", "run"]