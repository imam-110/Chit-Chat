package kiet.imam.chatkaro.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kiet.imam.chatkaro.R
import kiet.imam.chatkaro.viewmodel.MessageViewModel
import kiet.imam.chatkaro.data.Message


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message : Message){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = if(message.isSentByCurrentUser){
            Alignment.End
        }else Alignment.Start
        ) {
        Box(
            modifier = Modifier
                .background(
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700) else colorResource(
                        id = R.color.teal_200
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)) {
            Text(text = message.text,
                color = colorResource(id = R.color.white),
                style = TextStyle(fontSize = 16.sp)
                )

        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = message.senderFirstName,
            style = TextStyle(
                fontSize = 12.sp),
            color = colorResource(id = R.color.black)
            )
        Text(text = formatTimeStamp(message.timestamp),
            style = TextStyle(
                fontSize = 12.sp,
                color =  colorResource(id = R.color.black)
            )
            )


    }

}









@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(roomId : String,
               messageViewModel: MessageViewModel = viewModel(),
               ){
    val messages by messageViewModel.messages.observeAsState(emptyList())
    messageViewModel.setRoomId(roomId)
    val text = remember {
        mutableStateOf("")}
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages){
                    message->
                    ChatMessageItem(message = Message(isSentByCurrentUser =
                    message.senderId == messageViewModel.currentUser.value?.email))
                }

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(value = text.value, onValueChange ={
                        text.value = it
                    }, textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                IconButton(onClick ={
                    if(text.value.isNotEmpty()){
                        messageViewModel.sendMessage(text.value.trim())
                        text.value =""
                    }
                    messageViewModel.loadMessages()
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")

                }
            }


    }

}





@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimeStamp(timeStamp : Long) : String {
    val messageDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeStamp),
        ZoneId.systemDefault()
    )
    val now = LocalDateTime.now()

    return when {
        isSameDay(messageDateTime, now)->"today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1),now)->"yesterday ${formatTime(messageDateTime)}"
        else->
            formatDate(messageDateTime)
    }

}




@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1 :LocalDateTime, dateTime2 : LocalDateTime) : Boolean{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime : LocalDateTime): String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)

}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime) : String{
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return formatter.format(dateTime)

}


