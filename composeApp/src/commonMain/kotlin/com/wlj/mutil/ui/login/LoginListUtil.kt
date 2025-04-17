package com.wlj.mutil.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wlj.shared.ListItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * @Author: wlj
 * @Date: 2025/3/11
 * @Description: 登录列表
 */

@Composable
fun LoginList( modifier: Modifier,loginItems: List<ListItem>) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        loginItems.forEach {
            Item(it)
        }
    }
}

/**
 * @param item 数据对象
 */
@Composable
private fun Item(item: ListItem) {

    Surface(
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = item.onClick)
                .padding(start = 15.dp, end = 10.dp)
                .height(43.dp)
        ) {
            Image(
                painter = painterResource(item.icon),
                contentDescription = null
            )
            Text(
                text = stringResource(item.title),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}
