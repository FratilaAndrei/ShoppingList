package com.example.shoppinglistudemy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(
            onClick = { showDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item frate")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(shoppingItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditComplete = { editName, editQuantity ->
                        shoppingItems = shoppingItems.map { it.copy(isEditing = false) }
                        // mapam peste items, facem copie la item-u pe care il editam, iar la final de editare il facem false
                        val editItem = shoppingItems.find { item.id == it.id }
                        editItem?.let{
                            it.name = editName
                            it.quantity = editQuantity
                        }
                    })
                }else{
                    ShoppingListItem(item = item, onEdit = {
                        // We map over the items and the one we are editing we set to True
                        shoppingItems = shoppingItems.map{it.copy(isEditing = it.id == item.id)}
                    }, onDelete = {
                        shoppingItems -= item
                    })
                        

                }
//                ShoppingListItem(item = it, {}, {})
            }
        }

    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                        val newItem = ShoppingItem(
                            id = shoppingItems.size + 1,
                            name = itemName,
                            quantity = itemQuantity.toInt()
                        )
                        shoppingItems = shoppingItems + newItem
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text(text = "Add")
                }

                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        }, title = { Text(text = "Add Shopping Item") }, text = {
            Column {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true
                )
            }
        })
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editName by remember {
        mutableStateOf(item.name)
    }
    var editQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editName,
                onValueChange = { editName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editQuantity,
                onValueChange = { editQuantity = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editName, editQuantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                BorderStroke(
                    2.dp, Color(0XFF018786)
                ), shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = item.name, modifier = Modifier.padding(20.dp))
        Text(text = "Quantity: ${item.quantity}", modifier = Modifier.padding(20.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Icon for edit")
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Icon for delete")
            }
        }
    }
}
