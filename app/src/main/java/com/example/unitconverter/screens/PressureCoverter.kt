package com.example.unitconverter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unitconverter.R
import com.example.unitconverter.viewmodels.MassViewModel
import com.example.unitconverter.viewmodels.PressureViewModel
import com.example.unitconverter.viewmodels.TemperatureViewModel

@Composable
fun PressureConverter() {
    val viewModel: PressureViewModel = viewModel()
    val strBar = stringResource(id = R.string.bar)
    val strPsi= stringResource(id = R.string.psi)
    val currentValue = viewModel.pressure.observeAsState(viewModel.pressure.value ?: "")
    val unit = viewModel.unit.observeAsState(viewModel.unit.value ?: R.string.bar)
    var result by rememberSaveable { mutableStateOf("") }
    val enabled by remember(currentValue.value) {
        mutableStateOf(!viewModel.getPressureAsFloat().isNaN())
    }

    val calc = {
        val temp = viewModel.convert()

        result = if (temp.isNaN())
            ""
        else
            "$temp${
                if (unit.value == R.string.bar)
                    strPsi
                else strBar
            }"
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PressureTextField(
            pressure = currentValue,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc,
            viewModel = viewModel
        )
        PressureButtonGroup(
            selected = unit,
            modifier = Modifier.padding(bottom = 16.dp)
        ) { resId: Int -> viewModel.setUnit(resId) }
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.h3
            )
        }
    }
}

@Composable
fun PressureTextField(
    pressure: State<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit,
    viewModel: PressureViewModel
) {
    TextField(
        value = pressure.value,
        onValueChange = {
            viewModel.setPressure(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.Pressure))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun PressureButtonGroup(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val sel = selected.value
    Row(modifier = modifier) {
        PressureRadioButton(
            selected = sel == R.string.bar,
            resId = R.string.bar,
            onClick = onClick
        )
        PressureRadioButton(
            selected = sel == R.string.psi,
            resId = R.string.psi,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )

    }
}

@Composable
fun PressureRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}