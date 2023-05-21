#!/bin/bash

. $HOME/.sbt/.quill-generic-coverage
sbt8 -Dquill.macro.log=false clean +coverage +test +coverageAggregate && sbt8 +coveralls || exit 1
PATH=$HOME/bin:$PATH sbt8 -Dquill.macro.log=false clean +publishLocalSigned +publishSigned +sonatypeRelease
