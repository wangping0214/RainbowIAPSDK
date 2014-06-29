/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.rainbow.iap.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088411193613191";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "frank@chaimiyouxi.com";

	//商户私钥，自助生成
	public static final String PRIVATE = 
	"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALtZaaMfEJY5EKqY" +
	"YaP2zSUOn5MKlFRxU7+8tZ6pyb1ywVr8K92DWeqy9PSpebTLk2LU1Esa0zRYZTrT" +
	"SaQE2nUtZ4szR9gYmBx8iIGDQga0QQfMyAKO16/n0LctL3JHB22VUfFS+VEBJ6M1" +
	"FMxtt8Q3DfoNYdl6mOD4hETmpoxJAgMBAAECgYBgauOyz4n5xeSN515Yw+tP5va4" +
	"9fjgfHJdewD9ZuQsW6Km8KCin7bm0rK+N3orUZnIgz++Z0/K4LM4UwTTJKxIQC0R" +
	"xDa+0rHgOvIcK7+teXEj58/GqHo/yYJfauQz83YsqCb3VfprlRCWbu/vvAMgwlcZ" +
	"x/5F/QgulSV4CmNAAQJBAOVjYtTMjoxW2XQXhp3EBK2CON+BSTb/E36t2bhht6CV" +
	"i6gsbVM3gT2DoM5JPJaf7M8Wq1OHsjEeUTUQB2KIa+kCQQDRFYJZEtLy3kOqECL9" +
	"FWBRUkJZsjY+lEw6+MdAzIYB73PpmOO3+LELE+RJmcfbFDkBC38RG6BHbZm99VrI" +
	"48FhAkAmcu847gSiv1f5novg299Q2fgAdqI4Bq9U130b670kvIxJJxE4FqCiF/MX" +
	"QK1YLfw6hfk3qhITK5q/Ay3JtUYpAkBk2WYdBrpfURv8HHpz7mqd7vp3/0Cw4KEA" +
	"VNzvAXel2VTkmM1GAJuMx1R2t8kxf8ibG2t32gZuTYw5lu3qNgkhAkBmZlJzrzOd" +
	"HR/l/wgRDVnZAvngT+54RwNcOWwSzcOEEmDyDLtcQJDIzkCwBLrEbPmxwfi6a/5t" +
	"xb/DLxPglEoV";

	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
