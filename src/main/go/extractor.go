package main

import (
	"flag"
	"log"
	"net"
  "fmt"

	pb "main/go/protobuf"

	"google.golang.org/grpc"
)

var port = flag.Int("port", 8888, "port to bind")
var limit = flag.Uint64("limit", 0, "number of repositories, 0 = All from DB")
var profiler = flag.Int("profiler", 6063, "start CPU & memeory profiler")

type extractorServer struct {
  limit uint64
}

func (s *extractorServer) ParseEveryFileToUast(repoBatch *pb.RepositoriesBatch, stream pb.UastExtractor_ParseEveryFileToUastServer) error {
  //for each repoo in batch
  //  query DB
  //  for each file in repo
  //     bblfsh.ParseUAST(file)
  //     stream.send(UAST)
  return nil
}

func main() {
	flag.Parse()
	lis, err := net.Listen("tcp", fmt.Sprintf(":%d", *port))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	grpcServer := grpc.NewServer()
	pb.RegisterUastExtractorServer(grpcServer, &extractorServer{*limit})

	grpcServer.Serve(lis)
}
