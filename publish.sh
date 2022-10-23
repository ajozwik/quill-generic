#!/bin/bash

PATH=$HOME/bin:$PATH sbt8 -Dquill.macro.log=false clean +test +publishLocalSigned +publishSigned +sonatypeRelease
