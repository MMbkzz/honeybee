
package com.stackstech.honeybee.bees.configuration.dqdefinition

import com.fasterxml.jackson.annotation.{JsonInclude, JsonProperty}
import com.fasterxml.jackson.annotation.JsonInclude.Include

/**
 * full set of griffin configuration
 * @param envConfig   environment configuration (must)
 * @param dqConfig    dq measurement configuration (must)
 */
@JsonInclude(Include.NON_NULL)
case class GriffinConfig(
    @JsonProperty("env") private val envConfig: EnvConfig,
    @JsonProperty("dq") private val dqConfig: DQConfig)
    extends Param {
  def getEnvConfig: EnvConfig = envConfig
  def getDqConfig: DQConfig = dqConfig

  def validate(): Unit = {
    assert(envConfig != null, "environment config should not be null")
    assert(dqConfig != null, "dq config should not be null")
    envConfig.validate()
    dqConfig.validate()
  }
}
