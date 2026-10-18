package com.adrenlabs.keyflaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF0A0D12)
private val Panel = Color(0xFF141922)
private val Line = Color(0xFF29313D)
private val Lime = Color(0xFFB7F36B)
private val Muted = Color(0xFF8792A2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EditorApp() }
    }
}

@Composable
private fun EditorApp() {
    var playing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.28f) }
    var selectedTab by remember { mutableStateOf("Edit") }
    var selectedLayer by remember { mutableIntStateOf(0) }
    val layers = remember { mutableStateListOf("Shape 01", "Background") }
    val transition = rememberInfiniteTransition(label = "preview")
    val motion by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse), label = "motion")
    LaunchedEffect(playing) {
        while (playing) {
            kotlinx.coroutines.delay(40)
            progress = (progress + 0.0035f) % 1f
        }
    }
    MaterialTheme(colorScheme = darkColorScheme(background = Ink, surface = Panel, primary = Lime, onBackground = Color.White, onSurface = Color.White)) {
        Column(Modifier.fillMaxSize().background(Ink).padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("KF / STUDIO", color = Lime, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Untitled project", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                }
                Box(Modifier.background(Lime, RoundedCornerShape(10.dp)).clickable { }.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    Text("EXPORT", color = Ink, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("PREVIEW", color = Muted, fontSize = 10.sp, letterSpacing = 1.8.sp, modifier = Modifier.weight(1f))
                Text("1080 × 1920", color = Muted, fontSize = 10.sp)
                Spacer(Modifier.width(12.dp))
                Text("30 FPS", color = Lime, fontSize = 10.sp)
            }
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().weight(1f).background(Color(0xFF10151D), RoundedCornerShape(18.dp)).border(1.dp, Line, RoundedCornerShape(18.dp)).padding(12.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val step = 28.dp.toPx()
                    var x = 0f
                    while (x < size.width) { drawLine(Color(0xFF1A222D), Offset(x, 0f), Offset(x, size.height), 1f); x += step }
                    var y = 0f
                    while (y < size.height) { drawLine(Color(0xFF1A222D), Offset(0f, y), Offset(size.width, y), 1f); y += step }
                    val rectW = size.width * 0.42f
                    val rectH = size.height * 0.34f
                    val left = (size.width - rectW) / 2f + (motion - 0.5f) * size.width * 0.13f
                    val top = (size.height - rectH) / 2f
                    drawRoundRect(Color(0x33B7F36B), Offset(left - 8f, top - 8f), Size(rectW + 16f, rectH + 16f), CornerRadius(18f, 18f))
                    drawRoundRect(Lime, Offset(left, top), Size(rectW, rectH), CornerRadius(18f, 18f))
                    drawCircle(Color.White, 4.dp.toPx(), Offset(size.width / 2f, size.height / 2f))
                }
                Text("PROXY PREVIEW  •  SHAPE 01", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp, modifier = Modifier.align(Alignment.BottomStart).padding(8.dp))
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("00:" + (progress * 60).toInt().toString().padStart(2, '0') + ":00", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Text("00:01:00:00", color = Muted, fontSize = 12.sp)
            }
            Slider(value = progress, onValueChange = { progress = it }, colors = SliderDefaults.colors(thumbColor = Lime, activeTrackColor = Lime, inactiveTrackColor = Line))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { progress = 0f; playing = false }, border = androidx.compose.foundation.BorderStroke(1.dp, Line)) { Text("RESET", color = Muted, fontSize = 11.sp) }
                Button(onClick = { playing = !playing }, colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = Ink), modifier = Modifier.weight(1f)) { Text(if (playing) "Ⅱ  PAUSE" else "▶  PLAY", fontWeight = FontWeight.Bold) }
                OutlinedButton(onClick = { progress = 0.5f }, border = androidx.compose.foundation.BorderStroke(1.dp, Line)) { Text("50%", color = Muted, fontSize = 11.sp) }
            }
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("TIMELINE", color = Muted, fontSize = 10.sp, letterSpacing = 1.8.sp, modifier = Modifier.weight(1f))
                Text("＋ ADD LAYER", color = Lime, fontSize = 10.sp, modifier = Modifier.clickable { layers.add("Shape %02d".format(layers.size)); selectedLayer = layers.lastIndex }.padding(6.dp))
            }
            Spacer(Modifier.height(8.dp))
            layers.forEachIndexed { index, layerName ->
                Row(Modifier.fillMaxWidth().padding(bottom = 7.dp).background(Panel, RoundedCornerShape(10.dp)).border(1.dp, if (selectedLayer == index) Lime.copy(alpha = 0.7f) else Line, RoundedCornerShape(10.dp)).clickable { selectedLayer = index }.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (index == 0) "▧" else "◇", color = Lime, fontSize = 16.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(layerName, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text(if (layerName == "Background") "Solid · #0A0D12" else "Transform · Opacity", color = Muted, fontSize = 10.sp)
                    }
                    Box(Modifier.fillMaxWidth(0.42f).height(23.dp).background(if (layerName != "Background") Color(0xFF31452A) else Color(0xFF252D39), RoundedCornerShape(5.dp))) {
                        Box(Modifier.fillMaxWidth(if (layerName != "Background") 0.72f else 1f).fillMaxHeight().background(if (layerName != "Background") Lime else Color(0xFF4A5667), RoundedCornerShape(5.dp)))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Edit", "Layers", "Effects", "Audio", "Export").forEach { tab ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { selectedTab = tab }) {
                        Text(when (tab) { "Edit" -> "◈"; "Layers" -> "▤"; "Effects" -> "✳"; "Audio" -> "♫"; else -> "⇧" }, color = if (selectedTab == tab) Lime else Muted, fontSize = 16.sp)
                        Text(tab, color = if (selectedTab == tab) Lime else Muted, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}
