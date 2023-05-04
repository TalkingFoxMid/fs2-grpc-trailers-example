package ru.talkingfox.grpcO

import cats.effect.implicits.effectResourceOps
import cats.effect.{IO, IOApp, Resource}
import io.grpc.{ManagedChannel, Metadata}
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import fs2.grpc.syntax.all.{fs2GrpcSyntaxManagedChannelBuilder, fs2GrpcSyntaxServerBuilder}
import ru.talkingfox.grpcO.helloworld.{HelloWorldServiceFs2Grpc, HelloWorldServiceFs2GrpcTrailers, Request}

object Fs2Client extends IOApp.Simple {
  val managedChannelResource: Resource[IO, ManagedChannel] =
    NettyChannelBuilder
      .forAddress("127.0.0.1", 9999)
      .usePlaintext()
      .resource[IO]

  override def run: IO[Unit] = {
    for {
      stub <- managedChannelResource
        .flatMap(ch => HelloWorldServiceFs2GrpcTrailers.stubResource[IO](ch))
      response <- stub.get(Request("123"), new Metadata()).toResource
      _ = println(response)
    } yield ()
  }.useForever

}
