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

### Sandbox mode

`sandbox` property controls whether payments should be processed on test environment. More details about test environment (token, test cards) can be found [here](https://docs.connect.paymentsense.cloud/ConnectE/SettingUpTestAccount).

Just set the property to true to enable sandbox mode:

    DojoSdk.sandbox = true

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