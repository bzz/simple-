package main

import (
	"flag"
	"fmt"
	"log"
	"net"

	pb "github.com/bzz/simple-extractor/src/main/go/protobuf"
	"github.com/src-d/go-git/plumbing/object"

	"google.golang.org/grpc"
)

var port = flag.Int("port", 8888, "port to bind")
var limit = flag.Uint64("limit", 0, "number of repositories, 0 = All from DB")
var profiler = flag.Int("profiler", 6063, "start CPU & memory profiler at the given port")

type extractorServer struct {
	limit uint64
	/*
		TODO(bzz): introduce abstactions
			 - db.Query()
			 - git.OpenRootedRepo()
			 - bblfsh.ParseUAST() (handles bblfshClient protocol.ProtocolServiceClient)
	*/
}

func newExtractorServer(n uint64) *extractorServer {
	//TODO(bzz): open connection to bblfsh, increase max msg size
	return &extractorServer{limit: n}
}

func (s *extractorServer) ParseEveryFileToUast(repoBatch *pb.RepositoriesBatch, stream pb.UastExtractor_ParseEveryFileToUastServer) error {
	s.startHTTPProfilerMaybe(*profiler)
	branch := "master"
	for _, repoID := range repoBatch.GetRepositoryIds { // for each repo in repoBatch
		gitTree := queryDbAndOpenSivaFile(repoID, branch)
		err = gitTree.File.ForEac(func(f *object.File) error { // for each file in repo
			// TODO(bzz)
			//     uast := bblfsh.ParseUAST(file)
			//     stream.send(uast)
		})
	}
	return nil
}

func main() {
	flag.Parse()
	lis, err := net.Listen("tcp", fmt.Sprintf(":%d", *port))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	grpcServer := grpc.NewServer()
	pb.RegisterUastExtractorServer(grpcServer, newExtractorServer(*limit))

	grpcServer.Serve(lis)
}

// Uses core-retrieval for
// - query DB on repository metadata (original ref in rootedRepo for a given branch)
// - RootTransactioner, go-billy-siva, go-siva and go-git to open .siva file
func (s *extractorServer) queryDbAndOpenSivaFile(repoID) {
	//TODO(bzz):
	// db.query(...)
	// git.OpenRootedRepo()
	//   start RootedTransactioner
	//   git.Open()
}

func (s *extractorServer) startHTTPProfilerMaybe(profilerAddr int) {
	//TODO(bzz): add profiler
}
