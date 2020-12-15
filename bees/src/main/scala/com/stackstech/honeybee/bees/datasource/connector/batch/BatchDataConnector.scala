
package com.stackstech.honeybee.bees.datasource.connector.batch

import com.stackstech.honeybee.bees.datasource.connector.DataConnector

trait BatchDataConnector extends DataConnector {

  def init(): Unit = {}

}
