package com.grama.angana.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.grama.angana.R
import com.grama.angana.presentation.auth.AuthScreen
import com.grama.angana.presentation.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private object Route {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val MAIN = "main"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val EVENTS = "events"
    const val REQUESTS = "requests"
    const val PROFILE = "profile"
    const val BOOKING = "booking"
    const val MAINTENANCE = "maintenance"
}

private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    BottomTab(Route.HOME, "Home", Icons.Filled.Home),
    BottomTab(Route.CALENDAR, "Calendar", Icons.Filled.CalendarMonth),
    BottomTab(Route.EVENTS, "Events", Icons.Outlined.Event),
    BottomTab(Route.REQUESTS, "Requests", Icons.Filled.ReceiptLong),
    BottomTab(Route.PROFILE, "Profile", Icons.Filled.Person)
)

@Composable
fun GramaAnganaNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Route.SPLASH) {
        composable(Route.SPLASH) {
            SplashScreen {
                nav.navigate(Route.AUTH) { popUpTo(Route.SPLASH) { inclusive = true } }
            }
        }
        composable(Route.AUTH) {
            val vm: AuthViewModel = hiltViewModel()
            AuthScreen(vm) { nav.navigate(Route.MAIN) { popUpTo(Route.AUTH) { inclusive = true } } }
        }
        composable(Route.MAIN) { MainContainer() }
    }
}

@Composable
private fun SplashScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onDone()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF1976D2)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.16f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Grama-Angana logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(18.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("Grama-Angana", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContainer() {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val current = entry?.destination?.route ?: Route.HOME
    val showBottom = current in tabs.map { it.route }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            if (showBottom) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = { Text(current.replaceFirstChar { it.uppercaseChar() }, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                )
            }
        },
        bottomBar = {
            if (showBottom) {
                NavigationBar(modifier = Modifier.navigationBarsPadding(), tonalElevation = 8.dp) {
                    tabs.forEach { tab ->
                        val selected = current == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                nav.navigate(tab.route) {
                                    popUpTo(Route.HOME)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label, modifier = Modifier.size(if (selected) 24.dp else 22.dp)) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = Route.HOME, modifier = Modifier.padding(padding)) {
            composable(Route.HOME) { HomeScreen(onBooking = { nav.navigate(Route.BOOKING) }, onMaintenance = { nav.navigate(Route.MAINTENANCE) }) }
            composable(Route.CALENDAR) { CalendarScreen() }
            composable(Route.EVENTS) { EventsScreen() }
            composable(Route.REQUESTS) { MyRequestsScreen() }
            composable(Route.PROFILE) { ProfileScreen() }
            composable(Route.BOOKING) { BookingScreen() }
            composable(Route.MAINTENANCE) { MaintenanceHubScreen() }
        }
    }
}

@Composable
private fun HomeScreen(onBooking: () -> Unit, onMaintenance: () -> Unit) = AnimatedScreen {
    val statItems = listOf(
        Triple("Bookings", 128f, ""),
        Triple("Events", 34f, ""),
        Triple("Utilization", 82f, "%")
    )
    val highlightedEvents = listOf(
        Triple("Health Camp", "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?w=1200", "Sunday 10:00 AM"),
        Triple("Village Sports", "https://images.unsplash.com/photo-1521412644187-c49fa049e84d?w=1200", "Next Week"),
        Triple("Women SHG Meet", "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=1200", "15 May")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), MaterialTheme.colorScheme.background)))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp, top = 14.dp)
    ) {
        item {
            Card(shape = MaterialTheme.shapes.extraLarge, elevation = CardDefaults.cardElevation(12.dp)) {
                Box {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1515169067868-5387ec356754?w=1400",
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(190.dp)
                    )
                    Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xE0101D2B)))))
                    Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                        Text("Village Community Hall", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Empowering bookings, events, and maintenance", color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
        item {
            Text("Welcome back, Community Volunteer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Manage bookings and events from one premium dashboard.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                statItems.forEach { (label, value, suffix) ->
                    AnimatedStatCard(label = label, value = value, suffix = suffix, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                ModernQuickActionButton("Book Hall", "Submit request instantly", Icons.Filled.CalendarMonth, Modifier.weight(1f), onBooking)
                ModernQuickActionButton("Maintenance", "Track contributions", Icons.Filled.Wallet, Modifier.weight(1f), onMaintenance)
            }
        }
        item {
            Card(
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Village Utilization", fontWeight = FontWeight.SemiBold)
                        Text("This month +13% compared to last month", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
        item { Text("Featured Events", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(highlightedEvents) { (title, image, schedule) ->
                    HorizontalEventCard(title = title, image = image, schedule = schedule)
                }
            }
        }
        item { Text("Community Updates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
        items(listOf("Health camp this Sunday", "SHG workshop registrations open", "Village youth sports event next week")) { update ->
            Card(shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(5.dp)) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.NotificationsNone, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Text(update)
                }
            }
        }
    }
}

@Composable
private fun AnimatedStatCard(label: String, value: Float, suffix: String, modifier: Modifier) {
    val animatedValue by animateFloatAsState(targetValue = value, animationSpec = spring(dampingRatio = 0.75f, stiffness = 120f), label = "statAnim")
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(vertical = 14.dp, horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (suffix.isEmpty()) "${animatedValue.toInt()}" else "${animatedValue.toInt()}$suffix", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label)
        }
    }
}

@Composable
private fun ModernQuickActionButton(title: String, subtitle: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    PremiumPressCard(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(24.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun HorizontalEventCard(title: String, image: String, schedule: String) {
    PremiumPressCard(onClick = {}, shape = MaterialTheme.shapes.extraLarge, modifier = Modifier.size(width = 260.dp, height = 180.dp)) {
        Box {
            AsyncImage(model = image, contentDescription = title, modifier = Modifier.fillMaxSize())
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xDD101827)))))
            Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(schedule, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun CalendarScreen() = AnimatedScreen {
    val slots = listOf(
        "10 May 10:00-12:00" to "AVAILABLE",
        "10 May 14:00-16:00" to "BOOKED",
        "11 May 10:00-12:00" to "PENDING"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f), MaterialTheme.colorScheme.background)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Legend("Available", Color(0xFF2E7D32))
                Legend("Booked", Color(0xFFC62828))
                Legend("Pending", Color(0xFFF9A825))
            }
        }
        items(slots) { (dateSlot, status) ->
            val color = when (status) {
                "AVAILABLE" -> Color(0xFF2E7D32)
                "BOOKED" -> Color(0xFFC62828)
                else -> Color(0xFFF9A825)
            }
            Card(shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(5.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(dateSlot)
                    Text(status, color = color, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun Legend(label: String, color: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("●", color = color)
        Text(label)
    }
}

@Composable
private fun EventsScreen() = AnimatedScreen {
    val events = listOf(
        Triple("Health Camp", "https://images.unsplash.com/photo-1576765608535-5f04d1e3f289?w=1200", "Free diagnostics and doctor consultation."),
        Triple("Village Sports Meet", "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=1200", "Youth and kids activities at the hall ground."),
        Triple("Skill Training", "https://images.unsplash.com/photo-1524178232363-1fb2b075b655?w=1200", "Digital literacy training for community members.")
    )
    val loadingAnim by rememberLottieComposition(LottieCompositionSpec.Url("https://assets9.lottiefiles.com/packages/lf20_3rwasyjy.json"))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), MaterialTheme.colorScheme.background)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(6.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    LottieAnimation(composition = loadingAnim, iterations = LottieConstants.IterateForever, modifier = Modifier.size(42.dp))
                    Text("Live community events", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        items(events) { (title, image, description) ->
            Card(shape = MaterialTheme.shapes.extraLarge, elevation = CardDefaults.cardElevation(9.dp)) {
                Column {
                    AsyncImage(model = image, contentDescription = null, modifier = Modifier.fillMaxWidth().height(170.dp))
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.LocationOn, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                            Text("Community Hall Campus")
                        }
                        Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingScreen() = AnimatedScreen {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var slot by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snackbar.showSnackbar("Ready to submit your request", duration = SnackbarDuration.Short)
    }

    Scaffold(
        snackbarHost = { AnimatedSnackbarHost(snackbar) },
        contentWindowInsets = WindowInsets.navigationBars
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f), MaterialTheme.colorScheme.background)))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Booking Request", style = MaterialTheme.typography.headlineMedium)
                Text("Fill details to submit your hall booking request.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item { OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(purpose, { purpose = it }, label = { Text("Purpose") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(date, { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(slot, { slot = it }, label = { Text("Time Slot") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(contact, { contact = it }, label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth()) }
            item {
                Button(
                    onClick = {
                        if (name.isNotBlank() && purpose.isNotBlank() && date.isNotBlank()) {
                            name = ""; purpose = ""; date = ""; slot = ""; contact = ""
                            scope.launch { snackbar.showSnackbar("Booking request submitted successfully") }
                        } else {
                            scope.launch { snackbar.showSnackbar("Please fill required details") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Submit Booking Request") }
            }
        }
    }
}

@Composable
private fun MaintenanceHubScreen() = AnimatedScreen {
    val items = listOf(
        Triple("Bulbs", 0.35f, "12 supporters"),
        Triple("Chairs", 0.60f, "20 supporters"),
        Triple("Fans", 0.25f, "9 supporters")
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.06f), MaterialTheme.colorScheme.background)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Text("Maintenance Hub", style = MaterialTheme.typography.headlineMedium) }
        items(items) { (title, progress, supporters) ->
            Card(shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(6.dp)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                    Text(supporters, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun MyRequestsScreen() = AnimatedScreen {
    val requests = listOf(
        "Village meeting - 12 May 10:00 - Approved",
        "Health camp setup - 15 May 14:00 - Pending",
        "Training workshop - 18 May 09:00 - Rejected"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f), MaterialTheme.colorScheme.background)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(requests) { request ->
            Card(shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(4.dp)) {
                Text(request, modifier = Modifier.padding(14.dp))
            }
        }
    }
}

@Composable
private fun ProfileScreen() = AnimatedScreen {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), MaterialTheme.colorScheme.background)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(shape = MaterialTheme.shapes.extraLarge, elevation = CardDefaults.cardElevation(8.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(48.dp), shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.secondaryContainer) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(Modifier.size(10.dp))
                Column {
                    Text("Community User", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text("grama.user@angana.in", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        ProfileTile("Role", "Public User")
        ProfileTile("Village", "Gram Panchayat Zone 2")
        ProfileTile("Booking History", "8 requests submitted")
        ProfileTile("Community Preference", "Events + Maintenance alerts")
    }
}

@Composable
private fun ProfileTile(title: String, value: String) {
    Card(shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AnimatedScreen(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(320)) + slideInVertically(animationSpec = tween(360), initialOffsetY = { it / 8 }),
        exit = fadeOut(animationSpec = tween(220)) + slideOutVertically(animationSpec = tween(220), targetOffsetY = { -it / 10 })
    ) { content() }
}

@Composable
private fun PremiumPressCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable ColumnScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.98f else 1f, label = "pressScale")
    Card(
        onClick = onClick,
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale).clip(shape),
        shape = shape,
        interactionSource = interaction,
        elevation = CardDefaults.cardElevation(defaultElevation = if (pressed) 3.dp else 7.dp)
    ) {
        Column(content = content)
    }
}

@Composable
private fun AnimatedSnackbarHost(hostState: SnackbarHostState) {
    AnimatedVisibility(
        visible = hostState.currentSnackbarData != null,
        enter = fadeIn(animationSpec = tween(220)) + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut(animationSpec = tween(180)) + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        SnackbarHost(hostState = hostState) { data ->
            Snackbar(snackbarData = data, shape = RoundedCornerShape(14.dp))
        }
    }
}
