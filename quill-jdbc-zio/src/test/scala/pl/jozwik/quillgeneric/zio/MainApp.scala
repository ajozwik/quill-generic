package pl.jozwik.quillgeneric.zio

import com.typesafe.scalalogging.StrictLogging
import io.getquill.H2ZioJdbcContext
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import pl.jozwik.quillgeneric.Strategy
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.repository.DateQuotes
import pl.jozwik.quillgeneric.zio.repository.ConfigurationJdbcRepository
import zio.ZIOAppDefault
import zio.interop.catz.*
object MainApp extends ZIOAppDefault with StrictLogging {

  private val strategy        = Strategy.namingStrategy
  private lazy val ctx        = new H2ZioJdbcContext(strategy) with ObjectGenericTimeDecoders with ObjectGenericTimeEncoders with DateQuotes
  private lazy val repository = new ConfigurationJdbcRepository(ctx)
  private val id              = ConfigurationId("key")

  private val appSuccess = for {
    _  <- repository.create(Configuration(id, "value"))
    a  <- repository.all
    el <- repository.readUnsafe(id)
    _  <- repository.deleteAll
  } yield {
    el
  }

  private val appFail = for {
    _  <- repository.create(Configuration(ConfigurationId("key"), "value"))
    a  <- repository.all
    el <- repository.readUnsafe(ConfigurationId("aaa"))

  } yield {
    a
  }

  def run = {
    val suc = for {
      success <- appSuccess.provideEnvironment(ZioHelperSpec.environment)
    } yield {
      logger.debug(s"Success $success ")
    }

    for {
      _ <- suc
      c <- appFail
        .provideEnvironment(ZioHelperSpec.environment)
        .mapErrorCause(e => {
          logger.error(s"$e")
          e
        })
        .exitCode
    } yield {
      logger.debug(s"$c")
    }
  }
}
