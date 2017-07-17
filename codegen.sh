#!/usr/bin/env bash


# Dependencies:
#  i) Google proto buffer compiler at https://github.com/google/protobuf
#  brew install protobuff 
#  ii) the Go codegen plugin at https://github.com/golang/protobuf
#  go get -u github.com/golang/protobuf/protoc-gen-go

proto="./src/main/protobuf/*.proto"
if [[ -n $1 ]]; then
  proto=$1
fi

protoc -I='./src/main' --go_out=plugins=grpc:'./src/main/go' $proto
# spbc my.proto --scala_out=.
./sbt protoc-generate
