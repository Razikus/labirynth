FROM frekele/ant:1.10.1-jdk8

ENV LABSERVERIP=localhost
ENV LABSERVERPORT=8989

VOLUME /compiled

ADD LabyrinthGameClient /sources

ADD entrypoint.sh /

CMD ["/bin/bash", "/entrypoint.sh"]
