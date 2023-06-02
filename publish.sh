#!/bin/bash

PATH=$HOME/bin:$PATH sbt8 -Dquill.macro.log=false clean +publishLocalSigned +publishSigned +sonatypeRelease
