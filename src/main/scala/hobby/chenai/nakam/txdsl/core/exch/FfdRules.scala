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
  * @version 1.0, 24/12/2021
  */
abstract class FfdRules(size: Int = 23) extends Memoize[(Token, Coin), FixedFracDigitsRule] with LazyGet with Lru {

  /** 不同交易所的规则不同。所以需要重写。 */
  protected def loadFfdRule(txPair: (Token, Coin)): FixedFracDigitsRule

  override protected val maxCacheSize = size

  override protected val delegate = new Delegate[(Token, Coin), FixedFracDigitsRule] {
    override def load(key: (Token, Coin))                               = Option(loadFfdRule(key))
    override def update(key: (Token, Coin), value: FixedFracDigitsRule) = Option(value)
  }

  final def rule(token: Token, pricing: Coin): FixedFracDigitsRule = get(token, pricing).get.as
}
