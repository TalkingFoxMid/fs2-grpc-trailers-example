package ru.talkingfox.grpcO

import cats.effect.kernel.Sync
import cats.effect.{IO, IOApp}
import com.google.protobuf.any.Any
import com.google.rpc.Status
import fs2.grpc.syntax.all.fs2GrpcSyntaxServerBuilder
import io.grpc.Metadata
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.lite.ProtoLiteUtils
import io.grpc.protobuf.services.ProtoReflectionService
import ru.talkingfox.grpcO.helloworld.{HelloWorldServiceFs2GrpcTrailers, Request, Response}

object Fs2Main  extends IOApp.Simple {
  class MyImpl[F[_]: Sync] extends HelloWorldServiceFs2GrpcTrailers[F, Metadata] {
    override def get(request: Request, ctx: Metadata): F[(Response, Metadata)] = {
      val me = new Metadata()
      me.put(
        Metadata.Key.of(
          "grpc-status-details-bin",
          ProtoLiteUtils.metadataMarshaller(com.google.rpc.Status.getDefaultInstance)
        ),
        Status.newBuilder()
          .setCode(0)
          .addDetails(
            Any.toJavaProto(
              Any.pack(
                Request("OKA")
              )
            )
          ).build()
      )
      Sync[F].delay(
        (Response(request.query.reverse), me)
      )
    }
  }
  override def run: IO[Unit] = {
    for {
      descriptor <- HelloWorldServiceFs2GrpcTrailers.bindServiceResource(
        new MyImpl[IO]
      )
      _ <- NettyServerBuilder
        .forPort(9999)
        .addService(descriptor)
        .addService(ProtoReflectionService.newInstance())
        .resource[IO]
        .evalMap(s => IO(s.start()))
    } yield ()
  }.useForever
}
