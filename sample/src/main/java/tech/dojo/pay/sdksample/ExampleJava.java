package tech.dojo.pay.sdksample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import kotlin.Unit;
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig;
import tech.dojo.pay.uisdk.DojoSDKDropInUI;
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams;
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandler;

public class ExampleJava extends AppCompatActivity {
    private final DojoPaymentFlowHandler dojoPaymentFlowHandler = DojoSDKDropInUI.INSTANCE.createUIPaymentHandler(
            this,
            ((dojoPaymentResult) -> {
                Toast.makeText(this, dojoPaymentResult.name(),
                        Toast.LENGTH_LONG).show();
                return Unit.INSTANCE;
            }));
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_ui);

        // on pay clicked
        DojoGPayConfig dojoGPayConfig = new DojoGPayConfig(
                false,
                null,
                false,
                false,
                false,
                "merchantName",
                "merchantId",
                "gatewayMerchantId",
                new ArrayList<>()
        );

        Button button= findViewById(R.id.startPaymentJava);
        button.setOnClickListener(v -> dojoPaymentFlowHandler.startPaymentFlow(
                new DojoPaymentFlowParams(
                        "", // payment-intent-id
                        "" , // add this if you supports saved card else pass null
                        dojoGPayConfig // add this if you support google pay else pass null
                )
        ));


    }
}