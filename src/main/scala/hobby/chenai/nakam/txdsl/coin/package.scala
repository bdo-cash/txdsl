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

package hobby.chenai.nakam.txdsl

import hobby.chenai.nakam.lang.J2S.NonNull
import hobby.chenai.nakam.txdsl.core.coin.{AbsCashGroup, AbsCoinGroup, AbsTokenGroup}
import scala.collection.concurrent.TrieMap
import scala.language.{implicitConversions, postfixOps}

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 10/05/2019
  */
package object coin {
  /*为了用一个`import`就能把所有功能导入，而不用许多个`import`。*/

  type Coin     = AbsCoinGroup
  type Cash     = AbsCashGroup
  type Token    = AbsTokenGroup
  type CoinUnt  = AbsCoinGroup#Unt
  type CashUnt  = AbsCashGroup#Unt
  type TokenUnt = AbsTokenGroup#Unt
  type CoinAmt  = AbsCoinGroup#AbsCoin
  type CashAmt  = AbsCashGroup#AbsCoin
  type TokenAmt = AbsTokenGroup#AbsCoin

  lazy val BTC                                             = BtcToken.BTC
  lazy val SAT                                             = BtcToken.SAT
  implicit lazy val _BTC_i: Int => BtcToken.DslImplInt     = BtcToken.wrapBtcNum
  implicit lazy val _BTC_s: String => BtcToken.DslImpl     = _BTC_d(_)
  implicit lazy val _BTC_b: Double => BtcToken.DslImpl     = BtcToken.wrapBtcNum
  implicit lazy val _BTC_d: BigDecimal => BtcToken.DslImpl = BtcToken.wrapBtcNum
  // implicit lazy val _btc_d = BtcToken.wrapBtcNum(_: BigDecimal)

  lazy val ETH                                             = EthToken.ETH
  lazy val GWei                                            = EthToken.GWei
  lazy val Wei                                             = EthToken.Wei
  implicit lazy val _ETH_i: Int => EthToken.DslImplInt     = EthToken.wrapEthNum
  implicit lazy val _ETH_s: String => EthToken.DslImpl     = _ETH_d(_)
  implicit lazy val _ETH_b: Double => EthToken.DslImpl     = EthToken.wrapEthNum
  implicit lazy val _ETH_d: BigDecimal => EthToken.DslImpl = EthToken.wrapEthNum

  lazy val CNY                                            = CnyCash.CNY
  lazy val CNJiao                                         = CnyCash.Jiao
  lazy val CNFen                                          = CnyCash.Fen
  lazy val CNFen3                                         = CnyCash.Fen3
  implicit lazy val _CNY_i: Int => CnyCash.DslImplInt     = CnyCash.wrapCnyNum
  implicit lazy val _CNY_s: String => CnyCash.DslImpl     = _CNY_d(_)
  implicit lazy val _CNY_b: Double => CnyCash.DslImpl     = CnyCash.wrapCnyNum
  implicit lazy val _CNY_d: BigDecimal => CnyCash.DslImpl = CnyCash.wrapCnyNum

  lazy val USDT                                              = UsdtToken.USDT
  lazy val USFen3                                            = UsdtToken.USFen3
  implicit lazy val _USDT_i: Int => UsdtToken.DslImplInt     = UsdtToken.wrapUsdtNum
  implicit lazy val _USDT_s: String => UsdtToken.DslImpl     = _USDT_d(_)
  implicit lazy val _USDT_b: Double => UsdtToken.DslImpl     = UsdtToken.wrapUsdtNum
  implicit lazy val _USDT_d: BigDecimal => UsdtToken.DslImpl = UsdtToken.wrapUsdtNum

  lazy val GT                                            = GtToken.GT
  implicit lazy val _GT_i: Int => GtToken.DslImpl        = _GT_d(_)
  implicit lazy val _GT_s: String => GtToken.DslImpl     = _GT_d(_)
  implicit lazy val _GT_b: Double => GtToken.DslImpl     = GtToken.wrapGtNum
  implicit lazy val _GT_d: BigDecimal => GtToken.DslImpl = GtToken.wrapGtNum

  lazy val BNB                                             = BnbToken.BNB
  implicit lazy val _BNB_i: Int => BnbToken.DslImpl        = _BNB_d(_)
  implicit lazy val _BNB_s: String => BnbToken.DslImpl     = _BNB_d(_)
  implicit lazy val _BNB_b: Double => BnbToken.DslImpl     = BnbToken.wrapBnbNum
  implicit lazy val _BNB_d: BigDecimal => BnbToken.DslImpl = BnbToken.wrapBnbNum

  lazy val ADA                                             = AdaToken.ADA
  implicit lazy val _ADA_i: Int => AdaToken.DslImpl        = _ADA_d(_)
  implicit lazy val _ADA_s: String => AdaToken.DslImpl     = _ADA_d(_)
  implicit lazy val _ADA_b: Double => AdaToken.DslImpl     = AdaToken.wrapAdaNum
  implicit lazy val _ADA_d: BigDecimal => AdaToken.DslImpl = AdaToken.wrapAdaNum

  lazy val AE                                            = AeToken.AE
  implicit lazy val _AE_i: Int => AeToken.DslImpl        = _AE_d(_)
  implicit lazy val _AE_s: String => AeToken.DslImpl     = _AE_d(_)
  implicit lazy val _AE_b: Double => AeToken.DslImpl     = AeToken.wrapAeNum
  implicit lazy val _AE_d: BigDecimal => AeToken.DslImpl = AeToken.wrapAeNum

  lazy val ALGO                                              = AlgoToken.ALGO
  implicit lazy val _ALGO_i: Int => AlgoToken.DslImpl        = _ALGO_d(_)
  implicit lazy val _ALGO_s: String => AlgoToken.DslImpl     = _ALGO_d(_)
  implicit lazy val _ALGO_b: Double => AlgoToken.DslImpl     = AlgoToken.wrapAlgoNum
  implicit lazy val _ALGO_d: BigDecimal => AlgoToken.DslImpl = AlgoToken.wrapAlgoNum

  lazy val BCH                                             = BchToken.BCH
  implicit lazy val _BCH_i: Int => BchToken.DslImpl        = _BCH_d(_)
  implicit lazy val _BCH_s: String => BchToken.DslImpl     = _BCH_d(_)
  implicit lazy val _BCH_b: Double => BchToken.DslImpl     = BchToken.wrapBchNum
  implicit lazy val _BCH_d: BigDecimal => BchToken.DslImpl = BchToken.wrapBchNum

  lazy val BSV                                             = BsvToken.BSV
  implicit lazy val _BSV_i: Int => BsvToken.DslImpl        = _BSV_d(_)
  implicit lazy val _BSV_s: String => BsvToken.DslImpl     = _BSV_d(_)
  implicit lazy val _BSV_b: Double => BsvToken.DslImpl     = BsvToken.wrapBsvNum
  implicit lazy val _BSV_d: BigDecimal => BsvToken.DslImpl = BsvToken.wrapBsvNum

  lazy val DDD                                             = DddToken.DDD
  implicit lazy val _DDD_i: Int => DddToken.DslImpl        = _DDD_d(_)
  implicit lazy val _DDD_s: String => DddToken.DslImpl     = _DDD_d(_)
  implicit lazy val _DDD_b: Double => DddToken.DslImpl     = DddToken.wrapDddNum
  implicit lazy val _DDD_d: BigDecimal => DddToken.DslImpl = DddToken.wrapDddNum

  lazy val DOGE                                              = DogeToken.DOGE
  implicit lazy val _DOGE_i: Int => DogeToken.DslImpl        = _DOGE_d(_)
  implicit lazy val _DOGE_s: String => DogeToken.DslImpl     = _DOGE_d(_)
  implicit lazy val _DOGE_b: Double => DogeToken.DslImpl     = DogeToken.wrapDogeNum
  implicit lazy val _DOGE_d: BigDecimal => DogeToken.DslImpl = DogeToken.wrapDogeNum

  lazy val DOT                                             = DotToken.DOT
  implicit lazy val _DOT_i: Int => DotToken.DslImpl        = _DOT_d(_)
  implicit lazy val _DOT_s: String => DotToken.DslImpl     = _DOT_d(_)
  implicit lazy val _DOT_b: Double => DotToken.DslImpl     = DotToken.wrapDotNum
  implicit lazy val _DOT_d: BigDecimal => DotToken.DslImpl = DotToken.wrapDotNum

  lazy val ELF                                             = ElfToken.ELF
  implicit lazy val _ELF_i: Int => ElfToken.DslImpl        = _ELF_d(_)
  implicit lazy val _ELF_s: String => ElfToken.DslImpl     = _ELF_d(_)
  implicit lazy val _ELF_b: Double => ElfToken.DslImpl     = ElfToken.wrapElfNum
  implicit lazy val _ELF_d: BigDecimal => ElfToken.DslImpl = ElfToken.wrapElfNum

  lazy val EOS                                             = EosToken.EOS
  implicit lazy val _EOS_i: Int => EosToken.DslImpl        = _EOS_d(_)
  implicit lazy val _EOS_s: String => EosToken.DslImpl     = _EOS_d(_)
  implicit lazy val _EOS_b: Double => EosToken.DslImpl     = EosToken.wrapEosNum
  implicit lazy val _EOS_d: BigDecimal => EosToken.DslImpl = EosToken.wrapEosNum

  lazy val FIL                                             = FilToken.FIL
  implicit lazy val _FIL_i: Int => FilToken.DslImpl        = _FIL_d(_)
  implicit lazy val _FIL_s: String => FilToken.DslImpl     = _FIL_d(_)
  implicit lazy val _FIL_b: Double => FilToken.DslImpl     = FilToken.wrapFilNum
  implicit lazy val _FIL_d: BigDecimal => FilToken.DslImpl = FilToken.wrapFilNum

  lazy val GARD                                              = GardToken.GARD
  implicit lazy val _GARD_i: Int => GardToken.DslImpl        = _GARD_d(_)
  implicit lazy val _GARD_s: String => GardToken.DslImpl     = _GARD_d(_)
  implicit lazy val _GARD_b: Double => GardToken.DslImpl     = GardToken.wrapGardNum
  implicit lazy val _GARD_d: BigDecimal => GardToken.DslImpl = GardToken.wrapGardNum

  lazy val IOTA                                              = IotaToken.IOTA
  implicit lazy val _IOTA_i: Int => IotaToken.DslImpl        = _IOTA_d(_)
  implicit lazy val _IOTA_s: String => IotaToken.DslImpl     = _IOTA_d(_)
  implicit lazy val _IOTA_b: Double => IotaToken.DslImpl     = IotaToken.wrapIotaNum
  implicit lazy val _IOTA_d: BigDecimal => IotaToken.DslImpl = IotaToken.wrapIotaNum

  lazy val IOTX                                              = IotxToken.IOTX
  implicit lazy val _IOTX_i: Int => IotxToken.DslImpl        = _IOTX_d(_)
  implicit lazy val _IOTX_s: String => IotxToken.DslImpl     = _IOTX_d(_)
  implicit lazy val _IOTX_b: Double => IotxToken.DslImpl     = IotxToken.wrapIotxNum
  implicit lazy val _IOTX_d: BigDecimal => IotxToken.DslImpl = IotxToken.wrapIotxNum

  lazy val LBA                                             = LbaToken.LBA
  implicit lazy val _LBA_i: Int => LbaToken.DslImpl        = _LBA_d(_)
  implicit lazy val _LBA_s: String => LbaToken.DslImpl     = _LBA_d(_)
  implicit lazy val _LBA_b: Double => LbaToken.DslImpl     = LbaToken.wrapLbaNum
  implicit lazy val _LBA_d: BigDecimal => LbaToken.DslImpl = LbaToken.wrapLbaNum

  lazy val LTC                                             = LtcToken.LTC
  implicit lazy val _LTC_i: Int => LtcToken.DslImpl        = _LTC_d(_)
  implicit lazy val _LTC_s: String => LtcToken.DslImpl     = _LTC_d(_)
  implicit lazy val _LTC_b: Double => LtcToken.DslImpl     = LtcToken.wrapLtcNum
  implicit lazy val _LTC_d: BigDecimal => LtcToken.DslImpl = LtcToken.wrapLtcNum

  lazy val NKN                                             = NknToken.NKN
  implicit lazy val _NKN_i: Int => NknToken.DslImpl        = _NKN_d(_)
  implicit lazy val _NKN_s: String => NknToken.DslImpl     = _NKN_d(_)
  implicit lazy val _NKN_b: Double => NknToken.DslImpl     = NknToken.wrapNknNum
  implicit lazy val _NKN_d: BigDecimal => NknToken.DslImpl = NknToken.wrapNknNum

  lazy val ONT                                             = OntToken.ONT
  implicit lazy val _ONT_i: Int => OntToken.DslImpl        = _ONT_d(_)
  implicit lazy val _ONT_s: String => OntToken.DslImpl     = _ONT_d(_)
  implicit lazy val _ONT_b: Double => OntToken.DslImpl     = OntToken.wrapOntNum
  implicit lazy val _ONT_d: BigDecimal => OntToken.DslImpl = OntToken.wrapOntNum

  lazy val QTUM                                              = QtumToken.QTUM
  implicit lazy val _QTUM_i: Int => QtumToken.DslImpl        = _QTUM_d(_)
  implicit lazy val _QTUM_s: String => QtumToken.DslImpl     = _QTUM_d(_)
  implicit lazy val _QTUM_b: Double => QtumToken.DslImpl     = QtumToken.wrapQtumNum
  implicit lazy val _QTUM_d: BigDecimal => QtumToken.DslImpl = QtumToken.wrapQtumNum

  lazy val SUSHI                                               = SushiToken.SUSHI
  implicit lazy val _SUSHI_i: Int => SushiToken.DslImpl        = _SUSHI_d(_)
  implicit lazy val _SUSHI_s: String => SushiToken.DslImpl     = _SUSHI_d(_)
  implicit lazy val _SUSHI_b: Double => SushiToken.DslImpl     = SushiToken.wrapSushiNum
  implicit lazy val _SUSHI_d: BigDecimal => SushiToken.DslImpl = SushiToken.wrapSushiNum

  lazy val TSL                                             = TslToken.TSL
  implicit lazy val _TSL_i: Int => TslToken.DslImpl        = _TSL_d(_)
  implicit lazy val _TSL_s: String => TslToken.DslImpl     = _TSL_d(_)
  implicit lazy val _TSL_b: Double => TslToken.DslImpl     = TslToken.wrapTslNum
  implicit lazy val _TSL_d: BigDecimal => TslToken.DslImpl = TslToken.wrapTslNum

  lazy val XRP                                             = XrpToken.XRP
  implicit lazy val _XRP_i: Int => XrpToken.DslImpl        = _XRP_d(_)
  implicit lazy val _XRP_s: String => XrpToken.DslImpl     = _XRP_d(_)
  implicit lazy val _XRP_b: Double => XrpToken.DslImpl     = XrpToken.wrapXrpNum
  implicit lazy val _XRP_d: BigDecimal => XrpToken.DslImpl = XrpToken.wrapXrpNum

  lazy val YAMV2                                              = Yam2Token.YAMV2
  implicit lazy val _YAMV2_i: Int => Yam2Token.DslImpl        = _YAMV2_d(_)
  implicit lazy val _YAMV2_s: String => Yam2Token.DslImpl     = _YAMV2_d(_)
  implicit lazy val _YAMV2_b: Double => Yam2Token.DslImpl     = Yam2Token.wrapYAMv2Num
  implicit lazy val _YAMV2_d: BigDecimal => Yam2Token.DslImpl = Yam2Token.wrapYAMv2Num

  lazy val YFII                                              = YfiiToken.YFII
  implicit lazy val _YFII_i: Int => YfiiToken.DslImpl        = _YFII_d(_)
  implicit lazy val _YFII_s: String => YfiiToken.DslImpl     = _YFII_d(_)
  implicit lazy val _YFII_b: Double => YfiiToken.DslImpl     = YfiiToken.wrapYfiiNum
  implicit lazy val _YFII_d: BigDecimal => YfiiToken.DslImpl = YfiiToken.wrapYfiiNum

  lazy val ZEC                                               = ZCashToken.ZEC
  implicit lazy val _ZEC_i: Int => ZCashToken.DslImpl        = _ZEC_d(_)
  implicit lazy val _ZEC_s: String => ZCashToken.DslImpl     = _ZEC_d(_)
  implicit lazy val _ZEC_b: Double => ZCashToken.DslImpl     = ZCashToken.wrapZecNum
  implicit lazy val _ZEC_d: BigDecimal => ZCashToken.DslImpl = ZCashToken.wrapZecNum

  object Serializer extends (AbsCoinGroup#AbsCoin => (BigDecimal, String)) { // format: off
    def apply(coin: AbsCoinGroup#AbsCoin): (BigDecimal, String) = {
      val std = coin.std
      (std.value, std.unit.name)
    }

    //@throws[RuntimeException]
    def unapply(tuple: (BigDecimal, String)): Option[AbsCoinGroup#AbsCoin] = Option(tuple._2 match { // format: on
      case CNY.name   => tuple._1 CNY
      case USDT.name  => tuple._1 USDT
      case BTC.name   => tuple._1 BTC
      case ETH.name   => tuple._1 ETH
      case GT.name    => tuple._1 GT
      case BNB.name   => tuple._1 BNB
      case ADA.name   => tuple._1 ADA
      case AE.name    => tuple._1 AE
      case ALGO.name  => tuple._1 ALGO
      case BCH.name   => tuple._1 BCH
      case BSV.name   => tuple._1 BSV
      case DDD.name   => tuple._1 DDD
      case DOGE.name  => tuple._1 DOGE
      case DOT.name   => tuple._1 DOT
      case ELF.name   => tuple._1 ELF
      case EOS.name   => tuple._1 EOS
      case FIL.name   => tuple._1 FIL
      case GARD.name  => tuple._1 GARD
      case IOTA.name  => tuple._1 IOTA
      case IOTX.name  => tuple._1 IOTX
      case LBA.name   => tuple._1 LBA
      case LTC.name   => tuple._1 LTC
      case NKN.name   => tuple._1 NKN
      case ONT.name   => tuple._1 ONT
      case QTUM.name  => tuple._1 QTUM
      case SUSHI.name => tuple._1 SUSHI
      case TSL.name   => tuple._1 TSL
      case XRP.name   => tuple._1 XRP
      case YAMV2.name => tuple._1 YAMV2
      case YFII.name  => tuple._1 YFII
      case ZEC.name   => tuple._1 ZEC
      case name @ _   => otherTokensUntStdMap.getOrElseUpdate(name.ensuring(_ == name.toUpperCase.trim), new OtherToken(name).unitStd) * tuple._1
      case _          => ??? // 交易所的所有币种不可能都列出来，不能简单地抛出异常。throw new RuntimeException(s"不存在指定的币种，或不是[标准]单位:$coin".tag)
    })
  }

  implicit class Serializable(coin: AbsCoinGroup#AbsCoin) {
    def serialize: (BigDecimal, String) = Serializer(coin)
  }

  implicit class Deserializable(tuple: (BigDecimal, String)) { // format: off
    def desrl: Option[AbsCoinGroup#AbsCoin] = tuple match {
      case Serializer(coin) => Some(coin.ensuring(_.nonNull)) // 相当于`case opt @ Serializer.unapply(tuple) if opt.isDefined =>`
      case _                => None
    }
    def coin: CoinAmt = desrl.get
    def token: TokenAmt = desrl.get.token
  } // format: on

  implicit class Str2BigInt(count: String) {
    @inline def bigInt: BigInt = string2BigInt(count)
  }

  implicit class BigDec2BigInt(count: BigDecimal) {
    @inline def bigInt: BigInt = bigDecimal2BigInt(count)
  }

  implicit class Str2BigDec(count: String) {
    @inline def bigDec: BigDecimal = string2BigDecimal(count)
  }

  implicit class BigInt2BigDec(count: BigInt) {
    @inline def bigDec: BigDecimal = bigInt2BigDecimal(count)
  }

  @inline implicit def string2BigInt(count: String): BigInt         = BigInt(count)
  @inline implicit def bigDecimal2BigInt(count: BigDecimal): BigInt = count.toBigInt()
  @inline implicit def string2BigDecimal(count: String): BigDecimal = BigDecimal(count)
  @inline implicit def bigInt2BigDecimal(count: BigInt): BigDecimal = BigDecimal(count)
  // NOT needed. 否则`BtcToken`中的`10.pow(8)`有多个隐式转换的错误，但
  // 如果只去掉`int2BigDecimal()`会使原本应用`int`的转而应用`double2BigDecimal()`。
//  @inline implicit def double2BigDecimal(count: Double): BigDecimal = BigDecimal(count)
//  @inline implicit def int2BigDecimal(count: Int): BigDecimal       = BigDecimal(count)

  lazy val ZERO: BigDecimal    = 0
  lazy val ONE: BigDecimal     = 1
  lazy val ONE_e_8: BigDecimal = 1e-8
  lazy val TEN: BigDecimal     = 10

  lazy val ZERO_CNY = 0.CNY
  lazy val ONE_CNY  = 1.CNY

  lazy val ZERO_USDT = 0.USDT
  lazy val ONE_USDT  = 1.USDT

  lazy val DUST_ROUGH: BigDecimal = 0.00001

  lazy val otherTokensUntStdMap = TrieMap.empty[String, OtherToken#UNIT]
}
