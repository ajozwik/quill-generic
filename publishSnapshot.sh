#!/bin/bash

PATH=$HOME/bin:$PATH sbt clean test publishSigned
