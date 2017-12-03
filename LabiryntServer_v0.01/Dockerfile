FROM frekele/ant:1.10.1-jdk8

ADD LabiryntServer /sources
RUN cd /sources && sh BUILD.sh
EXPOSE 8989

ADD entrypoint.sh /

CMD ["/bin/bash", "/entrypoint.sh"]

