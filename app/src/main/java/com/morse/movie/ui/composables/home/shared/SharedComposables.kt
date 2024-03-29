package com.morse.movie.ui.composables.home.shared

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.morse.movie.R
import com.morse.movie.data.entities.remote.CastResponse
import com.morse.movie.data.entities.remote.DetailsResponse
import com.morse.movie.data.entities.ui.MediaActionsStatus
import com.morse.movie.ui.theme.Shapes
import com.morse.movie.utils.Constants
import com.morse.movie.utils.customBackground
import com.morse.movie.utils.openYoutubeTrailer
import com.morse.movie.utils.shareMedia

@Preview(showSystemUi = true)
@Composable
fun ShowSharedComposable() {
    Column {
        MediaItem(imageUrl = "", name = " Morse") {}
        Rate(modifier = Modifier, rateValue = "9.5")
        RatedMediaItem(
            imageUrl = "",
            mediaName = "Ahmed Morse",
            mediaYear = "2022",
            rateValue = "9.9"
        ) {}
    }
}

@Composable
fun MediaItem(imageUrl: String, name: String, onClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier
        .padding(horizontal = 10.dp)
        .clickable { onClick.invoke() }) {

        val (poster, title) = createRefs()

        MediaImage(
            modifier = Modifier
                .size(Constants.regularImageWidth, Constants.regularImageHeight)
                .constrainAs(poster) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            url = imageUrl
        )
        Text(
            text = name, color = Color(0xFF222222), style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(poster.bottom, 10.dp)
                start.linkTo(poster.start)
                end.linkTo(poster.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun Loading(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Color(0XFFF89A04), strokeWidth = 5.dp
    )
}


@Preview(showSystemUi = true)
@Composable
fun ShowErrorLayout() {
    Error(Modifier) {

    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Error(modifier: Modifier, onRetry: () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.errorMessage),
            color = Color.Black,
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center ,
            fontSize = TextUnit(
                15F,
                TextUnitType.Sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {onRetry.invoke()} , colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFFFD7624))) {
            Text(
                text = stringResource(id = R.string.retry),
                color = Color.White,
                style = MaterialTheme.typography.h1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center ,
                fontSize = TextUnit(
                    15F,
                    TextUnitType.Sp
                )
            )
        }
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun Empty(modifier: Modifier, @StringRes message: Int) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0F)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = message),
            color = Color.Black,
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(
                15F,
                TextUnitType.Sp
            )
        )
    }
}

@Composable
fun RatedMediaItem(
    imageUrl: String,
    mediaName: String,
    mediaYear: String,
    rateValue: String,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
            .clickable { onClick.invoke() }
    ) {
        val (background, rate, name, year) = createRefs()
        MediaImage(
            modifier = Modifier
                .constrainAs(background) { top.linkTo(parent.top) }
                .size(
                    Constants.regularImageWidth, Constants.ratedRegularImageHeight
                ), url = imageUrl
        )
        Rate(modifier = Modifier.constrainAs(rate) {
            end.linkTo(background.end, 10.dp)
            top.linkTo(background.top, 10.dp)
        }, rateValue = rateValue)
        Text(
            text = mediaName,
            modifier = Modifier.constrainAs(name) {
                bottom.linkTo(background.bottom, 10.dp)
                linkTo(
                    start = background.start,
                    end = background.end,
                    startMargin = 10.dp,
                    endMargin = 10.dp,
                    bias = 0.0F
                )
                width = Dimension.fillToConstraints
            },
            color = Color.White,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = mediaYear, modifier = Modifier.constrainAs(year) {
            bottom.linkTo(name.top, 1.dp)
            start.linkTo(name.start)

        }, color = Color.White, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun TVRatedMediaItem(imageUrl: String, mediaName: String, rateValue: String, onClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier
        .padding(vertical = 5.dp)
        .clickable { onClick.invoke() }) {
        val (background, rate, name) = createRefs()
        MediaImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(Constants.tvImageHeight)
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    linkTo(parent.start, parent.end)
                }, url = imageUrl
        )
        Rate(modifier = Modifier.constrainAs(rate) {
            end.linkTo(background.end, 10.dp)
            top.linkTo(background.top, 10.dp)
        }, rateValue = rateValue)
        Text(
            text = mediaName, modifier = Modifier.constrainAs(name) {
                top.linkTo(background.bottom, 10.dp)
                start.linkTo(background.start)
                linkTo(background.start, background.end, bias = 0.0F)
            }, color = Color.Black,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Composable
fun RateBar(modifier: Modifier, count: Int, until: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(count) {
            val currentStar = it + 1
            if (until >= currentStar) {
                Image(
                    painter = painterResource(id = R.drawable.yellow_star),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(5.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.gray_star),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun MediaImage(modifier: Modifier = Modifier, url: String, shape: Shape = Shapes.large) {
    AsyncImage(
        modifier = modifier
            .clip(shape)
            .shadow(10.dp, ambientColor = Color.Black),
        model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .build(),
        placeholder = painterResource(id = R.drawable.placeholder),
        contentScale = ContentScale.FillBounds,
        contentDescription = ""
    )
}

@Composable
fun Rate(modifier: Modifier, rateValue: String) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val rate = rateValue.split('.')
        Image(
            painter = painterResource(id = R.drawable.rate_shape), contentDescription = null
        )
        Row {
            Text(text = rate.first(), color = Color.White, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.width(1.dp))
            Text(
                text = ".${rate.last()}",
                color = Color.White,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun Fab(
    modifier: Modifier,
    isSelected: Boolean,
    @DrawableRes selectedIcon: Int,
    @DrawableRes unselectedIcon: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick.invoke() }, modifier = Modifier
            .size(56.dp)
            .customBackground(isSelected)
            .then(modifier)
    ) {
        androidx.compose.material3.Icon(
            painter = painterResource(id = if (isSelected) selectedIcon else unselectedIcon),
            contentDescription = null,
            tint = if (isSelected) Color.White else Color(0XFF979797),
        )
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun RenderActors(
    modifier: Modifier,
    casts: ArrayList<CastResponse.Cast>,
    scrollableState: ScrollableState = rememberScrollState(),
) {
    Column(
        modifier = Modifier
            .scrollable(scrollableState, Orientation.Vertical)
            .then(modifier)
    ) {
        Text(
            text = stringResource(id = R.string.full_cast_and_crew),
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Companion.Justify,
            color = Color(0XFF666666),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = TextUnit(18F, TextUnitType.Sp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(modifier = modifier) {
            items(casts) {
                MediaItem(
                    imageUrl = it.getPosterPath(),
                    name = it.name
                ) {

                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
fun ConstraintLayoutScope.RenderDetails(
    onBackPressed: () -> Unit,
    detailsModel: DetailsResponse,
    castModel: ArrayList<CastResponse.Cast>,
    actionsStatus: MediaActionsStatus = MediaActionsStatus(),
    onLikedPressed: (Boolean) -> Unit,
    onStaredPressed: (Boolean) -> Unit,
    onCommentedPressed: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val topGuideline = createGuidelineFromTop(0.05F)
    val textDetailsGuideline = createGuidelineFromTop(0.29F)
    val detailGuidelineBottom = createGuidelineFromTop(0.55F)
    val (name, watching, rateNumbers, rateStars, playFab,
        details, likeFab, starFab, commentsFab, backBtn,
        shareBtn, detailBackground, rateBar, rateValueInt,
        rateValueDouble, cast) = createRefs()
    MediaImage(
        url = detailsModel.getBackgroundImage(),
        modifier = Modifier.constrainAs(detailBackground) {
            linkTo(parent.top, detailGuidelineBottom)
            linkTo(parent.start, parent.end)
            height = Dimension.fillToConstraints
            width = Dimension.fillToConstraints
        },
        shape = RectangleShape
    )

    Row(modifier = Modifier
        .constrainAs(backBtn) {
            start.linkTo(parent.start, 10.dp)
            top.linkTo(topGuideline)
        }
        .clickable {
            onBackPressed.invoke()
        }, verticalAlignment = Alignment.CenterVertically
    ) {

        androidx.compose.material3.Icon(
            painter = painterResource(id = R.drawable.back_icon),
            contentDescription = null,
            tint = Color.White
        )
        Text(
            text = stringResource(id = R.string.back),
            style = MaterialTheme.typography.h1,
            fontSize = TextUnit(16F, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

    IconButton(modifier = Modifier.constrainAs(shareBtn) {
        top.linkTo(backBtn.top)
        bottom.linkTo(backBtn.bottom)
        end.linkTo(parent.end, 10.dp)
    }, onClick = {
        context.shareMedia(detailsModel)
    }) {
        androidx.compose.material3.Icon(
            painter = painterResource(id = R.drawable.share_icon),
            contentDescription = null,
            tint = Color.White
        )
    }

    Text(
        text = detailsModel.title,
        modifier = Modifier.constrainAs(name) {
            linkTo(backBtn.start, shareBtn.end)
            top.linkTo(textDetailsGuideline)
            width = Dimension.fillToConstraints
        },
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = TextUnit(22F, TextUnitType.Sp)
    )

    Text(
        text = stringResource(id = R.string.people_watching, detailsModel.popularity.toString()),
        modifier = Modifier.constrainAs(watching) {
            linkTo(backBtn.start, shareBtn.end)
            top.linkTo(name.bottom, 5.dp)
            width = Dimension.fillToConstraints
        },
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = TextUnit(18F, TextUnitType.Sp)
    )

    Text(
        text = detailsModel.getVoteDecimal(),
        modifier = Modifier.constrainAs(rateValueInt) {
            start.linkTo(watching.start)
            top.linkTo(watching.bottom, 5.dp)
        },
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Normal,
        color = Color(0XFFFECB2F),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = TextUnit(18F, TextUnitType.Sp)
    )
    Text(
        text = detailsModel.getVoteFraction(),
        modifier = Modifier.constrainAs(rateValueDouble) {
            start.linkTo(rateValueInt.end)
            top.linkTo(rateValueInt.top)
        },
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Normal,
        color = Color(0XFFFECB2F),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = TextUnit(14F, TextUnitType.Sp)
    )

    RateBar(modifier = Modifier.constrainAs(rateBar) {
        linkTo(rateValueInt.top, rateValueInt.bottom, bias = 0.5F)
        start.linkTo(rateValueDouble.end, 10.dp)
    }, count = 5, until = 4)


    Fab(
        modifier = Modifier
            .constrainAs(playFab) {
                linkTo(rateBar.end, parent.end, endMargin = 10.dp, bias = 1F)
                top.linkTo(watching.top)
            },
        isSelected = true,
        selectedIcon = R.drawable.play,
        unselectedIcon = R.drawable.play
    ) {
        context.openYoutubeTrailer(id = detailsModel.id)
    }


    Text(
        text = detailsModel.overview,
        modifier = Modifier.constrainAs(details) {
            start.linkTo(watching.start)
            linkTo(playFab.bottom, detailBackground.bottom, 5.dp)
            linkTo(backBtn.start, shareBtn.end)
            height = Dimension.fillToConstraints
            width = Dimension.fillToConstraints
        },
        style = MaterialTheme.typography.h1,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Normal,
        textAlign = TextAlign.Justify,
        color = Color.White,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis,
        fontSize = TextUnit(18F, TextUnitType.Sp)
    )

    RenderActors(modifier = Modifier.constrainAs(cast) {
        linkTo(detailBackground.bottom, likeFab.top, 10.dp)
        linkTo(backBtn.start, shareBtn.end)
        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }, castModel)

    DetailsAction(
        onClick = {
            actionsStatus.liked.value = actionsStatus.liked.value.not()
            onLikedPressed.invoke(actionsStatus.liked.value)
        },
        modifier = Modifier
            .constrainAs(likeFab) {
                bottom.linkTo(parent.bottom, 10.dp)
                linkTo(parent.start, starFab.start)
            },
        isSelected = actionsStatus.liked.value,
        count = "230k",
        unselectedIcon = R.drawable.unselected_like,
        selectedIcon = R.drawable.selected_like
    )

    DetailsAction(
        onClick = {
            actionsStatus.stared.value = actionsStatus.stared.value.not()
            onStaredPressed.invoke(actionsStatus.stared.value)
        },
        modifier = Modifier
            .constrainAs(starFab) {
                bottom.linkTo(parent.bottom, 10.dp)
                linkTo(likeFab.end, commentsFab.start)
            }, isSelected = actionsStatus.stared.value,
        count = "2003",
        unselectedIcon = R.drawable.unselected_star,
        selectedIcon = R.drawable.selected_star
    )

    DetailsAction(
        onClick = {
            actionsStatus.comments.value = actionsStatus.comments.value.not()
            onCommentedPressed.invoke(actionsStatus.comments.value)
        },
        modifier = Modifier
            .constrainAs(commentsFab) {
                bottom.linkTo(parent.bottom, 10.dp)
                linkTo(starFab.end, parent.end)
            },
        isSelected = actionsStatus.comments.value,
        count = "390",
        unselectedIcon = R.drawable.unselected_comment,
        selectedIcon = R.drawable.selected_comment
    )


}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DetailsAction(
    modifier: Modifier,
    isSelected: Boolean,
    count: String,
    @DrawableRes unselectedIcon: Int,
    @DrawableRes selectedIcon: Int,
    onClick: () -> Unit
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Fab(
            modifier = modifier,
            isSelected = isSelected,
            selectedIcon = selectedIcon,
            unselectedIcon = unselectedIcon
        ) {
            onClick.invoke()
        }

        Text(
            text = count,
            style = MaterialTheme.typography.h1,
            color = Color(0xFF999999),
            fontWeight = FontWeight.Medium,
            fontSize = TextUnit(13F, TextUnitType.Sp),
        )

    }
}