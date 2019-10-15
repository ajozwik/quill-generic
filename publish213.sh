#!/bin/bash

PATH=$HOME/bin:$PATH sbt -Dquill.macro.log=false -Dmacro.scala.version=2.13 clean test publishLocal publishSigned sonatypeRelease
