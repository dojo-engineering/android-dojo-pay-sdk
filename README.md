# Dojo Pay SDK Android 🤖

This project contains the SDK for payments. This will be used in our internal apps but also will be used for external customers.

## How to use

SDK functionality can be accessed via `DojoSdk` object.

## Card payment

Card payments are handled by `DojoCardPaymentHandler`. It can be instantiated via `DojoSdk.createCardPaymentHandler` factory method, which takes `activity` and `result callback` as parameters. Payment process begins when `DojoCardPaymentHandler.executeCardPayment` is being invoked.

```
class CardPaymentActivity : AppCompatActivity() {

   private val cardPayment = DojoSdk.createCardPaymentHandler(this) { result ->
      progressBar.isVisible = false
      showResult(result)
   }

   override fun onPayClicked(token: String, payload: DojoCardPaymentPayload) {
      progressBar.isVisible = true
      cardPayment.executeCardPayment(token, payload)
   }

}
```
## Google pay
### Please note that in order to test google pay in production you need to register your app at the google pay api consol.
### Google pay availability
Before we start on the google pay payment you need to check if google pay is available on that device by calling `isGpayAvailable()` from `DojoSdk`

```
        DojoSdk.isGpayAvailable(
            activity,
            DojoGPayConfig(
                collectShipping = true ,
                collectBilling = true,
                collectPhoneNumber = true,
                collectEmailAddress =true,
                merchantName = "",
                merchantId = "",
                gatewayMerchantId = ""
            ),
            { 
            // handle if the google pay is available on this device
            googlePayButton.visibility = View.VISIBLE 
            },
            {
            // handle if the google pay is  not available on this device
            googlePayButton.visibility = View.GONE 
            }
        )
```
### Google pay payment
After making sure that google pay is avilibe on that device now we can start on making the payment using google pay , to that  you first begin on building the `DojoGPayPayload`from our sdk this object consists
1. `DojoGPayConfig`:  this object has all the google pay configurations most of them are optional it's based on your case if you want to enable or disable them like `collectShipping` for example, but what is required is the following parameters
    - `merchantName` : this is your mechant name
    - `merchantId` : this id you got from the google pay api console after submitting your app for review to get production access to google pay
    - `gatewayMerchantId`: the id you got from dojo protal
2. `email` :user email and it's optional
   sample object
```
  DojoGPayConfig(
                        collectShipping = true,
                        allowedCountryCodesForShipping =  listOf("US", "GB"),
                        collectBilling = true,
                        collectPhoneNumber = true,
                        collectEmailAddress = true,
                        merchantName = "",
                        merchantId = "",
                        gatewayMerchantId = ""
                    ),
                    email= 
                )
```


Second object that you need to provide for making the gpay payment is `dojoPaymentIntent`
this object is used to make the sdk know the payment details like the `PaymentToken` and also the `Payment Amount`
Sample object
```
     dojoPaymentIntent = DojoPaymentIntent(
                    token = "token",
                    totalAmount = DojoTotalAmount(10, "GBP")
                )
```

Google pay  payments are handled by `DojoGPayHandler`. It can be instantiated via `DojoSdk.createGPayHandler` factory method, which takes `activity` and `result callback` as parameters. Payment process begins when `DojoGPayHandler.executeGPay` is being invoked.

```
class GpayPaymentActivity : AppCompatActivity() {

   private val gPayment = DojoSdk.createGPayHandler(this) { result ->
        showResult(result)
    }

     override fun onGPayClicked(
        dojoGPayPayload: DojoGPayPayload,
        dojoPaymentIntent: DojoPaymentIntent
    ) {
        gPayment.executeGPay(dojoGPayPayload, dojoPaymentIntent)
    }

}
```


#### Receive payment result as activity result

In order to launch card payment activity for result, call:

```
DojoSdk.startCardPayment(activity, token, payload)
DojoSdk.startGPay(this, dojoGPayPayload, dojoPaymentIntent)

``` 

Override `onActivityResult()` callback and parse the result:
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {  
    val result = DojoSdk.parseCardPaymentResult(requestCode, resultCode, data)  
    val gPayResult = DojoSdk.parseGPayPaymentResult(requestCode, resultCode, data)
}
```

### Sandbox mode

`sandbox` property controls whether payments should be processed on test environment. More details about test environment (token, test cards) can be found [here](https://docs.connect.paymentsense.cloud/ConnectE/SettingUpTestAccount).

Just set the property to true to enable sandbox mode:

    DojoSdk.sandbox = true

### Result codes

Backend codes
```
SUCCESSFUL = 0  
AUTHORIZING = 3 
REFERRED = 4  
DECLINED = 5
DUPLICATE_TRANSACTION = 20 
FAILED = 30  
WAITING_PRE_EXECUTE = 99
INVALID_REQUEST = 400 
ISSUE_WITH_ACCESS_TOKEN = 401  
NO_ACCESS_TOKEN_SUPPLIED = 404 
INTERNAL_SERVER_ERROR = 500
```

Additional codes
```
SDK_INTERNAL_ERROR = 7770 //Network connection issues or other issues
```

When payment process is terminated by user (e.x. closed 3ds screen), then `DECLINED(5)` error will be returned.

## How to set up

### Add dependency

`implementation 'tech.dojo.pay:sdk:1.5.0' `

### Configure authentication

- Authentication to GitHub Packages is required for installing or using the sdk. More details about Github Authentication for packages can be found at https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
- Ensure `credentials.properties` file is populated with the following
```  
gpr.user=GITHUB_USERID 
gpr.key=PERSONAL_ACCESS_TOKEN  
```  
Replace GITHUB_USERID with personal / organisation Github User ID and PERSONAL_ACCESS_TOKEN with the token generated. Instructions for generating personal access token is provided below.

#### Generate a Personal Access Token for GitHub
- Inside you GitHub account:
- Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
- Make sure you select the following scopes (“ read:packages”) and Generate a token
- After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.
- Replace GITHUB_USERID with personal / organisation Github User ID and PERSONAL_ACCESS_TOKEN with the token generated

**NOTE: DO NOT COMMIT WITH YOUR GITHUB CREDENTIALS TO REPOSITORY. IT IS ONY MEANT TO BE USED LOCALLY.**

## Releasing ##

Please read our [release guidelines](/RELEASING.md) for publishing or releasing artifacts.

## Contributing ##

We love contributions! Please read our [contribution guidelines](/CONTRIBUTING.md) to get started.