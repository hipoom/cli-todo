package com.hipoom.cli.todo.handler.style

import com.hipoom.cli.core.ui.palette.Color

/**
 * @author ZhengHaiPeng
 * @since 2026/3/1 0:24
 *
 */
interface Style {

    val pinTextColor: Color?

    val pinBackgroundColor: Color?

    val commentTextColor: Color?

    val commentBackgroundColor: Color?
}