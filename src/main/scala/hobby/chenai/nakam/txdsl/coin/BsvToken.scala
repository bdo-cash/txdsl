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

import scala.language.implicitConversions
import scala.math.BigInt.int2bigInt

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 30/08/2020
  */
object BsvToken extends AbsTokenGroup {
  override type COIN = BitcoinSatoshiVersion
  override type UNIT = COIN with Unt

  override def unitStd = BSV

  override def make(count: BigInt, unt: UNIT) = new BitcoinSatoshiVersion(count) {
    override def unit = unt
  }

  abstract class BitcoinSatoshiVersion private[BsvToken] (count: BigInt) extends AbsCoin(count: BigInt) {

    override def equals(obj: Any) = obj match {
      case that: BitcoinSatoshiVersion => that.canEqual(this) && that.count == this.count
      case _                           => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[BitcoinSatoshiVersion]
  }

  lazy val BSV: UNIT = new BitcoinSatoshiVersion(10.pow(8)) with Unt {
    override val name = "BSV"
  }

  class DslImpl(count: BigDecimal) {
    @inline def BSV: COIN = BsvToken.BSV * count
  }

  @inline implicit def wrapBsvNum(count: Double): DslImpl     = new DslImpl(count)
  @inline implicit def wrapBsvNum(count: BigDecimal): DslImpl = new DslImpl(count)
}
