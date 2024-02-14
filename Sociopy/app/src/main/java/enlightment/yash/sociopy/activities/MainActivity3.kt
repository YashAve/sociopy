package enlightment.yash.sociopy.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import enlightment.yash.sociopy.R

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlayRows()
        }
    }
}

@Preview
@Composable
private fun PlayRows() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF2F2F2))
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.burger_image),
            contentDescription = "burger",
            modifier = Modifier.height(300.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Happy Meal",
                    style = TextStyle(fontSize = TextUnit(value = 26F, type = TextUnitType.Sp)),
                    modifier = Modifier.align(Alignment.CenterVertically)

                )
                Text(
                    text = "$5.99",
                    color = Color.Black,
                    style = TextStyle(fontSize = TextUnit(value = 18F, type = TextUnitType.Sp))
                )
            }
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Text(
                text = "800 calories",
                style = TextStyle(fontSize = TextUnit(value = 17F, type = TextUnitType.Sp))
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Button(onClick = {}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(text = "ORDER NOW")
            }
        }
    }
}

@Preview
@Composable
private fun RowsAndColumns() {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(border = BorderStroke(width = 1.dp, color = Color.Black)),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "ITEM1", modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(text = "ITEM2", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .border(border = BorderStroke(width = 1.dp, color = Color.Black)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "ITEM1", modifier = Modifier.align(Alignment.CenterVertically))
            Text(text = "ITEM2", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Preview
@Composable
private fun PlayColumns() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF2F2F2))
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.burger_image),
            contentDescription = "burger",
            modifier = Modifier.height(300.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Happy Meal",
                style = TextStyle(fontSize = TextUnit(value = 26F, type = TextUnitType.Sp))
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Text(
                text = "800 calories",
                style = TextStyle(fontSize = TextUnit(value = 17F, type = TextUnitType.Sp))
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Text(
                text = "$5.99",
                color = Color.Black,
                style = TextStyle(fontSize = TextUnit(value = 18F, type = TextUnitType.Sp))
            )
        }
    }
}