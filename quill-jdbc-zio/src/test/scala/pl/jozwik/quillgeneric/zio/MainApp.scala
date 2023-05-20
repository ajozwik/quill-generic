package pl.jozwik.quillgeneric.zio

import com.typesafe.scalalogging.StrictLogging
import io.getquill.H2ZioJdbcContext
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import pl.jozwik.quillgeneric.Strategy
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.repository.DateQuotes
import pl.jozwik.quillgeneric.zio.repository.ConfigurationJdbcRepository
import zio.Console.printLine
import zio.ZIOAppDefault
import zio.interop.catz.*
object MainApp extends ZIOAppDefault with StrictLogging {

  private val strategy        = Strategy.namingStrategy
  private lazy val ctx        = new H2ZioJdbcContext(strategy) with ObjectGenericTimeDecoders with ObjectGenericTimeEncoders with DateQuotes
  private lazy val repository = new ConfigurationJdbcRepository(ctx)

  private val app = for {
    _ <- repository.create(Configuration(ConfigurationId("key"), "value"))
    a <- repository.all
  } yield {
    a
  }

  def run = {
    logger.debug("Start")
    for {
      s <- app.tap(result => printLine(result.toString)).provideEnvironment(ZioHelperSpec.environment).exitCode
    } yield {
      logger.debug(s"Stop $s")
    }

  }
}
