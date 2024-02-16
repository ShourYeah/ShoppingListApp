package com.example.shoppinglistapp

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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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

data class ShoppingItem(val id:Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing: Boolean = false)  // Being Used in ShoppingItemEditor



@Composable

fun ShoppingListApp(){

    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())  //ShoppingItem is an object considered data type here, it will contain all data passed in the data class
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    var itemName by remember { mutableStateOf("") } //Contains list item name used in alert box
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center
    ) {
        Button(onClick = { showDialog= true }, // Alert dialog visible
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        {
            Text(text = "Add Item")

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // This will push the Add Item button at the top automatically

        ){
            items(sItems){
                item-> //Using 'it' as 'item'
                if(item.isEditing){
                    ShoppingItemEditor(item =item , onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(isEditing = false) }  //maps & copies item
                        val editedItem = sItems.find { it.id == item.id } //finds iterable items
                        editedItem?.let {
                            it.name= editedName
                            it.quantity= editedQuantity
                        }
                    })
                }
                else{

                    // For on edit click
                    ShoppingListItem(item = item,
                        onEditClick = {
                        // finding out which item we are editing and changing is "isEditing boolean" to true
                        sItems= sItems.map{it.copy(isEditing = it.id==item.id)}

                    }, onDeleteClick = {
                        sItems= sItems-item

                    })
                }

            }
        }

    }

    if(showDialog){
        AlertDialog(onDismissRequest = { showDialog= false }, confirmButton = {

             Row(
                 modifier= Modifier
                     .fillMaxWidth()
                     .padding(8.dp),
                 horizontalArrangement = Arrangement.SpaceBetween
             ) {
                    Button(onClick = {
                        if(itemName.isNotBlank()){
                            val newItem = ShoppingItem(
                                id= sItems.size+1,
                                name= itemName,
                                quantity = itemQuantity.toInt()
                            )
                                sItems = sItems+ newItem
                            showDialog= false
                            itemName="" //After enterning once the itemName will be reset so that user do not need to erase themselves

                        }
                    }) {
                        Text(text = "Add")
                    }

                    Button(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }
             }
        },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange ={itemName=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        ) //'it' helps in providing the new information in case of data change

                    OutlinedTextField(value = itemQuantity, onValueChange ={itemQuantity=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            )

        }
    }

@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete:(String, Int)->Unit){
    var editedName by remember { mutableStateOf(item.name)} //While editing, the default value of the name being edited is the original name
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing)}

   Row (modifier = Modifier
       .fillMaxWidth()
       .background(Color.White)
       .padding(8.dp),
       horizontalArrangement = Arrangement.SpaceEvenly)
   {

       Column {
           BasicTextField(value = editedName,
               onValueChange = {editedName=it},
               singleLine = true,
               modifier = Modifier
                   .wrapContentSize()
                   .padding(8.dp)
               )

           BasicTextField(value = editedQuantity,
               onValueChange = {editedQuantity=it},
               singleLine = true,
               modifier = Modifier
                   .wrapContentSize()
                   .padding(8.dp)
           )
       }

       Button(onClick = {
           isEditing= false
           onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)  // Elvis operator if value will be null, it will be assigned as 1

       }) {
           Text(text = "Save")
       }

   }

}








@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,  //Lambda function on click
    onDeleteClick: () -> Unit,
    )
{
    Row(
        modifier= Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text= item.name, modifier = Modifier.padding(8.dp))
        Text(text= "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {

            IconButton(onClick = onEditClick){
               Icon(imageVector = Icons.Default.Edit , contentDescription = null)

            }

            IconButton(onClick = onDeleteClick){
                Icon(imageVector = Icons.Default.Delete , contentDescription = null)

            }
        }
        }
    }





