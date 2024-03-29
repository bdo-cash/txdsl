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
  * @version 1.0, 29/05/2017
  */
object BtcToken extends AbsTokenGroup {
  override type COIN = Bitcoin
  override type UNIT = COIN with Unt

  override def unitStd = BTC
  override def unitMin = SAT

  override def make(count: BigInt, unt: UNIT) = new Bitcoin(count) {
    override def unit = unt
  }

  abstract class Bitcoin private[BtcToken] (count: BigInt) extends AbsCoin(count: BigInt) {

    override def equals(obj: Any) = obj match {
      case that: Bitcoin => that.canEqual(this) && that.count == this.count
      case _             => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[Bitcoin]
  }

  // 既是单位数据也是枚举
  lazy val SAT: UNIT = new Bitcoin(1) with Unt {
    override val name = "SAT"
  }

  lazy val BTC: UNIT = new Bitcoin(10.pow(8)) with Unt { // 一亿聪
    override val name = "BTC"
  }

  class DslImpl(count: BigDecimal) {
    @inline def SAT: COIN = BtcToken.SAT * count
    @inline def BTC: COIN = BtcToken.BTC * count
  }

  class DslImplInt(count: Int) {
    @inline def SAT: COIN = BtcToken.SAT * count
    @inline def BTC: COIN = BtcToken.BTC * count
  }

  // 不可以写在父类里，否则对于多个不同的币种就不知道转换给谁了。
  @inline implicit def wrapBtcNum(count: Int): DslImplInt     = new DslImplInt(count)
  @inline implicit def wrapBtcNum(count: Double): DslImpl     = new DslImpl(count)
  @inline implicit def wrapBtcNum(count: BigDecimal): DslImpl = new DslImpl(count)
}
