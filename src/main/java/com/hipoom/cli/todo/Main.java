package com.hipoom.cli.todo;

import androidx.annotation.NonNull;

import com.hipoom.cli.core.ui.Printer;
import com.hipoom.cli.todo.handler.style.StyleInitializer;

/**
 * @author ZhengHaiPeng
 * @since 2025/2/3 20:48
 */
public class Main {

    public static Printer printer = new Printer() {
        @Override
        public void print(@NonNull String s) {
            System.out.print(s);
        }
    };

    public static void main(String[] args) {
        new TodoApp().run(new String[] { "shell" });
    }

}
