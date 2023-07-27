package com.todo.app.Utility;

import javax.swing.JOptionPane;

public class ErrorPopup {
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "エラー", JOptionPane.ERROR_MESSAGE);
    }
}
