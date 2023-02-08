/*
 * Copyright (C) 2019-present, Chenai Nakam(chenai.nakam@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hobby.chenai.nakam.txdsl.coin

import hobby.chenai.nakam.txdsl.core.coin.AbsTokenGroup
import scala.math.BigInt.int2bigInt

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 16/11/2021
  */
class OtherToken(name: String, precision: Int = 8) extends AbsTokenGroup { outer =>
  require(precision >= 0 && precision <= 18) // eth: 10^18

  override type COIN = Tkn
  override type UNIT = COIN with Unt

  override def unitStd = uStd
  override def unitMin = uStm

  override def make(count: BigInt, unt: UNIT) = new Tkn(count) {
    override def unit = unt
  }

  abstract class Tkn private[OtherToken] (count: BigInt) extends AbsCoin(count: BigInt) {

    override def equals(obj: Any) = obj match {
      case that: Tkn => that.canEqual(this) && that.count == this.count
      case _         => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[Tkn]
  }

  private lazy val uStd: UNIT = new Tkn(10.pow(precision)) with Unt {
    override val name = outer.name
  }

  private lazy val uStm: UNIT = new Tkn(1) with Unt {
    override val name = outer.name + ".stm"
  }
}
