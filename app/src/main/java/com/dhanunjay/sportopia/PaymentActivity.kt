package com.dhanunjay.sportopia

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PaymentActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current

            // Gradient background
            val gradient45 = Brush.linearGradient(
                colors = listOf(Color.White, Color.White),
                start = Offset(0f, Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY, 0f)
            )

            Surface(
                modifier = Modifier
                    .background(gradient45)
                    .fillMaxSize()
                    .padding(28.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient45),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.card),
                        contentDescription = "",
                        modifier = Modifier
                            .offset(x = 10.dp, y = 0.dp)
                            .requiredWidth(320.dp)
                            .requiredHeight(220.dp)
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    HeadingTextComponent(value = "Payment Details")

                    Spacer(modifier = Modifier.height(15.dp))

                    // Text Fields
                    MyTextFieldComponent(
                        labelValue = "Bank Name",
                        onTextChanged = { /* Handle text change */ },
                        textColor = Color.Black
                    )

                    MyTextFieldComponent(
                        labelValue = "Card Number",
                        onTextChanged = { /* Handle text change */ },
                        textColor = Color.Black
                    )

                    MyTextFieldComponent(
                        labelValue = "Sort Code",
                        onTextChanged = { /* Handle text change */ },
                        textColor = Color.Black
                    )

                    MyTextFieldComponent(
                        labelValue = "3 digits Cvv Number",
                        onTextChanged = { /* Handle text change */ },
                        textColor = Color.Black
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .heightIn(48.dp),
                        onClick = {
                            context.startActivity(Intent(context, ThankYou::class.java))
                        },
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        // Add your content inside the button (e.g., Text or Icon)
                    }

                    Box(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun MyTextFieldComponent(
    labelValue: String,
    onTextChanged: (String) -> Unit,
    textColor: Color
) {
    OutlinedTextField(
        value = labelValue,
        onValueChange = onTextChanged,
        label = { Text(text = labelValue, color = textColor) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(color = textColor)
    )
}

@Composable
fun HeadingTextComponent(value: String, fontSize: androidx.compose.ui.unit.TextUnit = 24.sp) {
    Text(
        text = value,
        fontSize = fontSize,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview
@Composable
fun DefaultPreviewOfPaymentScreen() {
    PaymentActivity()
}
