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

/** `币安`交易所的平台币。
  *
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 16/11/2021
  */
object BnbToken extends AbsTokenGroup {
  override type COIN = Bnb
  override type UNIT = COIN with Unt

  override def unitStd = BNB

  override def make(count: BigInt, unt: UNIT) = new Bnb(count) {
    override def unit = unt
  }

  abstract class Bnb private[BnbToken] (count: BigInt) extends AbsCoin(count: BigInt) {

    override def equals(obj: Any) = obj match {
      case that: Bnb => that.canEqual(this) && that.count == this.count
      case _         => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[Bnb]
  }

  lazy val BNB: UNIT = new Bnb(10.pow(8)) with Unt {
    override val name = "BNB"
  }

  class DslImpl(count: BigDecimal) {
    @inline def BNB: COIN = BnbToken.BNB * count
  }

  @inline implicit def wrapBnbNum(count: Double): DslImpl     = new DslImpl(count)
  @inline implicit def wrapBnbNum(count: BigDecimal): DslImpl = new DslImpl(count)
}
