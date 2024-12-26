# Jetpack Compose Picker

Android library providing a Picker implemented by Jetpack Compose.


![License MIT](https://img.shields.io/badge/Apache_2.0-9E9F9F?label=License)
![Android minimuml version](https://img.shields.io/badge/26+-9E9F9F?&label=Minimum&logo=android)

## Features

- ✅️ Same experience as [NumberPicker view](https://developer.android.com/reference/android/widget/NumberPicker)
- ✅️ Supports scroll and fling
- ✅️ Compatible with Material3
- ✅️ Custamizable with slot pattern

<!-- TODO screenshot here -->

## Installation

TODO

## Basic Usage

### Number picker

<img src="capture/sample_number_picker.png" height="240">

```kotlin
    var value by remember { mutableIntStateOf(0) }

    NumberPicker(
        value = value,
        range = 0..10,
        onValueChange = { value = it },
    )
```

### Picker with generic values

<img src="capture/sample_generic_picker.png" height="240">

```kotlin
    val values = remember {
        listOf(
            LocalDate.of(2024, 12, 1),
            LocalDate.of(2024, 12, 2),
            LocalDate.of(2024, 12, 3),
            LocalDate.of(2024, 12, 4),
            LocalDate.of(2024, 12, 5),
            LocalDate.of(2024, 12, 6),
        )
    }

    var value by remember { mutableStateOf(values.first()) }

    Picker(
        value = value,
        values = values,
        onValueChange = { value = it },
    )
```

### Customize picker

<img src="capture/sample_custom_picker.png" height="240">

```kotlin
    val values = remember {
        listOf(
            Icons.Outlined.Build,
            Icons.Outlined.MailOutline,
            Icons.Outlined.Call,
            Icons.Outlined.AccountCircle,
            Icons.Outlined.Create,
            Icons.Outlined.Delete,
        )
    }
    var value by remember { mutableStateOf(values.first()) }
    val state = rememberPickerState(value, values) { value = it }

    Picker(
        state = state,
        enabled = true,
        colors = PickerDefaults.colors(contentColor = MaterialTheme.colorScheme.secondary),
        labelSize = DpSize(240.dp, 64.dp),
        dividerHeight = 4.dp,
        flingBehavior = PickerDefaults.flingBehavior(state = state, flingEnabled = false),
    ) { current, enabled ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = current,
                contentDescription = null,
            )
            Text(text = current.name)
        }
    }
```
