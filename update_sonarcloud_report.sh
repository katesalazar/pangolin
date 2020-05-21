#!/bin/sh


FAILURE=1

SUCCESS=0


UNAME_S=`uname -s`

if test "${UNAME_S}" = 'Darwin'
then
    JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.7.jdk/Contents/Home/ /Users/$USER/Desktop/dat/not_backed_up/3rd_party/apache-maven-3.6.3/bin/mvn verify sonar:sonar
else
    echo 'unknown operating system'
    exit ${FAILURE}
fi

exit ${SUCCESS}
