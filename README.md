# Dojo Pay SDK Android 🤖

This project contains the SDK for payments. This will be used in our internal apps but also will be used for external customers.

## How to use

SDK functionality can be accessed via `DojoSdk` object.

### Card payment

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

#### Receive payment result as activity result

In order to launch card payment activity for result, call:

```
DojoSdk.startCardPayment(activity, token, payload)
``` 

Override `onActivityResult()` callback and parse the result:
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {  
    val result = DojoSdk.parseCardPaymentResult(requestCode, resultCode, data)  
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

`implementation 'tech.dojo.pay:sdk:1.0.0' `

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