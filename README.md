This is equivalent of https://github.com/src-d/berserker

# Architecture
It consists of 2 parts/processes, talking though gRPC.

Focus of this implementation is
 - polyglot, a single project containing .proto, go and scala source code
 - uses `.proto` as 'source of truth'
 - uses gRPC server-side streaming per-file, to reduce memory pressure

### Apache Spark job in Scala
```
 fetches all RepositoryIDs from DB, that need to be processed
 partition IDs (#of partitions = number of parallel processes (go-git, enry, bblfsh))
 for each partition
   start Go gRPC server
   RepositoriesBatch := full partition
   client.ParseEveryFileToUAST(RepositoriesBatch)
   // all UASTs for repos in a batch will be in Task memory :/
```

### Extractor in Golang
```
 expose gRPC interface
 for given repositoryIDs
   get metadata from DB (refs)
   for each repositoryID:
     read master from .siva file (core.RootTransactioner, go-git, go-billy-siva, etc)
     for every file
       detect lang
       filter python & java
       send to bblfsh server to get UAST
       serialize it to Protobuff
```

## Run locally

0. generate Scala/Go code from .proto
  - install `protoc`
  - `go get -u github.com/gogo/protobuf/...`
  - `./codegen.sh`

1. build scala
  `./sbt build`

2. build Go
  - server
    `go build -o extractor-server ./src/main/go`
  - cli
    `go build ?`

3. run
  ```
  ./extractor-server
  ./sbt run
  ```