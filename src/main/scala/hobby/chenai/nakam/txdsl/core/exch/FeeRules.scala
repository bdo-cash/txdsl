/*
 * Copyright (C) 2021-present, Chenai Nakam(chenai.nakam@gmail.com)
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

package hobby.chenai.nakam.txdsl.core.exch

import hobby.chenai.nakam.lang.TypeBring.AsIs
import hobby.chenai.nakam.tool.cache.{Delegate, LazyGet, Lru, Memoize}
import hobby.chenai.nakam.txdsl.core.coin.{Coin, Token}

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 16/01/2021
  */
final case class FeeRule[F <: Fee[_ <: Coin, _ <: Coin]](taker: F#Rule, maker: F#Rule)

abstract class FeeRulesMemoize(size: Int = 23) extends Memoize[(Token, Coin, Boolean), FeeRule[Fee[Coin, Coin]]] with LazyGet with Lru {
  /** @param txArgs `_1/_2`: txPair, `_3`: isBuyOrSell, 用于标识`Fee`的参数化类型。 */
  protected def loadFeeRule(txArgs: (Token, Coin, Boolean)): FeeRule[Fee[Coin, Coin]]

  override protected val maxCacheSize = size

  override protected val delegate = new Delegate[(Token, Coin, Boolean), FeeRule[Fee[Coin, Coin]]] {
    override def load(txArgs: (Token, Coin, Boolean))                                    = Option(loadFeeRule(txArgs))
    override def update(txArgs: (Token, Coin, Boolean), value: FeeRule[Fee[Coin, Coin]]) = Option(value)
  }

  final def rule(txPair: (Token, Coin), isBuyOrSell: Boolean): FeeRule[Fee[Coin, Coin]] = get(txPair._1, txPair._2, isBuyOrSell).get.as
}
