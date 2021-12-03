/*
 * Copyright (C) 2017-present, Chenai Nakam(chenai.nakam@gmail.com)
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
import hobby.chenai.nakam.txdsl.core.coin._
import java.util.concurrent.ConcurrentHashMap
import scala.language.postfixOps

/**
  * 没有手续费的[理想状态]下的交易所。交易手续费[[Fee]]等其它组件可以依托本组件创建符合实际情况的交易所。<br>
  *
  * @param name         交易所名称。
  * @param pricingToken 定价`token`。必填，但只要不[[updateTokenPricingRate]]就不会用它来计算定价。
  * @param pricingCash  定价法币（默认为支持所以[[tokens]]）。
  * @param tokens       本平台支持的除[[pricingToken]]和[[pricingCash]]之外的所有币种。
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 30/05/2017
  */
abstract class AbsExchange(val name: String, override val pricingToken: Token, override val pricingCash: Cash, tokens: Token*)
    extends CoinEx(pricingToken, pricingCash) with Memoize[(Token, Coin), FixedFracDigitsRule] with LazyGet with Lru {
  require(!tokens.contains(pricingToken))

  val supportTokens = pricingToken :: tokens.toList.distinct
  private val zero  = pricingCash.unitStd * 0
  supportTokens.foreach(cashPriRateMap.put(_, zero))
  // tokenPriRateMap（即token定价）是可选的，因此不put into Map.
  // supportTokens.foreach(tokenPriRateMap.put(_, zero))

  /** 不同交易所的规则不同。所以需要重写。 */
  protected def loadFfdRule(counterParty: (Token, Coin)): FixedFracDigitsRule

  override protected val maxCacheSize = 50

  override protected val delegate = new Delegate[(Token, Coin), FixedFracDigitsRule] {
    override def load(key: (Token, Coin))                               = Option(loadFfdRule(key))
    override def update(key: (Token, Coin), value: FixedFracDigitsRule) = Option(value)
  }

  override final def getFfdRule[T <: Token, C <: Coin](token: T, pricingCoin: C): FixedFracDigitsRule = get(token, pricingCoin).get.as[FixedFracDigitsRule]

  // 没有比特币汇率则必须有法币汇率。
  /** 从加密货币到法币的汇率，而法币在一个交易所只有一种。 */
  private final lazy val cashPriRateMap = new ConcurrentHashMap[Token, PriCCoin]
  /** 从加密货币到比特币的汇率。 */
  private final lazy val tokenPriRateMap = new ConcurrentHashMap[Token, PriTCoin]

  // 重构，避免歧义。因为`cashPriRateMap`把所有`supportTokens`都初始化为`0`了，但`tokenPriRateMap`并没有初始化。
  override final def isCashExSupport(token: Token)  = cashPriRateMap.containsKey(token) && cashPriRateMap.get(token).value > ZERO
  override final def isTokenExSupport(token: Token) = tokenPriRateMap.containsKey(token) && tokenPriRateMap.get(token).value > ZERO

  protected final lazy val impl = coinTpeImpl[PriTCoin, PriCCoin]

  /** @param token 必须包含在[[tokens]]里或是[[pricingToken]]；
    * @param rate 必须是[[pricingCash]]。
    */
  final def updateCashPricingRate(token: TokenUnt, rate: CashAmt): Unit = {
    import impl._
    cashPriRateMap.put(requireSupports(token, false), rate)
  }

  /** @param token 必须包含在[[tokens]]里且[[pricingToken]]除外；
    * @param rate 必须是[[pricingToken]]。
    */
  final def updateTokenPricingRate(token: TokenUnt, rate: TokenAmt): Unit = {
    import impl._
    tokenPriRateMap.put(requireSupports(token, true), rate)
  }

  private def requireSupports(token: TokenUnt, token$cash: Boolean): Token = {
    require(token == token.std, s"parameter `$token` should be `${token.std}`.")
    require(isTokenSupport(token, token$cash), s"parameter `$token` is is not supported. supportTokens: $supportTokenStr.")
    token.group
  }

  /** @tparam CG  [[PriTCoin]]#GROP for `token$cash = true`, [[PriCCoin]]#GROP for `token$cash = false`. */
  override final def getExRate[CG <: Coin](group: Token, token$cash: Boolean, ignoreZero: Boolean = false) = {
    // `group != pricingCash`恒成立（类型都不同），但`pricingToken`本身也是可以的。
    val rate = if (group == pricingToken) pricingToken.unitStd * ONE else if (token$cash) tokenPriRateMap.get(group) else cashPriRateMap.get(group)
    require(
      rate != null && (ignoreZero || rate.value > ZERO),
      if (isTokenSupport(group.unitStd)) s"rate of `${group.unitStd}/${if (token$cash) pricingToken.unitStd else pricingCash.unitStd}` have not initialized on `$name`."
      else s"`$group` is not supported."
    )
    rate.as[CG#COIN]
  }

  override def toString = s"`$name`(priCash: ${pricingCash.unitStd} | priTkn: ${pricingToken.unitStd})$supportTokenStr"

  def supportTokenStr = supportTokens.map(_.unitStd).mkString("[", ", ", "]")
}

abstract class CoinEx(val pricingToken: Token, val pricingCash: Cash) {
  type PriTCoin = pricingToken.COIN
  type PriCCoin = pricingCash.COIN

  protected val supportTokens: Seq[Coin]

  protected def getFfdRule[T <: Token, C <: Coin](token: T, pricingCoin: C): FixedFracDigitsRule

  protected def isTokenExSupport(token: Token): Boolean
  protected def isCashExSupport(token: Token): Boolean

  protected def getExRate[CG <: Coin](tokenGroup: Token, token$cash: Boolean, ignoreZero: Boolean = false): CG#COIN

  /** `pricingToken`是包含在`supportTokens`里面的。 */
  final def isCoinSupport(coin: CoinAmt, pricingOnly: Boolean = false): Boolean = coin.group == pricingCash || (if (pricingOnly) coin.group == pricingToken else isTokenSupport(coin, false))
  final def isTokenSupport(coin: CoinAmt, tokensOnly: Boolean = true): Boolean  = supportTokens.exists { t => if (tokensOnly && t == pricingToken) false else t == coin.group }

  final def applyExch(src: CoinAmt, dst: CoinUnt, ceiling: Boolean): CoinAmt = {
    if (isCoinSupport(src) && isCoinSupport(dst)) {
      ex.applyOrElse((src, dst, ceiling, true), (x: (CoinAmt, _, _, _)) => x._1)
    } else src
  }

  /** 有`pricingToken`定价则优先 token, 否则强制`pricingCash`定价（若没有则报错）。 */
  private final lazy val ex: (CoinAmt, CoinAmt, Boolean, Boolean) PartialFunction CoinAmt = {
    // pricingCash => pricingCash // 到这里不会出现两个不一样的法币。
    case (cash: CashAmt, dst: CashUnt, _, _) => dst.unit << cash
    // token => pricingCash
    case (token: TokenAmt, dst: CashUnt, ceiling, promise) =>
      if (promise /*注意这个promise不能把任务再转给pricingToken，不然会死递归*/ || isCashExSupport(token.group)) {
        val ffdRule = getFfdRule(token.group, dst.group)
        import ffdRule._
        import ffdRule.impl._
        dst.unit << sell(token, getExRate(token.group, token$cash = false), ceiling)
      } else token
    // pricingCash => token
    case (cash: CashAmt, dst: TokenUnt, ceiling, promise) =>
      if (promise || isCashExSupport(dst.group)) {
        val ffdRule = getFfdRule(dst.group, cash.group)
        import ffdRule._
        import ffdRule.impl._
        dst.unit << buy(cash /*注意这里不是dst, cash表示有多少钱*/, getExRate(dst.group, token$cash = false), ceiling)
      } else cash
    // pricingToken => pricingToken // 与token不同的是，cash（在一个本对象中）就一种，不需要判断。
    case (token: TokenAmt, dst: TokenUnt, _, _) if (token.group eq pricingToken) && (dst.group eq pricingToken) =>
      dst.unit << token
    // token => pricingToken
    case (token: TokenAmt, dst: TokenUnt, ceiling, promise) if dst.group eq pricingToken =>
      if (isTokenExSupport(token.group)) {
        val ffdRule = getFfdRule(token.group, dst.group)
        import ffdRule._
        import ffdRule.impl._
        dst.unit << sell(token, getExRate(token.group, token$cash = true), ceiling)
      } else if (promise) ex.apply(ex.apply(token, pricingCash.unitStd, ceiling, promise), dst, ceiling, promise)
      else token
    // pricingToken => token
    case (token: TokenAmt, dst: TokenUnt, ceiling, promise) if token.group eq pricingToken =>
      if (isTokenExSupport(dst.group)) {
        val ffdRule = getFfdRule(dst.group, token.group)
        import ffdRule._
        import ffdRule.impl._
        dst.unit << buy(token /*注意这里不是dst, token表示有多少钱*/, getExRate(dst.group, token$cash = true), ceiling)
      } else if (promise) ex.apply(ex.apply(token, pricingCash.unitStd, ceiling, promise), dst, ceiling, promise)
      else token
    // token => token
    case (src: TokenAmt, dst: TokenUnt, ceiling, promise) =>
      if (dst.group == src.group) dst.unit << src
      else {
        val ptf = isTokenExSupport(src.group)
        val ptt = isTokenExSupport(dst.group)
        val cf  = isCashExSupport(src.group)
        val ct  = isCashExSupport(dst.group)
        if (ptf && ptt) ex.apply(ex.apply(src, pricingToken.unitStd, ceiling, false), dst, ceiling, false)
        else if (cf && ct) ex.apply(ex.apply(src, pricingCash.unitStd, ceiling, false), dst, ceiling, false)
        else if (promise) {
          if (ptf || ptt) ex.apply(ex.apply(src, pricingToken.unitStd, ceiling, true), dst, ceiling, true)
          else ex.apply(ex.apply(src, pricingCash.unitStd, ceiling, true), dst, ceiling, true)
        } else src
      }
  }
}
