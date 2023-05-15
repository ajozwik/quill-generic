package pl.jozwik.quillgeneric.monad

import io.getquill.JdbcContextConfig
import io.getquill.util.LoadConfig
import pl.jozwik.quillgeneric.PoolHelper

import javax.sql.DataSource

object HelperSpec {
  private val cfg      = LoadConfig(PoolHelper.PoolName)
  private val jdbcCfg  = JdbcContextConfig(cfg)
  val pool: DataSource = jdbcCfg.dataSource
}
