package ch.ihdg.calendarcolor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.ihdg.calendarcolor.ui.theme.CalendarColorTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted: Map<String, @JvmSuppressWildcards Boolean> ->
            if (isGranted.values.all { it }) {
                startActivity(Intent(this, CalendarListActivity::class.java))
            } else {
                setContent {
                    CalendarColorTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MissingPermission(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                // Permissions are given. Start the actual app
                startActivity(Intent(this, CalendarListActivity::class.java))
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_CALENDAR
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_CALENDAR
            ) -> {
                setContent {
                    CalendarColorTheme {
                        Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                            ExplainPermission(
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                requestPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_CALENDAR,
                                        Manifest.permission.WRITE_CALENDAR
                                    )
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                    )
                )
            }
        }
    }
}

@Composable
fun ExplainPermission(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.explain_permissions),
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = onClick,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.grant_calendar_permissions))
        }
    }
}

@Composable
fun MissingPermission(modifier: Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.cant_use),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = stringResource(R.string.go_to_settings),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExplainPermissionPreview() {
    CalendarColorTheme {
        ExplainPermission {}
    }
}

@Preview(showBackground = true)
@Composable
fun MissingPermissionPreview() {
    CalendarColorTheme {
        MissingPermission(Modifier)
    }
}
