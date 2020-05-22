#!/bin/sh


FAILURE=1

SUCCESS=0


UNAME_S=`uname -s`

if test "${UNAME_S}" = 'Darwin'
then
    #   Supposed not to work.
    # JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.7.jdk/Contents/Home/ java -cp target/pangolin-0.0.1-SNAPSHOT.jar org.minia.pangolin.App

    #   Would work.
    JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.7.jdk/Contents/Home/ java -jar target/pangolin-0.0.1-SNAPSHOT-jar-with-dependencies.jar

    #   Would work as well.
    # JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.7.jdk/Contents/Home/ java -cp target/pangolin-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.minia.pangolin.App
else
    echo 'unknown operating system'
    exit ${FAILURE}
fi

exit ${SUCCESS}
