package com.italo.pokerdroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.google.firebase.firestore.FirebaseFirestore
import com.italo.pokerdroid.data.model.GameState
import com.italo.pokerdroid.data.model.User
import com.italo.pokerdroid.ui.MainViewModel
import com.italo.pokerdroid.ui.theme.PokerdroidTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firestore =
            FirebaseFirestore.getInstance().collection("game").document("Fgx3J3El6sswom7cilz0")
                .collection("user")

        setContent {
            val gameState = viewModel.gameState.collectAsState().value
            var usernameTextFieldValue by remember { mutableStateOf("") }
            var fibonacciSeed by remember { mutableStateOf(2) }
            var userJoined by remember { mutableStateOf(User()) }

            PokerdroidTheme {
                Box {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Scaffold(
                    backgroundColor = Color.Transparent
                ) {
                    if (userJoined.id.isNullOrEmpty()) {
                        ConstraintLayout(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val (nameTitle, nameInput, joinButton) = createRefs()

                            Text(
                                text = "Digite seu nome : ",
                                style = TextStyle(fontSize = 35.sp, color = Color.White),
                                modifier = Modifier
                                    .constrainAs(nameTitle) {
                                        top.linkTo(parent.top, 20.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .padding(20.dp)
                            )

                            TextField(
                                value = usernameTextFieldValue,
                                onValueChange = { usernameTextFieldValue = it },
                                modifier = Modifier.constrainAs(nameInput) {
                                    top.linkTo(nameTitle.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                )
                            )

                            Button(
                                modifier = Modifier.constrainAs(joinButton) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(nameInput.bottom, 15.dp)
                                },
                                onClick = {
                                    firestore.add(
                                        User(
                                            name = usernameTextFieldValue,
                                            vote = 0,
                                            voted = false
                                        )
                                    ).addOnSuccessListener {
                                        userJoined = User(
                                            id = it.id,
                                            name = usernameTextFieldValue,
                                            vote = 0,
                                            voted = false
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(text = "Entrar no Poker Game")
                            }

                        }
                    } else {
                        ConstraintLayout(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val (playersList, voteText, subtractButton, increaseButton, voteButton, revealButton, resetButton) = createRefs()

                            LazyVerticalGrid(
                                cells = GridCells.Fixed(3),
                                modifier = Modifier.constrainAs(playersList) {
                                    top.linkTo(parent.top, 20.dp)
                                    bottom.linkTo(voteText.top, 10.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    height = Dimension.fillToConstraints
                                },
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(gameState.users) { item ->
                                    playerCell(user = item, gameState = gameState)
                                }
                            }
                            Text(
                                text = fibonacci(fibonacciSeed).toString(),
                                style = TextStyle(color = Color.White, fontSize = 20.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.constrainAs(voteText) {
                                    bottom.linkTo(parent.bottom, 16.dp)
                                    start.linkTo(subtractButton.end,15.dp)
                                    top.linkTo(playersList.bottom,10.dp)
                                    width = Dimension.value(30.dp)
                                    visibility = if (gameState.showVotes == true) Visibility.Gone else Visibility.Visible
                                }
                            )
                            Button(
                                modifier = Modifier.constrainAs(subtractButton) {
                                    start.linkTo(parent.start,10.dp)
                                    top.linkTo(playersList.bottom,10.dp)
                                    bottom.linkTo(parent.bottom,10.dp)
                                    visibility = if (gameState.showVotes == true) Visibility.Gone else Visibility.Visible
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = { if (fibonacciSeed in 3..11) fibonacciSeed-- }) {
                                Text(
                                    text = "-",
                                    style = TextStyle(fontSize = 20.sp, color = Color.Red)
                                )
                            }
                            Button(
                                modifier = Modifier.constrainAs(increaseButton) {
                                    start.linkTo(voteText.end,15.dp)
                                    top.linkTo(playersList.bottom,10.dp)
                                    bottom.linkTo(parent.bottom,10.dp)
                                    visibility = if (gameState.showVotes == true) Visibility.Gone else Visibility.Visible
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = { if (fibonacciSeed in 2..10) fibonacciSeed++ }) {
                                Text(
                                    text = "+",
                                    style = TextStyle(fontSize = 20.sp, color = Color.Green)
                                )
                            }
                            Button(
                                modifier = Modifier.constrainAs(voteButton) {
                                    start.linkTo(increaseButton.end,10.dp)
                                    top.linkTo(playersList.bottom,10.dp)
                                    bottom.linkTo(parent.bottom,10.dp)
                                    visibility = if (gameState.showVotes == true) Visibility.Gone else Visibility.Visible
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = {
                                    viewModel.vote(userJoined.id ?: "",fibonacci(fibonacciSeed))
                                }) {
                                Text(
                                    text = "Votar",
                                    style = TextStyle(fontSize = 20.sp, color = Color.Black)
                                )
                            }
                            Button(
                                modifier = Modifier.constrainAs(revealButton) {
                                    start.linkTo(voteButton.end,10.dp)
                                    top.linkTo(playersList.bottom,10.dp)
                                    bottom.linkTo(parent.bottom,10.dp)
                                    visibility = if (gameState.showVotes == true) Visibility.Gone else Visibility.Visible
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = {
                                    viewModel.showVotes()
                                }) {
                                Text(
                                    text = "Mostrar",
                                    style = TextStyle(fontSize = 20.sp, color = Color.Black)
                                )
                            }
                            Button(
                                modifier = Modifier.constrainAs(resetButton) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(playersList.bottom,10.dp)
                                    bottom.linkTo(parent.bottom,10.dp)
                                    visibility = if (gameState.showVotes == false) Visibility.Gone else Visibility.Visible
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                onClick = {
                                    viewModel.resetVotes()
                                }) {
                                Text(
                                    text = "Recomeçar",
                                    style = TextStyle(fontSize = 20.sp, color = Color.Black)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun fibonacci(n: Int) =
    (2 until n).fold(1 to 1) { (prev, curr), _ ->
        curr to (prev + curr)
    }.second

@Composable
fun playerCell(gameState: GameState, user: User) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .width(200.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
        ) {

            val (playerName, playerVote) = createRefs()

            Text(
                text = user.name ?: "",
                style = TextStyle(fontSize = 15.sp),
                modifier = Modifier.constrainAs(playerName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Text(
                text = if (gameState.showVotes == false) {
                    if (user.voted == true) {
                        "VOTOU!"
                    } else {
                        "Votando ..."
                    }
                } else {
                    user.vote.toString()
                },
                style = if (gameState.showVotes == false) {
                    if (user.voted == true) {
                        TextStyle(Color.Green, fontSize = 15.sp)
                    } else {
                        TextStyle(color = Color.LightGray, fontSize = 15.sp)
                    }
                } else {
                    TextStyle(fontSize = 20.sp)
                },
                modifier = Modifier.constrainAs(playerVote) {
                    top.linkTo(playerName.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokerdroidTheme {
        Text("Android")
    }
}