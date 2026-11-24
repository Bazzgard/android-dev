package com.example.helloworld

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private lateinit var scanResult: EditText

    private val scanner = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let { scanResult.setText(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spacing = (16 * resources.displayMetrics.density).roundToInt()
        scanResult = EditText(this).apply {
            id = R.id.scan_result
            setHint(R.string.scan_result_hint)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            gravity = Gravity.TOP or Gravity.START
        }
        val scanButton = Button(this).apply {
            setText(R.string.scan_button)
            setOnClickListener {
                scanner.launch(ScanOptions().apply {
                    setPrompt(getString(R.string.scan_prompt))
                    setBeepEnabled(false)
                    setOrientationLocked(true)
                })
            }
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            isFocusableInTouchMode = true
            addView(scanResult, LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f))
            addView(scanButton, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = spacing
            })
        }
        ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
            val safeArea = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime()
            )
            view.setPadding(
                spacing + safeArea.left,
                spacing + safeArea.top,
                spacing + safeArea.right,
                spacing + safeArea.bottom
            )
            insets
        }
        setContentView(content)
    }
}
