package org.bzz.extractor.protobuf

object UastExtractorGrpc {
  val METHOD_PARSE_EVERY_FILE_TO_UAST: _root_.io.grpc.MethodDescriptor[org.bzz.extractor.protobuf.RepositoriesBatch, org.bzz.extractor.protobuf.FileUast] =
    _root_.io.grpc.MethodDescriptor.create(
      _root_.io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
      _root_.io.grpc.MethodDescriptor.generateFullMethodName("org.bzz.extractor.protobuf.UastExtractor", "ParseEveryFileToUast"),
      new com.trueaccord.scalapb.grpc.Marshaller(org.bzz.extractor.protobuf.RepositoriesBatch),
      new com.trueaccord.scalapb.grpc.Marshaller(org.bzz.extractor.protobuf.FileUast))
  
  trait UastExtractor extends _root_.com.trueaccord.scalapb.grpc.AbstractService {
    override def serviceCompanion = UastExtractor
    def parseEveryFileToUast(request: org.bzz.extractor.protobuf.RepositoriesBatch, responseObserver: _root_.io.grpc.stub.StreamObserver[org.bzz.extractor.protobuf.FileUast]): Unit
  }
  
  object UastExtractor extends _root_.com.trueaccord.scalapb.grpc.ServiceCompanion[UastExtractor] {
    implicit def serviceCompanion: _root_.com.trueaccord.scalapb.grpc.ServiceCompanion[UastExtractor] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = org.bzz.extractor.protobuf.ExtractorProto.javaDescriptor.getServices().get(0)
  }
  
  trait UastExtractorBlockingClient {
    def serviceCompanion = UastExtractor
    def parseEveryFileToUast(request: org.bzz.extractor.protobuf.RepositoriesBatch): scala.collection.Iterator[org.bzz.extractor.protobuf.FileUast]
  }
  
  class UastExtractorBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[UastExtractorBlockingStub](channel, options) with UastExtractorBlockingClient {
    override def parseEveryFileToUast(request: org.bzz.extractor.protobuf.RepositoriesBatch): scala.collection.Iterator[org.bzz.extractor.protobuf.FileUast] = {
      scala.collection.JavaConverters.asScalaIteratorConverter(_root_.io.grpc.stub.ClientCalls.blockingServerStreamingCall(channel.newCall(METHOD_PARSE_EVERY_FILE_TO_UAST, options), request)).asScala
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): UastExtractorBlockingStub = new UastExtractorBlockingStub(channel, options)
  }
  
  class UastExtractorStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[UastExtractorStub](channel, options) with UastExtractor {
    override def parseEveryFileToUast(request: org.bzz.extractor.protobuf.RepositoriesBatch, responseObserver: _root_.io.grpc.stub.StreamObserver[org.bzz.extractor.protobuf.FileUast]): Unit = {
      _root_.io.grpc.stub.ClientCalls.asyncServerStreamingCall(channel.newCall(METHOD_PARSE_EVERY_FILE_TO_UAST, options), request, responseObserver)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): UastExtractorStub = new UastExtractorStub(channel, options)
  }
  
  def bindService(serviceImpl: UastExtractor, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
    _root_.io.grpc.ServerServiceDefinition.builder("org.bzz.extractor.protobuf.UastExtractor")
    .addMethod(
      METHOD_PARSE_EVERY_FILE_TO_UAST,
      _root_.io.grpc.stub.ServerCalls.asyncServerStreamingCall(new _root_.io.grpc.stub.ServerCalls.ServerStreamingMethod[org.bzz.extractor.protobuf.RepositoriesBatch, org.bzz.extractor.protobuf.FileUast] {
        override def invoke(request: org.bzz.extractor.protobuf.RepositoriesBatch, observer: _root_.io.grpc.stub.StreamObserver[org.bzz.extractor.protobuf.FileUast]): Unit =
          serviceImpl.parseEveryFileToUast(request, observer)
      }))
    .build()
  
  def blockingStub(channel: _root_.io.grpc.Channel): UastExtractorBlockingStub = new UastExtractorBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): UastExtractorStub = new UastExtractorStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = org.bzz.extractor.protobuf.ExtractorProto.javaDescriptor.getServices().get(0)
  
}