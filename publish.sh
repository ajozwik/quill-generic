#!/bin/bash

. $HOME/.sbt/.quill-generic-coverage
sbt8 -Dquill.macro.log=false clean +test +coverageAggregate && sbt8 +coverageAggregate || exit 1
PATH=$HOME/bin:$PATH sbt8 -Dquill.macro.log=false +publishLocalSigned +publishSigned +sonatypeRelease
