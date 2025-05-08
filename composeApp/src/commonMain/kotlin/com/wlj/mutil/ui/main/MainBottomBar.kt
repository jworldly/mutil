import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wlj.mutil.ui.main.MainState
import com.wlj.shared.tools
import mutil.composeapp.generated.resources.Res
import mutil.composeapp.generated.resources.home
import mutil.composeapp.generated.resources.me
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun MainBottomBar(
    tabIndex: Int,
    modifier: Modifier = Modifier,
    onTabClick: (Int) -> Unit,
) {

    tools.log("MainBottomBar: tabIndex = $tabIndex")

    // 使用 remember 保留 tabData，避免每次重组时重新创建
    val tabData = rememberSaveable {
        listOf(
            TabItem(
                index = 0,
                labelResId = Res.string.home
            ),
            TabItem(
                index = 1,
                labelResId = Res.string.me
            )
        )
    }

    TabRow(
        selectedTabIndex = tabIndex,
        indicator = {},
        modifier = modifier
    ) {
        tabData.forEach { tab ->
            Tab(
                selected = tabIndex == tab.index,
                onClick = {
                    try {
                        onTabClick(tab.index)
                    } catch (e: Exception) {
                        // 处理异常，例如记录日志或显示错误信息
                        tools.log("MainBottomBar: Error selecting tab")
                        e.printStackTrace()
                    }
                },
                icon = {
                    RenderTabIcon(
                        selectedIndex = tabIndex,
                        tab = tab
                    )
                }
            )
        }
    }
}

@Composable
private fun RenderTabIcon(selectedIndex: Int, tab: TabItem) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(
//            painter = painterResource(
//                id = if (selectedIndex == tab.index) tab.selectedIcon else tab.unselectedIcon
//            ),
//            contentDescription = stringResource(id = tab.labelResId),
//            modifier = Modifier.size(22.dp)
//        )
        Text(
            text = stringResource(tab.labelResId),
            style = MaterialTheme.typography.labelSmall,
            color = if (selectedIndex == tab.index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

data class TabItem(
    val index: Int, // 索引，与选中对比
    val labelResId: StringResource, // 标签的资源ID
    val selectedIcon: Int = 0, // 选中时的图标资源ID
    val unselectedIcon: Int = 0,// 未选中时的图标资源ID
)


