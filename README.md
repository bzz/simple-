
Scala
 fetches all RepositoryIDs from DB, that need to be processed
 partition IDs (#of partitions = number of parallel processes (go-git, enry, bblfsh))
 for each partition
   start Go gRPC server
   RepositoriesBatch := full partition
   client.ParseEveryFileToUAST(RepositoriesBatch)
   // all UASTs for repos in a batch will be in Task memory :/

Go
 expose gRPC interface
 for given repositoryID
   get details from DB (refs)
   read master from .siva file (core.RootTransactioner, go-git, go-billy-siva, etc)
   for every file
     detect lang
     filter python & java
     send to bblfsh server to get UAST
     serialize it to Protobuff
     return

## Run localy

0. codegen.sh - generate Scala/Go code from .proto
  protoc --go_out=plugins=grpc:. route_guide.proto
  sbt something? depends on ScalaPB

1. build scala
  sbt build

2. build Go
   `export GOPATH=$(pwd):$GOPATH`

  - server
    go build ./src/main/go/...

  - cli
    go build ...

3. run
  ./extractor-server
  ./sbt run