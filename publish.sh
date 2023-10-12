#!/bin/bash

PATH=$HOME/bin:$PATH sbt -Dquill.macro.log=false clean +publishLocalSigned +publishSigned +sonatypeRelease
