package tt55.example.myshoppinglistapp

import android.renderscript.Sampler.Value
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingListItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    val isEditing: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingList(){
    val sItems = remember { mutableStateOf(listOf<ShoppingListItem>()) }
    val showDialog = remember{mutableStateOf(false)}
    val itemNames = remember { mutableStateOf("") }
    val itemQuantity = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Button(

            onClick = {showDialog.value= true },
            modifier = Modifier.size(200.dp, 50.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,//inside of button
                containerColor = Color.Red//background of button
            )
        )
        {

            Text("SETTIMO")

        }
        LazyColumn (modifier = Modifier.fillMaxSize().padding(16.dp)){
            items(sItems.value){
                item ->
                if(item.isEditing){
                    ShoppingListItemEditor(item = item, onEditeComplete = {
                            editeName, editeQuantity ->
                        sItems.value = sItems.value.map {it.copy(isEditing = false)}
                        val editeItem = sItems.value.find { it.id == item.id }
                        editeItem?.let {
                            it.name = editeName
                            it.quantity = editeQuantity
                        }
                    })

                }else{
                    ShoppingListItem(item = item,onEditeClick =
                    {sItems.value = sItems.value.map {it.copy(isEditing = it.id == item.id)}},
                        onDeleteClick = {
                            sItems.value = sItems.value.filter { it.id != item.id }
                })

                }
            }




        }

    }
    if(showDialog.value) {

        AlertDialog(onDismissRequest = {showDialog.value=false },
            confirmButton =  {


                // new button inside the dialog box
                Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {



                    Button(onClick = {
                        if (itemNames.value.isNotBlank()){
                            val newItem = ShoppingListItem(
                                id = sItems.value.size + 1,
                                name = itemNames.value,
                                quantity = itemQuantity.value.toInt()
                            )
                            sItems.value += newItem
                            showDialog.value = false
                            itemNames.value =""
                        }
                    } )
                    {                                      //button number 1 for add
                        Text("Add")
                    }



                    Button(onClick = {
                        showDialog.value=false       //Button number  2 for cancel
                    } ) {

                        Text("Cancel")
                    }

                }//row ka semicolon hia
            },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(value = itemNames.value,onValueChange = {itemNames.value=it},
                        singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))

                    //Quantity hai
                    OutlinedTextField(value = itemQuantity.value,onValueChange = {itemQuantity.value=it},
                        singleLine = true, modifier = Modifier.fillMaxWidth().padding(8.dp))
                }


            }

        )


    }

}
@Composable
fun ShoppingListItemEditor(item: ShoppingListItem,onEditeComplete:(String,Int )->Unit){
    var editeName= remember { mutableStateOf(item.name) }
    var editeQuality =remember  { mutableStateOf(item.quantity.toString()) }
    val isEditing = remember { mutableStateOf(item.isEditing) }

    //row22
    Row (modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly){


        Column {
            BasicTextField(
                value = editeName.value,
                onValueChange = { editeName.value = it },
                singleLine= true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editeQuality.value,
                onValueChange = { editeQuality.value = it },
                singleLine= true,
                modifier=Modifier.wrapContentSize().padding(8.dp)
            )

        }
        Button(
            onClick = {
                isEditing.value = false
                onEditeComplete(editeName.value, editeQuality.value.toIntOrNull() ?: 1 )

            }){

            Text("SAVE")

        }



    }//row last





}//composable last line


@Composable
fun ShoppingListItem(
    item: ShoppingListItem,
    onEditeClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Black, shape = RoundedCornerShape(30.dp))
            .background(Color.White), // White background added here
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = item.quantity.toString(), modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            // Single IconButton for editing
            IconButton(onClick = onEditeClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}


