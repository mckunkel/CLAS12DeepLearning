#!/bin/csh -f

#No magic here, just specify your environment

source /site/env/syscshrc
module load java_1.8
use groovy
setenv PATH /volatile/clas12/groovy-2.4.10/bin/:$PATH

unset CLARA_HOME
unset COATJAVA
unset CLAS12DIR

setenv CLARA_HOME $1
setenv COATJAVA $CLARA_HOME/plugins/clas12
setenv CLAS12DIR $CLARA_HOME/plugins/clas12
